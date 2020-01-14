package vn.harry.callrecorder.recycle.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.harry.callrecorder.R;
import vn.harry.callrecorder.recycle.viewholder.SimpleViewHolder;

public abstract class NormalAdapter<D> extends LoadMoreAdapterWithObject<D> {

    public static final int THRESHOLD = 200;
    //TODO: RecyclerView State
    public static final int STATE_NORMAL = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_ERROR = 3;
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_LOADING = 1000;
    public static final int TYPE_EMPTY = 1001;
    public static final int TYPE_ERROR = 1002;
    public static final int TYPE_HEADER = 1003;
    public static final int TYPE_FOOTER = 1004;
    public List<D> dataSet;
    //TODO: Header and Footer
    public List<Object> headers;
    public List<Object> footers;
    public boolean isLoadMoreFromTop;
    protected Context context;
    private View loadingView;
    private View emptyView;
    private View errorView;

    public NormalAdapter(Context context, boolean hasCustomLoadMoreHolder) {
        this(context, null, hasCustomLoadMoreHolder, null, null);
    }

    /**
     * Constructor has one type object
     *
     * @param context
     * @param initData
     * @param hasCustomLoadMoreHolder
     */
    public NormalAdapter(Context context, List<D> initData, boolean hasCustomLoadMoreHolder) {
        this(context, initData, hasCustomLoadMoreHolder, null, null);
    }

    /**
     * Constructor has header object, footer object and load more from Top of List
     *
     * @param context
     * @param initData
     * @param hasCustomLoadMoreHolder
     * @param headers
     * @param footers
     */
    public NormalAdapter(Context context, List<D> initData, boolean hasCustomLoadMoreHolder, List<Object> headers, List<Object> footers, boolean isLoadMoreFromTop) {
        super(context, hasCustomLoadMoreHolder, isLoadMoreFromTop);
        this.isLoadMoreFromTop = isLoadMoreFromTop;
        this.dataSet = new ArrayList<>();
        this.context = context;

        if (initData != null && initData.size() > 0) {
            loadInitialDataSet(initData);
        }

        if (headers == null) {
            this.headers = new ArrayList<>();
        } else {
            this.headers = headers;
        }

        if (footers == null) {
            this.footers = new ArrayList<>();
        } else {
            this.footers = footers;
        }
    }

    /**
     * Constructor have header object and footer object
     *
     * @param context
     * @param initData
     * @param hasCustomLoadMoreHolder
     * @param headers
     * @param footers
     */
    public NormalAdapter(Context context, List<D> initData, boolean hasCustomLoadMoreHolder, List<Object> headers, List<Object> footers) {
        super(context, hasCustomLoadMoreHolder);
        this.dataSet = new ArrayList<>();
        this.context = context;

        if (initData != null && initData.size() > 0) {
            loadInitialDataSet(initData);
        }

        if (headers == null) {
            this.headers = new ArrayList<>();
        } else {
            this.headers = headers;
        }

        if (footers == null) {
            this.footers = new ArrayList<>();
        } else {
            this.footers = footers;
        }
    }

    /**
     * Set data in first time.
     *
     * @param initialDataSet
     */
    public void loadInitialDataSet(final List<D> initialDataSet) {
        addMoreData(initialDataSet);
    }

