package vn.harry.callrecorder.ui.inportant;

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
