package com.smart.callrec.recycle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.smart.callrec.recycle.viewholder.DefaultLoadMoreViewHolder;

public abstract class LoadMoreAdapterWithObject<D> extends RecyclerView.Adapter {
    private final Context context;
    public int loadMoreViewType;
    public boolean isLoadMoreFromTop;
    protected int state = NormalAdapter.STATE_NORMAL;
    private boolean hasCustomLoadMoreHolder;

    public LoadMoreAdapterWithObject(Context context, boolean hasCustomLoadMoreHolder) {
        super();
        this.context = context;
        this.hasCustomLoadMoreHolder = hasCustomLoadMoreHolder;

    }

    /**
     * For Create Adapter that load more always in Top of List
     *
     * @param context
     * @param hasCustomLoadMoreHolder
     * @param isLoadMoreFromTop
     */
    public LoadMoreAdapterWithObject(Context context, boolean hasCustomLoadMoreHolder, boolean isLoadMoreFromTop) {
        this(context, hasCustomLoadMoreHolder);
        this.isLoadMoreFromTop = isLoadMoreFromTop;
    }

    public int getDefaultItemCount() {
        return 0;
    }

    public abstract boolean hasMoreData();

    @Override
    public int getItemCount() {
        return getDefaultItemCount() + (hasMoreData() ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMoreFromTop && hasMoreData() && position == 0 && state == NormalAdapter.STATE_NORMAL) {
            return loadMoreViewType;
        }

        if (!isLoadMoreFromTop && hasMoreData() && position == getDefaultItemCount() && state == NormalAdapter.STATE_NORMAL) {
            return loadMoreViewType;
        }


        int viewType = getDefaultItemViewType(position);

        if (viewType < 0) {
            throw new RuntimeException("viewType must be > 0");
        }

        if (isLoadMoreFromTop && hasMoreData()) {
            if (viewType <= loadMoreViewType) {
                loadMoreViewType = viewType - 1;
            }
        } else {
            if (viewType >= loadMoreViewType) {
                loadMoreViewType = viewType + 1;
            }
        }

        return viewType;
    }

    public int getState() {
        return state;
    }

    /**
     * @param position position in list
     * @return integer >= 0
     */
    protected int getDefaultItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Check Default View Holder Null
        if (onDefaultCreateViewHolder(parent, viewType) == null) {
            Log.e("", "onDefaultCreateViewHolder null");
        }

        // If view type is load more -> get Load more View Holder
        // If view type is view holder -> get default View Holder
        return viewType == loadMoreViewType ? getLoadMoreViewHolder(parent) : onDefaultCreateViewHolder(parent, viewType);
    }

    // Should Override
    protected RecyclerView.ViewHolder onDefaultCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * Get Load More View Holder
     *
     * @param parent
     * @return
     */

    protected RecyclerView.ViewHolder getLoadMoreViewHolder(ViewGroup parent) {
        if (!hasCustomLoadMoreHolder) {
            //Use default holder
            final View view = LayoutInflater.from(context).inflate(DefaultLoadMoreViewHolder.LAYOUT_ID, parent, false);
            return new DefaultLoadMoreViewHolder(view);
        } else {
            //Use custom view holder
            return onCreateLoadMoreViewHolder(parent);
        }
    }

    /**
     * Binding ViewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (hasCustomLoadMoreHolder) {
            if (loadMoreViewType == getItemViewType(position)) {
                onBindLoadMoreViewHolder(holder, position);
            } else {
                onDefaultBindViewHolder(holder, position);
            }
        } else {
            if (holder instanceof DefaultLoadMoreViewHolder) {
                holder.itemView.setVisibility(hasMoreData() ? View.VISIBLE : View.GONE);
            } else {
                onDefaultBindViewHolder(holder, position);
            }
        }
    }


    //TODO: Need Override
    protected void onDefaultBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    //TODO: Need Override
    protected RecyclerView.ViewHolder onCreateLoadMoreViewHolder(ViewGroup parent) {
        return null;
    }

    //TODO: Need Override
    protected void onBindLoadMoreViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    //TODO: Need Override
    public abstract void loadMore(List<D> moreList);

}