    protected void loadMoreDataSet(final List<D> loadMoreDataSet, final DataSetLoadedCallback<D> callback) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                dataSet.addAll(loadMoreDataSet);
                callback.dataSetLoaded(dataSet);
            }
        }, 1000);
    }

    @Override
    public int getDefaultItemCount() {
        switch (state) {
            case STATE_LOADING:
            case STATE_EMPTY:
            case STATE_ERROR:
                return getHeaderCount() + 1;
        }
        return getActualItemCount() + getHeaderCount() + getFooterCount();
    }


    public int getActualItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    /**
     * Adding more data with delay
     *
     * @param moreList
     */
    @Override
    public void loadMore(final List<D> moreList) {

        if (moreList.size() == 0) {
            notifyDataSetChanged();
            return;
        }

        final int currentSize = dataSet.size();
        final long start = System.currentTimeMillis();
        if (isLoadMoreFromTop) {
            Collections.reverse(moreList);
            dataSet.addAll(0, moreList);
        } else {
            dataSet.addAll(moreList);
        }

        final int startPos = isLoadMoreFromTop ? 0 : currentSize;
        final int endPos = isLoadMoreFromTop ? moreList.size() : dataSet.size();

        if (dataSet.size() != currentSize) {
            if (System.currentTimeMillis() - start > THRESHOLD) { //likely to have loaded in background
                notifyItemRangeChanged(startPos, endPos);
            } else {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemRangeChanged(startPos, endPos);
                    }
                }, THRESHOLD);
            }
        }
    }

    public void addMoreData(List<D> moreItems) {
        if (isLoadMoreFromTop) {
            Collections.reverse(moreItems);
            this.dataSet.addAll(0, moreItems);
            this.notifyItemRangeChanged(0, moreItems.size());
        } else {
            int startPosition = getActualItemCount() + getHeaderCount();
            this.dataSet.addAll(moreItems);
            this.notifyItemRangeInserted(startPosition, moreItems.size());
        }
    }

    /**
     * Almost using for adding data in the last page
     *
     * @param moreItems
     */
    public void addMoreDataNoAnim(List<D> moreItems) {
        if (isLoadMoreFromTop) {
            Collections.reverse(moreItems);
            this.dataSet.addAll(0, moreItems);
            this.notifyDataSetChanged();
        } else {
            this.dataSet.addAll(moreItems);
            this.notifyDataSetChanged();
        }
    }

    public void refreshData() {
        this.dataSet.clear();
        this.notifyDataSetChanged();
    }

    public void addItem(D item, int position) {
        this.dataSet.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        this.dataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        Collections.swap(this.dataSet, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public List<D> getDataSet() {
        if (dataSet == null) {
            return new ArrayList<>();
        }
        return dataSet;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void setViewState(View loadingView, View errorView, View emptyView) {
        this.loadingView = loadingView;
        this.emptyView = emptyView;
        this.errorView = errorView;
    }

    public void setState(int state) {
        this.state = state;
        this.notifyDataSetChanged();
    }

    @Override
    protected int getDefaultItemViewType(int position) {
        //TODO: Check what type our position is, based on the assumption that the order is headers > items > footers
        if (position < getHeaderCount()) {
            return TYPE_HEADER;
        } else if (getActualItemCount() == 0) {
            switch (state) {
                case STATE_LOADING:
                    return TYPE_LOADING;
                default:
                    return TYPE_EMPTY;
            }
        } else if (getActualItemCount() > 0 &&
                ((isLoadMoreFromTop && hasMoreData() ? position - getHeaderCount() - 1 : position - getHeaderCount()) < getActualItemCount())) {
            switch (state) {
                case STATE_LOADING:
                    return TYPE_LOADING;
                case STATE_EMPTY:
                    return TYPE_EMPTY;
                case STATE_ERROR:
                    return TYPE_ERROR;
                default:
                    return getActualItemViewType(position - getHeaderCount());
            }
        } else {
            return TYPE_FOOTER;
        }

    }

    @Override
    protected RecyclerView.ViewHolder onDefaultCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOADING:
                if (onCreateLoadingViewHolder(parent) == null) {
                    if (loadingView == null) {
                        loadingView = LayoutInflater.from(context).inflate(R.layout.layout_loading_default, parent, false);
                    }
                    return setSimpleHolderLayout(loadingView);
                }
                return onCreateHeaderViewHolder(parent);
            case TYPE_EMPTY:
                if (onCreateEmptyViewHolder(parent) == null) {
                    if (emptyView == null) {
                        emptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty_default, parent, false);
                    }
                    return setSimpleHolderLayout(emptyView);
                }
                return onCreateEmptyViewHolder(parent);
            case TYPE_ERROR:
                if (onCreateErrorViewHolder(parent) == null) {
                    if (errorView == null) {
                        errorView = LayoutInflater.from(context).inflate(R.layout.layout_error_default, parent, false);
                    }
                    return setSimpleHolderLayout(errorView);
                }
                return onCreateErrorViewHolder(parent);
            case TYPE_HEADER:
                return onCreateHeaderViewHolder(parent);
            case TYPE_FOOTER:
                return onCreateFooterViewHolder(parent);
            default:
                return onActualCreateViewHolder(parent, viewType);
        }
    }

    public void onBindErrorViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public void onBindEmptyViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public void onBindLoadingViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
        return null;
    }

    public RecyclerView.ViewHolder onCreateLoadingViewHolder(ViewGroup parent) {
        return null;
    }

    public RecyclerView.ViewHolder onCreateEmptyViewHolder(ViewGroup parent) {
        return null;
    }

    public RecyclerView.ViewHolder onCreateErrorViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected void onDefaultBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getHeaderCount() > 0 && position < getHeaderCount()) {
            onBindHeaderViewHolder(holder, position);
        } else if (getActualItemCount() > 0 &&
                ((isLoadMoreFromTop && hasMoreData() ? position - getHeaderCount() - 1 : position - getHeaderCount()) < getActualItemCount())) {
            switch (state) {
                case STATE_LOADING:
                    onBindLoadingViewHolder(holder, position);
                    break;
                case STATE_EMPTY:
                    onBindEmptyViewHolder(holder, position);
                    break;
                case STATE_ERROR:
                    onBindErrorViewHolder(holder, position);
                    break;
                default:
                    onActualBindViewHolder(holder, position - getHeaderCount());
                    break;
            }
        } else {
            onBindFooterViewHolder(holder, position - getHeaderCount() - getActualItemCount());
        }
    }


    /**
     * Get actual item view type (not including LoadMoreViewHolder)
     * need override
     *
     * @param position
     * @return
     */
    public abstract int getActualItemViewType(int position);

    /**
     * Bind actual view holder (not including LoadMoreViewHolder)
     * need override
     *
     * @param holder
     * @param position
     */
    protected abstract void onActualBindViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * Create actual view holder (not including LoadMoreViewHolder)
     * Need override
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract RecyclerView.ViewHolder onActualCreateViewHolder(ViewGroup parent, int viewType);

    public int getFooterCount() {
        return footers == null ? 0 : footers.size();
    }

    public int getHeaderCount() {
        return headers == null ? 0 : headers.size();
    }

    public void addHeader(Object header) {
        if (state != STATE_NORMAL) {
            Log.e("Error:", " You can not add header or footer while you are not in normal mode");
            return;
        }
        if (!headers.contains(header)) {
            headers.add(header);
            notifyItemInserted(getHeaderCount() - 1);
        }
    }

    public void removeHeader(Object header) {
        if (headers.contains(header)) {
            int position = headers.indexOf(header);
            headers.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeHeader(int position) {
        if (headers.size() > 0 && position < getHeaderCount()) {
            headers.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeAllHeader() {
        if (headers.size() > 0) {
            headers.clear();
            notifyDataSetChanged();
        }
    }

    public void addFooter(Object footer) {
        if (state != STATE_NORMAL) {
            Log.e("Error:", " You can not add header or footer while you are not in normal mode");
            return;
        }
        if (!footers.contains(footer)) {
            footers.add(footer);
            notifyItemInserted(getHeaderCount() + getActualItemCount() + getFooterCount() - 1);
        }
    }

    public void removeFooter(Object footer) {
        if (footers.contains(footer)) {
            int position = footers.indexOf(footer);
            footers.remove(position);
            notifyItemRemoved(getHeaderCount() + getActualItemCount() + position);
        }
    }

    public void removeFooter(int position) {
        if (footers.size() > 0 && position < footers.size()) {
            footers.remove(position);
            notifyItemRemoved(getHeaderCount() + getActualItemCount() + position);
        }
    }

    public void removeAllFooters() {
        if (footers.size() > 0) {
            footers.clear();
            notifyDataSetChanged();
        }
    }

    public D getItem(int position) {
        return hasMoreData() && isLoadMoreFromTop ? dataSet.get(position - 1) : dataSet.get(position);
    }

    public boolean isEmpty() {
        return getActualItemCount() == 0;
    }

    private SimpleViewHolder setSimpleHolderLayout(View view) {
        SimpleViewHolder simpleViewHolder = new SimpleViewHolder(view);
        simpleViewHolder.itemView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );

        return simpleViewHolder;
    }

    private void postDelayed(Runnable runnable, long delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, delayMillis);
    }

    public interface DataSetLoadedCallback<D> {
        void dataSetLoaded(List<D> dataSet);
    }

}


