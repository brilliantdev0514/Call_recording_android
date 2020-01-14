package vn.harry.callrecorder.ui.navigation;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.harry.callrecorder.entity.DrawerBean;
import vn.harry.callrecorder.recycle.adapter.BaseAdapter;
import vn.harry.callrecorder.util.ValueConstants;

public class NavigationDrawerAdapter extends BaseAdapter<DrawerBean> {

    private int mCurrentSelectedPosition;

    public NavigationDrawerAdapter(FragmentActivity activity, int currentSelectedPosition) {
        super(activity, false);
        this.mCurrentSelectedPosition = currentSelectedPosition;
    }

    @Override
    public int getActualItemViewType(int position) {
        if (position == 0) {
            return ValueConstants.NavigationItemType.Header;
        } else {
            return ValueConstants.NavigationItemType.Item;
        }
    }

    @Override
    protected void onActualBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ValueConstants.NavigationItemType.Header:
                handleHeaderView((DrawerHeaderViewHolder) holder, dataSet.get(position), position);
                break;
            case ValueConstants.NavigationItemType.Item:
                handleItemView((DrawerItemViewHolder) holder, dataSet.get(position), position);
                break;
            default:
        }
    }

    private void handleItemView(DrawerItemViewHolder holder, DrawerBean item, int position) {
        holder.fillData(item, mCurrentSelectedPosition, position);
        holder.setOnRecyclerViewItemClick(onRecyclerViewItemClick);
    }

    private void handleHeaderView(DrawerHeaderViewHolder holder, DrawerBean item, int position) {
        holder.fillData(item, position);
    }

    @Override
    protected RecyclerView.ViewHolder onActualCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == ValueConstants.NavigationItemType.Header) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(DrawerHeaderViewHolder.LAYOUT_ID, parent, false);
            return new DrawerHeaderViewHolder(v);
        } else if (viewType == ValueConstants.NavigationItemType.Item) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(DrawerItemViewHolder.LAYOUT_ID, parent, false);
            return new DrawerItemViewHolder(v);
        } else {
            return null;
        }
    }

    public void setCurrentSelectedPosition(int currentSelectedPosition) {
        this.mCurrentSelectedPosition = currentSelectedPosition;
    }
}
