package com.smart.callrec.ui.setting.cloud;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.smart.callrec.R;
import com.smart.callrec.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CloudFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CloudFragment extends BaseFragment {
    @BindView(R.id.ivBack)
    protected ImageView ivBack;
    @BindView(R.id.txtScreen)
    protected TextView txtScreen;

    public CloudFragment() {
        // Required empty public constructor
    }

    public static CloudFragment newInstance() {
        CloudFragment fragment = new CloudFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        setUpToolBarView(getString(R.string.setting_cloud), txtScreen, ivBack);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cloud;
    }

    @Override
    protected void initView(View root) {

    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void initPresenter() {

    }
}
