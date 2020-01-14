package vn.harry.callrecorder.ui.all;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import vn.harry.callrecorder.R;
import vn.harry.callrecorder.entity.Recording;
import vn.harry.callrecorder.recycle.viewholder.BaseViewHolder;
import vn.harry.callrecorder.util.ImageUtil;
import vn.harry.callrecorder.util.MySharedPreferences;
import vn.harry.callrecorder.util.StringUtils;

/**
 * Created by Harry_Hai on 2/17/2018.
 */

public class CallItemViewHolder extends BaseViewHolder<Recording> {
    public static final int LAYOUT_ID = R.layout.item_call;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

    @BindView(R.id.nameContact)
    TextView nameContact;
    @BindView(R.id.phoneContact)
    TextView phoneContact;
    @BindView(R.id.timeCall)
    TextView timeCall;
    @BindView(R.id.duration)
    TextView txtDuration;
    @BindView(R.id.size)
    TextView size;
    @BindView(R.id.ivCall)
    ImageView ivCall;
    @BindView(R.id.ivNote)
    ImageView ivNote;
    @BindView(R.id.ivContact)
    ImageView ivContact;

    OnRecyclerItemClick<Recording> onRecyclerItemClick;
    private int theme;

    public CallItemViewHolder(View itemView) {
        super(itemView);
    }

    public void fillData(Recording item, int position) {
        super.fillData(item, position);
        theme = MySharedPreferences.getInstance(itemView.getContext()).getInt(MySharedPreferences.KEY_THEMES, R.style.AppTheme_Red);
        onChangeTheme();
        StringUtils.setText(item.getUserName(), nameContact);
        if (item.getNote() == null) {
            ivNote.setVisibility(View.GONE);
            StringUtils.setText(item.getPhoneNumber(), phoneContact);
        } else {
            ivNote.setVisibility(View.VISIBLE);
            StringUtils.setText(item.getNote(), phoneContact);
        }
        try {
            // get id because get date is wrong.i don't know why
            StringUtils.setText(String.valueOf(DateFormat.format("hh:mm",
                    formatter.parse(item.getIdCall()))), timeCall);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (item.getUri() != null) {
            Uri uri = Uri.parse(item.getUri());
            onHandleDuration(uri);
            onGetSize(uri);
        }
        if (item.isOutGoing()) {
            ImageUtil.setImageView(itemView.getContext(), R.drawable.ic_out_going, ivCall);
        } else {
            ImageUtil.setImageView(itemView.getContext(), R.drawable.ic_in_coming, ivCall);
        }
    }

    private void onChangeTheme() {
        switch (theme) {
            case R.style.AppTheme_Red:
                ImageUtil.setImageView(itemView.getContext(), R.drawable.ic_user_red, ivContact);
                break;
            case R.style.AppTheme_Green:
                ImageUtil.setImageView(itemView.getContext(), R.drawable.ic_user_green, ivContact);
                break;
            case R.style.AppTheme_Blue:
                ImageUtil.setImageView(itemView.getContext(), R.drawable.ic_user_blue, ivContact);
                break;
            case R.style.AppTheme_Orange:
                ImageUtil.setImageView(itemView.getContext(), R.drawable.ic_user_orange, ivContact);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        View transitionView;
        if (getTransitionView() == null) {
            transitionView = view;
        } else {
            transitionView = getTransitionView();
        }
        if (onRecyclerItemClick != null && item != null) {
            onRecyclerItemClick.onItemClick(transitionView, item, position);
        }
    }

    private void onGetSize(Uri uri) {
        try {
            File file = new File(uri.getPath());
            long length = file.length();
            length = length / 1024;
            StringUtils.setText(length + "KB", size);
        } catch (Exception e) {
        }
    }

    private void onHandleDuration(Uri uri) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri.getPath());
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInmillisec = Long.parseLong(time);
            long duration = timeInmillisec / 1000;
            long h = duration / 3600;
            long m = (duration - h * 3600) / 60;
            long s = duration - (h * 3600 + m * 60);
            StringBuilder result = new StringBuilder();
            if (h < 10) {
                result.append("0").append(h).append(":");
            } else {
                result.append(h).append(":");
            }
            if (m < 10) {
                result.append("0").append(m).append(":");
            } else {
                result.append(m).append(":");
            }
            if (s < 10) {
                result.append("0").append(s);
            } else {
                result.append(s);
            }
            StringUtils.setText(result.toString(), txtDuration);
        } catch (Exception ex) {
        }

    }

    public void setOnRecyclerItemClick(OnRecyclerItemClick<Recording> onRecyclerViewItemClick) {
        this.onRecyclerItemClick = onRecyclerViewItemClick;
    }
}
