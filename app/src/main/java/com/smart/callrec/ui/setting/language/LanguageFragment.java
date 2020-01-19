package com.smart.callrec.ui.setting.language;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import com.smart.callrec.R;
import com.smart.callrec.ui.BaseFragment;
import com.smart.callrec.util.MySharedPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LanguageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LanguageFragment extends BaseFragment {
    @BindView(R.id.ivBack)
    protected ImageView ivBack;
    @BindView(R.id.txtScreen)
    protected TextView txtScreen;
    @BindView(R.id.txtDescriptionLanguage)
    protected TextView mTvDescriptionLanguage;
    @BindView(R.id.laguage)
    protected LinearLayout mLanguageLayout;
    @BindView(R.id.translation)
    protected LinearLayout mTranslationLayout;
    @BindView(R.id.translator)
    protected LinearLayout mTranslatorLayout;

    public LanguageFragment() {
    }

    public static LanguageFragment newInstance() {
        LanguageFragment fragment = new LanguageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mTvDescriptionLanguage.setText("" + MySharedPreferences.getInstance(getActivity()).getString(MySharedPreferences.KEY_LANGUAGE,  Locale.getDefault().getLanguage()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_language;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView(getString(R.string.screen_language), txtScreen, ivBack);
        mLanguageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog();
            }
        });
        mTranslationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendMail = new Intent(Intent.ACTION_SENDTO);
                intentSendMail.setType("text/plain");
                intentSendMail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_language));
                intentSendMail.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body_language));
                intentSendMail.setData(Uri.parse("mailto:edwin.netherland@gmail.com"));
                intentSendMail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentSendMail);
            }
        });
        mTranslatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTranslatorDialog();
            }
        });
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void initPresenter() {

    }

    private void showTranslatorDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle(getString(R.string.language_translators));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        arrayAdapter.add("Abraham Lincoln (USA)");
        arrayAdapter.add("Mother Teresa (JP)");
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builderSingle.setNegativeButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void showLanguageDialog() {
        LanguagesDialog languagesDialog = new LanguagesDialog();
        languagesDialog.show(getActivity().getFragmentManager(), "LanguagesDialogFragment");
    }
}
