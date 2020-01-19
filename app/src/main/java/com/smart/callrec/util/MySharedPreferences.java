package com.smart.callrec.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferences {
    public static final String KEY_GA = "sendToGA";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_PIN = "pin";
    public static final String KEY_PIN_PASS = "pass";
    public static final String KEY_THEMES = "themes";
    public static final String KEY_NOTICE = "notice";
    public static final String KEY_RECYCLE = "recycle";
    public static final String KEY_PLAYER = "player";
    public static final String KEY_LIBRARY = "library";
    public static final String KEY_AUTO_DELETE = "auto_delete";
    public static final String KEY_AUTO_DELETE_SHORT = "auto_delete_short";
    public static final String KEY_AUTO_DELETE_SHORT_POSITION = "auto_delete_short_position";
    public static final String KEY_OUT_DELAY = "out_delay";
    public static final String KEY_OUT_DELAY_POSITION = "out_delay_position";
    public static final String KEY_IN_DELAY = "in_delay";
    public static final String KEY_IN_DELAY_POSITION = "in_delay_position";
    public static final String KEY_START_AUTO = "start_auto";
    public static final String KEY_START_AUTO_POSITION = "start_auto_position";
    public static final String KEY_OUT_GOING = "out_going";
    public static final String KEY_AUDIO_SOURCE = "audio_source";
    private static MySharedPreferences mInstance = null;
    private Context mContext;

    private MySharedPreferences(Context context) {
        mContext = context;
    }

    public static MySharedPreferences getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySharedPreferences(context.getApplicationContext());
        }
        return mInstance;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(key, defValue);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(key, defValue);
    }
}
