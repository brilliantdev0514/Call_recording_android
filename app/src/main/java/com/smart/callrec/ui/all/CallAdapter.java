package com.smart.callrec.ui.all;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smart.callrec.entity.Recording;
import com.smart.callrec.recycle.adapter.BaseAdapter;

/**
 * Created by Harry_Hai on 2/16/2018.
 */

public class CallAdapter extends BaseAdapter<Object> {
    public final static int ITEM_TYPE_STATE = 0;
    public final static int ITEM_TYPE = 1;
    OnRecyclerItemClick<Recording> onRecyclerItemClick;

    public CallAdapter(FragmentActivity activity) {
        super(activity, false);
    }

    @Override
    public int getActualItemViewType(int position) {
        if (dataSet.get(position) instanceof TextViewItem) {
            return ITEM_TYPE_STATE;
        }
        return ITEM_TYPE;
    }

    @Override
    protected void onActualBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object object = dataSet.get(position);
        if (object instanceof TextViewItem) {
            handleStateItem((StateViewHolder) holder, object, position);
        } else {
            handleItemView((CallItemViewHolder) holder, object, position);
        }
    }

    private void handleItemView(CallItemViewHolder holder, Object object, int position) {
        holder.fillData((Recording) object, position);
        holder.setOnRecyclerItemClick(onRecyclerItemClick);
    }

    protected void handleStateItem(StateViewHolder holder, Object object, int position) {
        holder.fillData((TextViewItem) object, position);
    }


    @Override
    protected RecyclerView.ViewHolder onActualCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case ITEM_TYPE_STATE:
                itemView = LayoutInflater.from(context).inflate(StateViewHolder.LAYOUT_ID, parent,
                        false);
                return new StateViewHolder(itemView);
            case ITEM_TYPE:
                itemView = LayoutInflater.from(context).inflate(CallItemViewHolder.LAYOUT_ID, parent,
                        false);
                return new CallItemViewHolder(itemView);
        }
        return null;
    }

    public void setOnRecyclerItemClick(OnRecyclerItemClick<Recording> onRecyclerViewItemClick) {
        this.onRecyclerItemClick = onRecyclerViewItemClick;
    }
}
