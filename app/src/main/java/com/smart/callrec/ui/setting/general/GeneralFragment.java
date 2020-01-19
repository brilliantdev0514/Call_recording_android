package com.smart.callrec.ui.setting.general;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import com.smart.callrec.R;
import com.smart.callrec.ui.BaseFragment;
import com.smart.callrec.ui.MainActivity;
import com.smart.callrec.util.MySharedPreferences;
import com.smart.callrec.util.UserPreferences;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralFragment extends BaseFragment {
    @BindView(R.id.ivBack)
    protected ImageView ivBack;
    @BindView(R.id.txtScreen)
    protected TextView txtScreen;
    @BindView(R.id.switchPin)
    protected Switch mSwitchPin;
    @BindView(R.id.switchNotice)
    protected Switch mSwitchNotice;
    @BindView(R.id.switchRecycle)
    protected Switch mSwitchRecycle;
    private Notification notification;
    private NotificationManager notificationManager;

    public GeneralFragment() {
        // Required empty public constructor
    }

    public static GeneralFragment newInstance() {
        GeneralFragment fragment = new GeneralFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        mSwitchPin.setChecked(MySharedPreferences.getInstance(getActivity()).getBoolean(MySharedPreferences.KEY_PIN, false));
        mSwitchPin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MySharedPreferences.getInstance(getActivity()).putBoolean(MySharedPreferences.KEY_PIN, isChecked);
                if (isChecked) {
                    showInputPinDialog();
                }
            }
        });
        mSwitchNotice.setChecked(MySharedPreferences.getInstance(getActivity()).getBoolean(MySharedPreferences.KEY_NOTICE, true));
        mSwitchNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MySharedPreferences.getInstance(getActivity()).putBoolean(MySharedPreferences.KEY_NOTICE, isChecked);
                if (!isChecked) {
                    NotificationManager notifManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    notifManager.cancelAll();
                } else {
                    if (UserPreferences.getEnabled())
                        showNotification();
                }
            }
        });
        mSwitchRecycle.setChecked(MySharedPreferences.getInstance(getActivity()).getBoolean(MySharedPreferences.KEY_RECYCLE, false));
        mSwitchRecycle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MySharedPreferences.getInstance(getActivity()).putBoolean(MySharedPreferences.KEY_RECYCLE, isChecked);
            }
        });
    }

    private void showNotification() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getActivity(), 0, intent, 0);
        Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_app);
        notification = new NotificationCompat.Builder(
                getActivity())
                .setContentTitle(getActivity().getString(R.string.app_name))
                .setTicker("fgfgf")
                .setContentText(getActivity().getString(R.string.record_enabled))
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(notificationLargeIconBitmap)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(1213, notification);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_general;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView(getString(R.string.setting_general), txtScreen, ivBack);

    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void initPresenter() {

    }

    private void showInputPinDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.pin));
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertDialog.setView(input);
        alertDialog.setPositiveButton(getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String pin = input.getText().toString();
                        if (TextUtils.isEmpty(pin) || pin.length() < 4) {
                            mSwitchPin.setChecked(false);
                            Toast.makeText(getActivity(), getString(R.string.pin_error), Toast.LENGTH_SHORT).show();
                        } else {
                            mSwitchPin.setChecked(true);
                            MySharedPreferences.getInstance(getActivity()).putString(MySharedPreferences.KEY_PIN_PASS, pin.trim());
                        }
                    }
                });
        alertDialog.setNegativeButton(getString(R.string.dialog_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mSwitchPin.setChecked(false);
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
