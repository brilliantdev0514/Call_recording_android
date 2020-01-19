package com.smart.callrec.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.provider.DocumentFile;
import android.telephony.PhoneNumberUtils;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.smart.callrec.entity.Recording;
import com.smart.callrec.util.Constants;
import com.smart.callrec.util.UserPreferences;

public class FileHelper {
    /**
     * Returns a file descriptor for a new recording file in write mode.
     *
     * @throws Exception
     */
    public static DocumentFile getFile(Context context, @NonNull String phoneNumber) throws Exception {
        String date = (String) DateFormat.format("yyyyMMddHHmmss", new Date());
        String filename = date + "_" + cleanNumber(phoneNumber);

        return getStorageFile(context).createFile("audio/3gpp", filename);
    }

    public static void deleteAllRecords(Context context) {
        DocumentFile[] dirList = getStorageFile(context).listFiles();
        if (dirList == null)
            return;
        for (DocumentFile file : dirList) {
            file.delete();
        }
    }

    public static void deleteRecord(Context context, String uri) {
        DocumentFile[] dirList = getStorageFile(context).listFiles();
        if (dirList == null)
            return;
        for (DocumentFile file : dirList) {
            if (file.getUri().toString().equals(uri))
                file.delete();
        }
    }

    /// Obtains a contact name coresponding to a phone number.
    private static String getContactName(String phoneNum, Context context) {
        @SuppressWarnings("deprecation")
        String res = PhoneNumberUtils.formatNumber(phoneNum);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context.checkSelfPermission(Manifest.permission.READ_CONTACTS) !=
                        PackageManager.PERMISSION_GRANTED) {
            return res;
        }
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor names = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (names == null)
            return res;

        int indexName = names.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = names.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        if (names.getCount() > 0) {
            names.moveToFirst();
            do {
                String name = names.getString(indexName);
                String number = cleanNumber(names.getString(indexNumber));

                if (PhoneNumberUtils.compare(number, phoneNum)) {
                    res = name;
                    break;
                }
            } while (names.moveToNext());
        }
        names.close();

        return res;
    }

    private static String cleanNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    /// Fetches list of previous recordings
    public static List<Recording> listRecordings(Context context) {
        final DocumentFile directory = getStorageFile(context);

        DocumentFile[] files = directory.listFiles();
        List<Recording> fileList = new ArrayList<>();
        for (DocumentFile file : files) {
            if (!file.getName().matches(Constants.FILE_NAME_PATTERN)) {
                Log.d(Constants.TAG, String.format(
                        "'%s' didn't match the file name pattern",
                        file.getName()));
                continue;
            }
            Recording recording = new Recording(file.getName());
            recording.setUri(file.getUri().toString());
            String phoneNum = recording.getPhoneNumber();
            recording.setUserName(getContactName(phoneNum, context));
            fileList.add(recording);
        }

        return fileList;
    }

    /// Get the number of free bytes that are available on the external storage.
    @SuppressWarnings("deprecation")
    public static long getFreeSpaceAvailable(String path) {
        StatFs stat = new StatFs(path);
        long availableBlocks;
        long blockSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            availableBlocks = stat.getAvailableBlocksLong();
            blockSize = stat.getBlockSizeLong();
        } else {
            availableBlocks = stat.getAvailableBlocks();
            blockSize = stat.getBlockSize();
        }
        return availableBlocks * blockSize;
    }

    /**
     * Takes a size in bytes and converts it into a human-readable
     * String with units.
     */
    public static String addUnits(final long input) {
        int i = 0;
        long result = input;
        while (i <= 3 && result >= 1024)
            result = input / (long) Math.pow(1024, ++i);

        switch (i) {
            default:
                return result + " B";
            case 1:
                return result + " KiB";
            case 2:
                return result + " MiB";
            case 3:
                return result + " GiB";
        }
    }

    public static DocumentFile getStorageFile(Context context) {
        Uri uri = UserPreferences.getStorageUri();
        String scheme = uri.getScheme();
        if (scheme == null || scheme.equals("file")) {
            return DocumentFile.fromFile(new File(uri.getPath()));
        } else {
            return DocumentFile.fromTreeUri(context, uri);
        }
    }

    public static Uri getContentUri(Context context, Uri uri) {
        if (uri.getScheme() == "content")
            return uri;
        return FileProvider.getUriForFile(context,
                "com.callrecorder.android.fileprovider",
                new File(uri.getPath()));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isStorageWritable(Context context) {
        return getStorageFile(context).canWrite();
    }

    public static boolean isStorageReadable(Context context) {
        return getStorageFile(context).canRead();
    }

    public static String getDuration(File file) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return formateMilliSeccond(Long.parseLong(durationStr));
    }

    public static String formateMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString;
    }

}
