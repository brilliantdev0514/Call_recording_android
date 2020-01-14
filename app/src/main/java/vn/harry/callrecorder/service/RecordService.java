package vn.harry.callrecorder.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vn.harry.callrecorder.App;
import vn.harry.callrecorder.R;
import vn.harry.callrecorder.entity.Recording;
import vn.harry.callrecorder.helper.FileHelper;
import vn.harry.callrecorder.util.Constants;
import vn.harry.callrecorder.util.MySharedPreferences;
import vn.harry.callrecorder.util.UserPreferences;

public class RecordService extends Service {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
    private MediaRecorder recorder;
    private String phoneNumber;

    private DocumentFile file = null;
    private boolean onCall = false;
    private boolean recording = false;
    private boolean onForeground = false;
    private String idCall;
    public static AudioManager audioManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.TAG, "RecordService onStartCommand");
        if (intent == null)
            return START_NOT_STICKY;

        int commandType = intent.getIntExtra("commandType", 0);
        if (commandType == 0)
            return START_NOT_STICKY;

        boolean enabled = UserPreferences.getEnabled();

        switch (commandType) {
            case Constants.RECORDING_ENABLED:
                Log.d(Constants.TAG, "RecordService RECORDING_ENABLED");
                if (enabled && phoneNumber != null && onCall && !recording) {
                    Log.d(Constants.TAG, "RecordService STATE_START_RECORDING");
                    idCall = formatter.format(new Date());
                    startRecordingBySource();
                }
                break;
            case Constants.RECORDING_DISABLED:
                Log.d(Constants.TAG, "RecordService RECORDING_DISABLED");
                if (onCall && phoneNumber != null && recording) {
                    Log.d(Constants.TAG, "RecordService STATE_STOP_RECORDING");
                    stopAndReleaseRecorder();
                    recording = false;
                }
                break;
            case Constants.STATE_INCOMING_NUMBER:
                Log.d(Constants.TAG, "RecordService STATE_INCOMING_NUMBER");
                if (phoneNumber == null)
                    phoneNumber = intent.getStringExtra("phoneNumber");
                break;
            case Constants.STATE_CALL_START:
                Log.d(Constants.TAG, "RecordService STATE_CALL_START");
                onCall = true;
                if (enabled && phoneNumber != null && !recording) {
                    idCall = formatter.format(new Date());
                    startRecordingBySource();
                }
                break;
            case Constants.STATE_CALL_END:
                Log.d(Constants.TAG, "RecordService STATE_CALL_END");
                onCall = false;
                phoneNumber = null;
                recording = false;
                stopAndReleaseRecorder();
                break;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.TAG, "RecordService onDestroy");
        stopAndReleaseRecorder();
        super.onDestroy();
    }

    /// In case it is impossible to record
    private void terminateAndEraseFile() {
        Log.d(Constants.TAG, "RecordService terminateAndEraseFile");
        stopAndReleaseRecorder();
        recording = false;
        if (file != null)
            deleteFile();
    }

    private void deleteFile() {
        Log.d(Constants.TAG, "RecordService deleteFile");
        file.delete();
        file = null;
    }

    private void stopAndReleaseRecorder() {
        if (recorder == null)
            return;
        Log.d(Constants.TAG, "RecordService stopAndReleaseRecorder");
        boolean recorderStopped = false;
        boolean exception = false;
        try {
            recorder.stop();
            recorderStopped = true;
        } catch (IllegalStateException e) {
            Log.d(Constants.TAG, "RecordService: Failed to stop recorder.  Perhaps it wasn't started?", e);
            exception = true;
        } catch (RuntimeException e) {
            Log.d(Constants.TAG, "RecordService: Failed to stop recorder.  RuntimeException", e);
            exception = true;
        }
        recorder.reset();
        recorder.release();
        recorder = null;
        if (exception) {
            App.isOutComming = false;
            deleteFile();
        }
        if (recorderStopped) {
            Toast.makeText(this, this.getString(R.string.receiver_end_call),
                    Toast.LENGTH_SHORT)
                    .show();
            Recording recording = new Recording();
            recording.setIdCall(idCall);
            recording.setOutGoing(App.isOutComming);
            App.isOutComming = false;
            recording.save();
            file = null;
            Log.d(Constants.TAG, "RecordService save");
        }
    }

    private void startRecordingBySource() {
        boolean exception = false;
        int source = MySharedPreferences.getInstance(this).getInt(MySharedPreferences.KEY_AUDIO_SOURCE, 0);
        Log.d(Constants.TAG, "RecordService source: " + source);
        if (source != 0) {
            exception = startRecording(source);
        } else {
            exception = startRecording(MediaRecorder.AudioSource.VOICE_CALL);
            if (exception) {
                exception = startRecording(MediaRecorder.AudioSource.MIC);
                if (!exception) {
                    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(3, audioManager.getStreamMaxVolume(3), 0);
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    audioManager.setSpeakerphoneOn(true);
                }
            }
        }
        if (exception) {
            App.isOutComming = false;
            Log.e(Constants.TAG, "Failed to set up recorder.");
            terminateAndEraseFile();
            Toast toast = Toast.makeText(this,
                    this.getString(R.string.record_impossible),
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean startRecording(int source) {
        Log.d(Constants.TAG, "RecordService startRecording");
        if (recorder == null) {
            recorder = new MediaRecorder();
        }
        try {
            recorder.reset();
            recorder.setAudioSource(source);
//            recorder.setAudioSamplingRate(8000);
//            recorder.setAudioEncodingBitRate(12200);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setAudioChannels(1);
            recorder.setAudioSamplingRate(44100);
            recorder.setAudioEncodingBitRate(192000);
            if (file == null) {
                file = FileHelper.getFile(this, phoneNumber);
            }
            ParcelFileDescriptor fd = getContentResolver()
                    .openFileDescriptor(file.getUri(), "w");
            if (fd == null)
                throw new Exception("Failed open recording file.");
            recorder.setOutputFile(fd.getFileDescriptor());
            recorder.setOnErrorListener((mr, what, extra) -> {
                Log.e(Constants.TAG, "OnErrorListener " + what + "," + extra);
                terminateAndEraseFile();
            });

            recorder.setOnInfoListener((mr, what, extra) -> {
                Log.e(Constants.TAG, "OnInfoListener " + what + "," + extra);
                terminateAndEraseFile();
            });

            recorder.prepare();

            // Sometimes the recorder takes a while to start up
            Thread.sleep(2000);

            recorder.start();
            recording = true;

            Log.d(Constants.TAG, "RecordService: Recorder started!");
            Toast toast = Toast.makeText(this,
                    this.getString(R.string.receiver_start_call),
                    Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } catch (Exception e) {
            Log.d(Constants.TAG, "RecordService: Exception!");
        }
        return true;
    }
}
