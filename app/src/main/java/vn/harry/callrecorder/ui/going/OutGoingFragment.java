package vn.harry.callrecorder.ui.going;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import vn.harry.callrecorder.entity.MessageEvent;
import vn.harry.callrecorder.entity.Recording;
import vn.harry.callrecorder.ui.all.AllCallFragment;
import vn.harry.callrecorder.ui.option.BottomSheetFragment;

/**
 * Created by Harry_Hai on 2/13/2018.
 */

public class OutGoingFragment extends AllCallFragment {
    protected List<Recording> listOutGoing = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateView(List<Recording> allItems) {
        listOutGoing.clear();
        for (Recording recorder : allItems) {
            if (recorder.isOutGoing()) {
                listOutGoing.add(recorder);
            }
        }
        formatData(listOutGoing);
        mCallAdapter.dataSet.clear();
        mCallAdapter.loadInitialDataSet(mData);
        mRecyclerView.getRecycledViewPool().clear();
        mCallAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHandleSearch(MessageEvent event) {
        if (event.getCurrentItem() == OUTGOING) {
            filter(event.getMessage());
        }
    }
}
