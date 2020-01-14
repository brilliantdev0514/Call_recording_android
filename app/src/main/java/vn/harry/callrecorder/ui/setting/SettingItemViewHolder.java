package vn.harry.callrecorder.ui.setting;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import vn.harry.callrecorder.R;
import vn.harry.callrecorder.entity.DrawerBean;
import vn.harry.callrecorder.recycle.viewholder.BaseViewHolder;
import vn.harry.callrecorder.util.ImageUtil;
import vn.harry.callrecorder.util.StringUtils;

/**
 * Created by Harry_Hai on 2/14/2018.
 */

public class SettingItemViewHolder extends BaseViewHolder<DrawerBean> {
    public static final int LAYOUT_ID = R.layout.item_setting;
    @BindView(R.id.txtTitle)
    TextView tvTitle;
    @BindView(R.id.ivSetting)
    ImageView imgIcon;

    public SettingItemViewHolder(View itemView) {
        super(itemView);
    }

    public void fillData(DrawerBean item, int position) {
        super.fillData(item, position);
        ImageUtil.setImageView(itemView.getContext(), item.getResId(), imgIcon);
        StringUtils.setText(item.getTitle(), tvTitle);
    }
}
