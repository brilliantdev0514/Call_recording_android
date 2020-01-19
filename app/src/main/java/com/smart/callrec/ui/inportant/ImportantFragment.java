package com.smart.callrec.ui.inportant;

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

public class ImportantFragment extends AllCallFragment {
    protected List<Recording> listImportant = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateView(List<Recording> allItems) {
        listImportant.clear();
        for (Recording recorder : allItems) {
            if (recorder.isImportant()) {
                listImportant.add(recorder);
            }
        }
        formatData(listImportant);
        mCallAdapter.dataSet.clear();
        mCallAdapter.loadInitialDataSet(mData);
        mRecyclerView.getRecycledViewPool().clear();
        mCallAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHandleSearch(MessageEvent event) {
        if (event.getCurrentItem() == IMPORTANT) {
            filter(event.getMessage());
        }
    }
}
