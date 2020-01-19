package com.smart.callrec;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.StrictMode;
import android.util.DisplayMetrics;

import com.activeandroid.ActiveAndroid;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.Locale;

import com.smart.callrec.util.MySharedPreferences;
import com.smart.callrec.util.MySharedPreferences;
import com.smart.callrec.util.UserPreferences;

/**
 * Created by Harry_Hai on 2/18/2018.
 */

public class App extends Application {
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    public static boolean isOutComming = false;

    @Override
    public void onCreate() {
        super.onCreate();
        UserPreferences.init(this);
        ActiveAndroid.initialize(this);
        //sAnalytics =// GoogleAnalytics.getInstance(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        setDefaultLangugae();
    }

    private void setDefaultLangugae(){
        String[] availableLocales = {"en","ja"};
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        String localString = MySharedPreferences.getInstance(getApplicationContext()).getString(MySharedPreferences.KEY_LANGUAGE, availableLocales[0]);
        if (localString.contains("-")) {
            conf.locale = new Locale(localString.substring(0,
                    localString.indexOf("-")), localString.substring(
                    localString.indexOf("-"), localString.length()));
        } else {
            conf.locale = new Locale(localString);
        }
        res.updateConfiguration(conf, dm);
    }

    synchronized public Tracker getDefaultTracker() {
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }
        sTracker.enableAutoActivityTracking(true);
        return sTracker;
    }
}
