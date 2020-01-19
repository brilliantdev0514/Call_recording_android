package com.smart.callrec.ui.coming;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.smart.callrec.entity.MessageEvent;
import com.smart.callrec.entity.Recording;
import com.smart.callrec.ui.all.AllCallFragment;
import com.smart.callrec.ui.option.BottomSheetFragment;

/**
 * Created by Harry_Hai on 2/13/2018.
 */

public class InComingFragment extends AllCallFragment {
    protected List<Recording> listInComing = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateView(List<Recording> allItems) {
        listInComing.clear();
        for (Recording recorder : allItems) {
            if (!recorder.isOutGoing()) {
                listInComing.add(recorder);
            }
        }
        formatData(listInComing);
        mCallAdapter.dataSet.clear();
        mCallAdapter.loadInitialDataSet(mData);
        mRecyclerView.getRecycledViewPool().clear();
        mCallAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHandleSearch(MessageEvent event) {
        if (event.getCurrentItem() == INCOMING) {
            filter(event.getMessage());
        }
    }
}
