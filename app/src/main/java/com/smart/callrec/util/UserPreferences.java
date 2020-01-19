package com.smart.callrec.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

import com.smart.callrec.R;
import com.smart.callrec.service.RecordService;

public class UserPreferences {
    private static Context context = null;
    private static SharedPreferences prefs = null;
    public static Uri default_storage = null;

    public static void init(Context ctx) {
        context = ctx;
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        default_storage = Uri.fromFile(Environment.getExternalStorageDirectory());
    }

    private static void setString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit()
                .putString(key, value);
        editor.apply();
    }

    public static void setInt(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit()
                .putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit()
                .putBoolean(key, value);
        editor.apply();
    }
    public static boolean getOutGoing(boolean defaultValue) {
        return prefs.getBoolean(MySharedPreferences.KEY_OUT_GOING, defaultValue);
    }
    public static Uri getStorageUri() {
        String str = prefs.getString("storage_location", null);
        return str == null ? default_storage : Uri.parse(str);
    }

    public static void setStorageUri(Uri uri) {
        setString("storage_location", uri.toString());
    }

    public static boolean getEnabled() {
        return prefs.getBoolean("enabled", false);
    }

    public static void setEnabled(boolean enabled) {
        setBoolean("enabled", enabled);

        context.startService(new Intent(context, RecordService.class)
                .putExtra("commandType", enabled ? Constants.RECORDING_ENABLED
                        : Constants.RECORDING_DISABLED)
                .putExtra("enabled", enabled));
    }

    public static boolean getWelcomeSeen() {
        return prefs.getBoolean("welcome_seen", false);
    }

    public static void setWelcomeSeen() {
        setBoolean("welcome_seen", true);
    }
}
