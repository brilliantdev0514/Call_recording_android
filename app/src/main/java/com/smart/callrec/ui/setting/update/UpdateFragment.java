package com.smart.callrec.ui.setting.update;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import com.smart.callrec.App;
import com.smart.callrec.R;
import com.smart.callrec.ui.BaseFragment;
import com.smart.callrec.util.MySharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends BaseFragment {
    @BindView(R.id.ivBack)
    protected ImageView ivBack;
    @BindView(R.id.txtScreen)
    protected TextView txtScreen;
    @BindView(R.id.switchGA)
    protected Switch mSwitchGa;

    public UpdateFragment() {
    }

    public static UpdateFragment newInstance() {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_update;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView(getString(R.string.screen_update), txtScreen, ivBack);
        Activity activity = getActivity();
        mSwitchGa.setChecked(MySharedPreferences.getInstance(getActivity()).getBoolean(MySharedPreferences.KEY_GA, false));
        mSwitchGa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Tracker t = ((App) activity.getApplication()).getDefaultTracker();
//                t.enableAutoActivityTracking(isChecked);
//                MySharedPreferences.getInstance(getActivity()).putBoolean(MySharedPreferences.KEY_GA, isChecked);
            }
        });
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void initPresenter() {

    }

}
