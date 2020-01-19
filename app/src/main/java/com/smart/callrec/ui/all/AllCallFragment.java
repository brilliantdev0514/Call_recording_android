package com.smart.callrec.ui.all;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import com.smart.callrec.R;
import com.smart.callrec.entity.MessageEvent;
import com.smart.callrec.entity.Recording;
import com.smart.callrec.ui.BaseFragment;
import com.smart.callrec.ui.option.BottomSheetFragment;
import com.smart.callrec.util.DateTimeUtil;

/**
 * Created by Harry_Hai on 2/13/2018.
 */

public class AllCallFragment extends BaseFragment<AllCallPresenter> implements AllCallMvpView, OnRecyclerItemClick<Recording> {

    protected static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
    public static final String KEY_ITEM = "key_item";
    public static final String KEY_POSITION_PAPER = "key_position_paper";

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.total)
    protected TextView txtTotal;

    protected int ALL = 0;
    protected int INCOMING = 1;
    protected int OUTGOING = 2;
    protected int IMPORTANT = 3;

    protected CallAdapter mCallAdapter;
    protected List<Object> mData = new ArrayList<>();
    protected List<Object> mSearch = new ArrayList<>();
    protected LinearLayoutManager mLinearLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_all_call;
    }

    @Override
    protected void initView(View root) {
        initRecyclerView();
    }

    @Override
    protected void initData() {
        mPresenter.updateDataToDB(getActivity());
    }

    protected void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mCallAdapter = new CallAdapter(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCallAdapter.loadInitialDataSet(mData);
        mRecyclerView.setAdapter(mCallAdapter);
        mCallAdapter.setOnRecyclerItemClick(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateView(List<Recording> allItems) {
        formatData(allItems);
        mCallAdapter.dataSet.clear();
        mCallAdapter.loadInitialDataSet(mData);
        mRecyclerView.getRecycledViewPool().clear();
        mCallAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        onUpdateView(mPresenter.onDeleteEmpty(Recording.getAllItems()));
    }

    @SuppressLint("DefaultLocale")
    protected void formatData(List<Recording> allItems) {
        Calendar date = null;
        int total = 0;
        mData = new ArrayList<>();
        String stringTime;
        for (int i = 0; i < allItems.size(); i++) {

            Calendar currentDate = Calendar.getInstance();
            if (allItems.get(i).isDelete()) {
                continue;
            } else {
                total++;
            }
            try {
                //get id because get date is wrong.i don't know why
                currentDate.setTime(formatter.parse(allItems.get(i).getIdCall()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!DateTimeUtil.isTheSameMonth(currentDate, date)) {
                // add section data
                stringTime = DateFormat
                        .format("MMM d ,yyyy",
                                currentDate)
                        .toString();
                mData.add(new TextViewItem(stringTime));
                mData.add(allItems.get(i));
                date = currentDate;
            } else {
                mData.add(allItems.get(i));
            }
        }
        txtTotal.setText(String.format("%d", total));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(MessageEvent event) {
        if (event.getMessage().equals(BottomSheetFragment.EVENT_BUS_UPDATE)) {
            onUpdateView(mPresenter.onDeleteEmpty(Recording.getAllItems()));
        } else {
            onHandleSearch(event);
        }
    }

    public void onHandleSearch(MessageEvent event) {
        if (event.getCurrentItem() == ALL) {
            filter(event.getMessage());
        }
    }

    // Filter Class
    @SuppressLint("SetTextI18n")
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mSearch.clear();
        if (charText.length() == 0) {
            mSearch.addAll(mData);
        } else {
            for (Object wp : mData) {
                if (!(wp instanceof TextViewItem)) {
                    Recording recording = (Recording) wp;
                    if (recording.getUserName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        mSearch.add(wp);
                    }
                }
            }
        }
        mCallAdapter.dataSet.clear();
        if (mSearch.size() == 0) {
            mCallAdapter.loadInitialDataSet(new ArrayList<>());
        } else {
            mCallAdapter.loadInitialDataSet(mSearch);
        }
        mCallAdapter.notifyDataSetChanged();
        txtTotal.setText(mSearch.size() + "");
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void initPresenter() {
        mPresenter = new AllCallPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onResponseError(int apiMethod, int statusCode) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onItemClick(View view, Recording item, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ITEM, item);
        bundle.putInt(KEY_POSITION_PAPER, ALL);
        BottomSheetFragment.newInstance(bundle).show(getActivity().getSupportFragmentManager(), BottomSheetFragment.TAG);
    }
}
