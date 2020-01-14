package vn.harry.callrecorder.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import vn.harry.callrecorder.R;
import vn.harry.callrecorder.ui.MainActivity;
import vn.harry.callrecorder.util.Constants;
import vn.harry.callrecorder.util.MySharedPreferences;

/**
 * Created by hainm on 3/1/2018.
 */


public class SplashActivity extends AwesomeSplash {
    private int theme;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void initSplash(ConfigSplash configSplash) {
        theme = MySharedPreferences.getInstance(this).getInt(MySharedPreferences.KEY_THEMES, R.style.AppTheme_Red);
        switch (theme) {
            case R.style.AppTheme_Red:
                configSplash.setBackgroundColor(R.color.material_red_500);
                break;
            case R.style.AppTheme_Green:
                configSplash.setBackgroundColor(R.color.material_green_500);
                break;
            case R.style.AppTheme_Blue:
                configSplash.setBackgroundColor(R.color.material_blue_500);
                break;
            case R.style.AppTheme_Orange:
                configSplash.setBackgroundColor(R.color.material_orange_500);
                break;
            default:
                break;
        }
        configSplash.setAnimCircularRevealDuration(1000);
        configSplash.setPathSplash(Constants.DROID_LOGO);
        configSplash.setOriginalHeight(400);
        configSplash.setOriginalWidth(300);
        configSplash.setAnimPathStrokeDrawingDuration(1000);
        configSplash.setPathSplashStrokeSize(5);
//        configSplash.setPathSplashStrokeColor(R.color.material_blue_700);
        configSplash.setAnimPathFillingDuration(1500);
//        configSplash.setPathSplashFillColor(R.color.colorWhite);

        //Customize Title
        configSplash.setTitleSplash("Call Recorder");
        configSplash.setTitleTextColor(R.color.colorWhite);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(1500);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("fonts/streatwear.otf"); //provide string to your font located in assets/fonts/
    }

    @Override
    public void animationsFinished() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}