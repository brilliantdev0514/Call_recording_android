package com.smart.callrec.ui.navigation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import com.smart.callrec.R;
import com.smart.callrec.entity.DrawerBean;
import com.smart.callrec.recycle.viewholder.BaseViewHolder;
import com.smart.callrec.ui.MainActivity;
import com.smart.callrec.util.ImageUtil;
import com.smart.callrec.util.MySharedPreferences;
import com.smart.callrec.util.StringUtils;
import com.smart.callrec.util.UserPreferences;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DrawerHeaderViewHolder extends BaseViewHolder<DrawerBean> {

    public static final int LAYOUT_ID = R.layout.item_navigation_drawer_header;
    @BindView(R.id.switchOnOff)
    Switch switchOnOff;
    @BindView(R.id.txtStatus)
    TextView txtStatus;
    @BindView(R.id.imageView)
    ImageView imageView;
    private Notification notification;
    private NotificationManager notificationManager;
    private int theme;

    DrawerHeaderViewHolder(View itemView) {
        super(itemView);
        boolean isChecked = UserPreferences.getEnabled();
        switchOnOff.setChecked(isChecked);
        onCheckSwitch(isChecked, itemView.getContext());
        switchOnOff.setOnCheckedChangeListener((buttonView, isCheck) -> {
            if (MySharedPreferences.getInstance(itemView.getContext()).getInt(MySharedPreferences.KEY_START_AUTO_POSITION, 1) == 0) {
                Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.record_auto),
                        Toast.LENGTH_SHORT).show();
                switchOnOff.setChecked(true);
                return;
            }
            onCheckSwitch(isCheck, itemView.getContext());
            UserPreferences.setEnabled(isCheck);
            int toastText = !isCheck ? R.string.record_disabled :
                    R.string.record_enabled;
            Toast.makeText(itemView.getContext(), itemView.getContext().getString(toastText),
                    Toast.LENGTH_SHORT).show();
            if (MySharedPreferences.getInstance(itemView.getContext()).getBoolean(MySharedPreferences.KEY_NOTICE, true))
                showNotification(isCheck);
        });
        onChangeTheme();
    }

    private void onCheckSwitch(boolean isCheck, Context context) {
        if (isCheck) {
            StringUtils.setText(context.getString(R.string.enable), txtStatus);
        } else {
            StringUtils.setText(context.getString(R.string.disable), txtStatus);
        }
    }

    public void fillData(DrawerBean item, int position) {
        super.fillData(item, position);
    }

    private void showNotification(boolean isCheck) {

        Intent intent = new Intent(itemView.getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                itemView.getContext(), 0, intent, 0);
        Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.ic_app);
        notification = new NotificationCompat.Builder(
                itemView.getContext())
                .setContentTitle(itemView.getContext().getString(R.string.app_name))
                .setTicker("fgfgf")
                .setContentText(itemView.getContext().getString(R.string.record_enabled))
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(notificationLargeIconBitmap)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        notificationManager = (NotificationManager) itemView.getContext().getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        if (!isCheck) {
            notificationManager.cancel(1213);
        } else {
            notificationManager.notify(1213, notification);
        }
    }

    private void onChangeTheme() {
        theme = MySharedPreferences.getInstance(itemView.getContext()).getInt(MySharedPreferences.KEY_THEMES, R.style.AppTheme_Red);
        switch (theme) {
            case R.style.AppTheme_Red:
                ImageUtil.setImageView(itemView.getContext(), R.drawable.ic_app_red, imageView);
                break;
            case R.style.AppTheme_Green:
                ImageUtil.setImageView(itemView.getContext(), R.drawable.ic_app_green, imageView);
                break;
            case R.style.AppTheme_Blue:
                ImageUtil.setImageView(itemView.getContext(), R.drawable.ic_app_blue, imageView);
                break;
            case R.style.AppTheme_Orange:
                ImageUtil.setImageView(itemView.getContext(), R.drawable.ic_app_orange, imageView);
                break;
            default:
                break;
        }
    }
}
