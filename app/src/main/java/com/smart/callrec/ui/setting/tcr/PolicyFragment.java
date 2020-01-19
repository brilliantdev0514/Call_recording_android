package com.smart.callrec.ui.setting.tcr;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import com.smart.callrec.R;
import com.smart.callrec.ui.BaseFragment;

/**
 * Created by thanh the on 2/24/2018.
 */

public class PolicyFragment extends BaseFragment {
    public static final String TAG = "PolicyFragment";
    @BindView(R.id.ivBack)
    protected ImageView ivBack;
    @BindView(R.id.txtScreen)
    protected TextView txtScreen;
    @BindView(R.id.wvPolicy)
    protected WebView mWebView;

    public static PolicyFragment newInstance() {
        Bundle args = new Bundle();
        PolicyFragment fragment = new PolicyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        startWebView(mWebView, "https://nllapps.com");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_policy;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView("https://nllapps.com", txtScreen, ivBack);
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void initPresenter() {

    }

}
