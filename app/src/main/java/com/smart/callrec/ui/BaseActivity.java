package com.smart.callrec.ui;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;
import com.smart.callrec.R;
import com.smart.callrec.util.MySharedPreferences;

public abstract class BaseActivity extends AppCompatActivity {
    protected int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme = MySharedPreferences.getInstance(this).getInt(MySharedPreferences.KEY_THEMES,  R.style.AppTheme_Red);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
        initData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkPerms();
    }

    public void checkPerms() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    abstract protected int getLayoutId();

    abstract protected void initView();

    abstract protected void initData();

    public void replaceFragment(int container, Fragment fragment) {
        // This method have same logic in BaseFragment
        // please update if you change
        replaceFragment(container, fragment, null);
    }

    public void replaceFragment(int container, Fragment fragment, String tag) {
        // This method have same logic in BaseFragment
        // please update if you change
        FragmentManager manager = getSupportFragmentManager();
        if (tag != null) {
            Fragment f = manager.findFragmentByTag(tag);
            if (f != null) {
                manager.popBackStack();
            }
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * This method replace fragment and clear all back stack
     *
     * @param container fragment container
     * @param fragment  new fragment
     */
    public void replaceAndClearFragment(int container, Fragment fragment) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // clear action
        replaceFragmentforMain(container, fragment);
    }

    public void replaceFragmentforMain(int container, Fragment fragment) {
        replaceFragmentforMain(container, fragment, null);
    }

    public void replaceFragmentforMain(int container, Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        if (tag != null) {
            Fragment f = manager.findFragmentByTag(tag);
            if (f != null) {
                manager.popBackStack();
            }
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        int backStackCnt = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCnt > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void popEntireFragmentBackStack() {
        final FragmentManager fm = getSupportFragmentManager();
        final int backStackCount = fm.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            fm.popBackStack();
        }
    }

    public void popBackStack() {
        final FragmentManager fm = getSupportFragmentManager();
        final int backStackCount = fm.getBackStackEntryCount();
        if (backStackCount > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }
}
