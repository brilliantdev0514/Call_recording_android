package vn.harry.callrecorder.ui.option;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.provider.DocumentFile;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import vn.harry.callrecorder.R;
import vn.harry.callrecorder.entity.MessageEvent;
import vn.harry.callrecorder.entity.Recording;
import vn.harry.callrecorder.helper.FileHelper;
import vn.harry.callrecorder.ui.all.AllCallFragment;
import vn.harry.callrecorder.util.ImageUtil;
import vn.harry.callrecorder.util.MySharedPreferences;
import vn.harry.callrecorder.util.StringUtils;

@SuppressLint("ValidFragment")
public class BottomSheetFragment extends BottomSheetDialogFragment {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
    public static final String EVENT_BUS_UPDATE = "key_event_bus";
    public static final String TAG = "BottomSheetFragment";
    private Unbinder unbinder;
    private Recording item;
    @BindView(R.id.nameContact)
    TextView nameContact;
    @BindView(R.id.phoneContact)
    TextView phoneContact;
    @BindView(R.id.timeCall)
    TextView timeCall;
    @BindView(R.id.ivCall)
    ImageView ivCall;
    @BindView(R.id.ivContact)
    ImageView ivContact;
    @BindView(R.id.important)
    CheckBox important;
    private int position_paper;
    private int theme;

    public static BottomSheetFragment newInstance(Bundle bundle) {
        BottomSheetFragment fragment = new BottomSheetFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        item = bundle.getParcelable(AllCallFragment.KEY_ITEM);
        position_paper = bundle.getInt(AllCallFragment.KEY_POSITION_PAPER);

        StringUtils.setText(item.getUserName(), nameContact);
        StringUtils.setText(item.getPhoneNumber(), phoneContact);
        try {
            StringUtils.setText(DateFormat
                    .format("MMM d, yyyy hh:mm",
                            formatter.parse(item.getIdCall()))
                    .toString(), timeCall);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (item.isOutGoing()) {
            ImageUtil.setImageView(getActivity(), R.drawable.ic_out_going, ivCall);
        } else {
            ImageUtil.setImageView(getActivity(), R.drawable.ic_in_coming, ivCall);
        }
        if (item.isImportant()) {
            important.setChecked(true);
        } else {
            important.setChecked(false);
        }
        important.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                Recording exist = Recording.findById(item.getIdCall());
                if (exist != null) {
                    exist.setImportant(isCheck);
                    exist.save();
                }
            }
        });
        onChangeTheme();
    }

    private void onChangeTheme() {
        theme = MySharedPreferences.getInstance(getContext()).getInt(MySharedPreferences.KEY_THEMES, R.style.AppTheme_Red);
        switch (theme) {
            case R.style.AppTheme_Red:
                ImageUtil.setImageView(getContext(), R.drawable.ic_user_red, ivContact);
                break;
            case R.style.AppTheme_Green:
                ImageUtil.setImageView(getContext(), R.drawable.ic_user_green, ivContact);
                break;
            case R.style.AppTheme_Blue:
                ImageUtil.setImageView(getContext(), R.drawable.ic_user_blue, ivContact);
                break;
            case R.style.AppTheme_Orange:
                ImageUtil.setImageView(getContext(), R.drawable.ic_user_orange, ivContact);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.play, R.id.volume, R.id.addNote, R.id.call, R.id.delete, R.id.share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                startPlayExternal();
                dismiss();
                break;
            case R.id.volume:
                volumeControl();
                dismiss();
                break;
            case R.id.addNote:
                showAddNoteDialog(item.getIdCall());
                dismiss();
                break;
            case R.id.call:
                Uri number = Uri.parse("tel:" + item.getPhoneNumber());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
                dismiss();
                break;
            case R.id.delete:
                Recording exist = Recording.findById(item.getIdCall());
                if (MySharedPreferences.getInstance(getActivity()).getBoolean(MySharedPreferences.KEY_RECYCLE, false)) {
                    if (exist != null) {
                        exist.setDelete(true);
                        exist.save();
                    }
                } else {
                    assert exist != null;
                    FileHelper.deleteRecord(getActivity(), exist.getUri());
                    /*xóa db*/
                    exist.delete();
                }
                EventBus.getDefault().post(new MessageEvent(EVENT_BUS_UPDATE, position_paper));
                dismiss();
                break;
            case R.id.share:
                sendMail(item.getFileName());
                break;
        }
    }

    private void showAddNoteDialog(String idCall) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.add_note);
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);
        alertDialog.setPositiveButton(getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String note = input.getText().toString();
                        Recording recording = Recording.findById(idCall);
                        if (recording != null && !TextUtils.isEmpty(note)) {
                            recording.setNote(note);
                            recording.save();
                            EventBus.getDefault().post(new MessageEvent(EVENT_BUS_UPDATE, position_paper));
                        }
                    }
                });

        alertDialog.setNeutralButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private final Handler handler = new Handler();
    private int mediaFileLengthInMilliseconds;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SeekBar seekBarProgress;

    private void startPlayInternal(Recording item) {
        if (item == null) {
            return;
        }
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.player);
        TextView textView = dialog.findViewById(R.id.playerTitle);
        textView.setText(item.getFileName());
        ImageView buttonPlayPause = dialog.findViewById(R.id.playerPlayPause);
        seekBarProgress = dialog.findViewById(R.id.playerSeekBar);
        seekBarProgress.setProgress(0);
        seekBarProgress.setMax(100);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });

        buttonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        buttonPlayPause.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        mediaPlayer.seekTo((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));
                        primarySeekBarProgressUpdater();
                        buttonPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
            }
        });
        try {
            if (item.getUri() == null) return;
            mediaPlayer.setDataSource(getActivity(), Uri.parse(item.getUri()));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
    }

    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    private void sendMail(String fileName) {
        if (fileName == null) return;
        DocumentFile file = FileHelper.getStorageFile(getActivity())
                .findFile(fileName);
        Uri uri = FileHelper.getContentUri(getActivity(), file.getUri());

        Intent sendIntent = new Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_SUBJECT,
                        getActivity().getString(R.string.mail_subject))
                .putExtra(Intent.EXTRA_TEXT,
                        getActivity().getString(R.string.mail_body))
                .putExtra(Intent.EXTRA_STREAM, uri)
                .setData(uri)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setType("audio/3gpp");

        getActivity().startActivity(Intent.createChooser(sendIntent,
                getActivity().getString(R.string.send_mail)));
    }

    private void startPlayExternal() {
        try {
            if (item == null || TextUtils.isEmpty(item.getUri())) {
                return;
            }
            Uri uri = Uri.parse(item.getUri());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setDataAndType(uri, "audio/3gpp");
            getActivity().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), getString(R.string.can_recorder), Toast.LENGTH_SHORT).show();
        }

    }
    private void volumeControl(){
        Toast.makeText(getActivity(), "Volume", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
