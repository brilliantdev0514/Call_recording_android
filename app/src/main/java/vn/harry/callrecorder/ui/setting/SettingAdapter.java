package vn.harry.callrecorder.ui.setting;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.harry.callrecorder.entity.DrawerBean;
import vn.harry.callrecorder.recycle.adapter.BaseAdapter;

/**
 * Created by Harry_Hai on 2/14/2018.
 */

public class SettingAdapter extends BaseAdapter<DrawerBean> {
    public SettingAdapter(FragmentActivity activity) {
        super(activity, false);
    }

    @Override
    public int getActualItemViewType(int position) {
        return 0;
    }

    @Override
    protected void onActualBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        handleItemView((SettingItemViewHolder) holder, dataSet.get(position), position);
    }

    private void handleItemView(SettingItemViewHolder holder, DrawerBean item, int position) {
        holder.fillData(item, position);
        holder.setOnRecyclerViewItemClick(onRecyclerViewItemClick);
    }

    @Override
    protected RecyclerView.ViewHolder onActualCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(SettingItemViewHolder.LAYOUT_ID, parent, false);
        return new SettingItemViewHolder(v);
    }
}
