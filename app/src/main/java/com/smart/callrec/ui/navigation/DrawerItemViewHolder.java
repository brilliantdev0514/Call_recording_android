package com.smart.callrec.ui.navigation;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.smart.callrec.R;
import com.smart.callrec.entity.DrawerBean;
import com.smart.callrec.recycle.viewholder.BaseViewHolder;
import com.smart.callrec.util.ImageUtil;
import com.smart.callrec.util.StringUtils;

public class DrawerItemViewHolder extends BaseViewHolder<DrawerBean> {

    public static final int LAYOUT_ID = R.layout.item_navigation_drawer;
    @BindView(R.id.txtTitle)
    TextView tvTitle;
    @BindView(R.id.ivNavi)
    ImageView imgIcon;
    @BindView(R.id.root)
    RelativeLayout mRoot;

    public DrawerItemViewHolder(View itemView) {
        super(itemView);
    }

    public void fillData(DrawerBean item, int prositonSeleced, int position) {
        super.fillData(item, position);
        ImageUtil.setImageView(itemView.getContext(), item.getResId(), imgIcon);
        StringUtils.setText(item.getTitle(), tvTitle);
        if (position == prositonSeleced) {
            mRoot.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorGray79));
        } else {
            mRoot.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorWhite));
        }
    }
}
