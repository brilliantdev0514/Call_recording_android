package vn.harry.callrecorder.ui.setting.recorder;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import vn.harry.callrecorder.R;
import vn.harry.callrecorder.ui.BaseFragment;
import vn.harry.callrecorder.util.MySharedPreferences;
import vn.harry.callrecorder.util.UserPreferences;

public class RecordingFragment extends BaseFragment {

    @BindView(R.id.ivBack)
    protected ImageView ivBack;
    @BindView(R.id.txtScreen)
    protected TextView txtScreen;
    @BindView(R.id.switchAutoDelete)
    protected Switch mSwitchAutoDelete;
    @BindView(R.id.titleStartAuto)
    protected TextView titleStartAuto;
    @BindView(R.id.layoutStartAuto)
    protected RelativeLayout layoutStartAuto;
    @BindView(R.id.layoutAudioSource)
    protected RelativeLayout layoutAudioSource;
    @BindView(R.id.titleAudioSource)
    protected TextView titleAudioSource;
    @BindView(R.id.layoutAudioFormat)
    protected RelativeLayout layoutAudioFormat;
    @BindView(R.id.titleAudioFormat)
    protected TextView titleAudioFormat;
    private int inDelay = 0;
    String[] availableLocales;
    public RecordingFragment() {
    }

    public static RecordingFragment newInstance() {
        RecordingFragment fragment = new RecordingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData() {
        setUpToolBarView(getString(R.string.screen_recoder), txtScreen, ivBack);
        mSwitchAutoDelete.setChecked(MySharedPreferences.getInstance(getActivity()).getString(MySharedPreferences.KEY_AUTO_DELETE, "").isEmpty() ? false : true);
        mSwitchAutoDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showAutoDeleteDialog();
                } else {
                    MySharedPreferences.getInstance(getActivity()).putString(MySharedPreferences.KEY_AUTO_DELETE, "");
                }
            }
        });
        layoutStartAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartAutoDialog();
            }
        });

        availableLocales = getResources().getStringArray(R.array.source_text);
        titleAudioSource.setText(getString(R.string.recording_audio) + "(" + availableLocales[MySharedPreferences.getInstance(getActivity()).getInt(MySharedPreferences.KEY_AUDIO_SOURCE, 0)] + ")");
        layoutAudioSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAudioSourceDialog();
            }
        });
        layoutAudioFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwitchAutoDelete.setChecked(MySharedPreferences.getInstance(getActivity()).getString(MySharedPreferences.KEY_AUTO_DELETE, "").isEmpty() ? false : true);
        inDelay = MySharedPreferences.getInstance(getActivity()).getInt(MySharedPreferences.KEY_START_AUTO_POSITION, 1);
        String temp = inDelay == 0 ? getString(R.string.auto) : getString(R.string.manual);
        String data = getString(R.string.recording_start_recording) + " (" + temp + ")";
        titleStartAuto.setText(data);
    }

    private void showAudioSourceDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle(getString(R.string.recording_audio));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice);
        for (String i: availableLocales){
            arrayAdapter.add(i);
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                titleAudioSource.setText(getString(R.string.recording_audio) + "(" + arrayAdapter.getItem(which) + ")");
                MySharedPreferences.getInstance(getActivity()).putInt(MySharedPreferences.KEY_AUDIO_SOURCE, which);
                dialog.dismiss();
            }
        });
        builderSingle.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builderSingle.create();
        alertDialog.show();
        ListView listView = alertDialog.getListView();
        if (listView != null) {
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setItemChecked(MySharedPreferences.getInstance(getActivity()).getInt(MySharedPreferences.KEY_AUDIO_SOURCE, 0), true);
        }
    }

    private void showStartAutoDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle(getString(R.string.recording_start_recording));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice);
        arrayAdapter.add(getString(R.string.auto));
        arrayAdapter.add(getString(R.string.manual));
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    UserPreferences.setEnabled(true);
                }
                titleStartAuto.setText(getString(R.string.recording_start_recording) + "(" + arrayAdapter.getItem(which) + ")");
                MySharedPreferences.getInstance(getActivity()).putInt(MySharedPreferences.KEY_START_AUTO_POSITION, which);
                dialog.dismiss();
            }
        });
        builderSingle.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builderSingle.create();
        alertDialog.show();
        ListView listView = alertDialog.getListView();
        if (listView != null) {
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setItemChecked(MySharedPreferences.getInstance(getActivity()).getInt(MySharedPreferences.KEY_START_AUTO_POSITION, 1), true);
        }
    }

    private void showAutoDeleteDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.number_day));
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertDialog.setView(input);
        alertDialog.setPositiveButton(getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String pin = input.getText().toString();
                        if (TextUtils.isEmpty(pin)) {
                            mSwitchAutoDelete.setChecked(false);
                            MySharedPreferences.getInstance(getActivity()).putString(MySharedPreferences.KEY_AUTO_DELETE, "");
                        } else {
                            mSwitchAutoDelete.setChecked(true);
                            MySharedPreferences.getInstance(getActivity()).putString(MySharedPreferences.KEY_AUTO_DELETE, pin);
                        }
                    }
                });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mSwitchAutoDelete.setChecked(false);
                MySharedPreferences.getInstance(getActivity()).putString(MySharedPreferences.KEY_AUTO_DELETE, "");
            }
        });

        alertDialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recording;
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
