package com.smart.callrec.ui.all;

import java.util.List;

import com.smart.callrec.entity.Recording;
import com.smart.callrec.mvp.BaseMvpView;

/**
 * Created by Harry_Hai on 2/16/2018.
 */

public interface AllCallMvpView extends BaseMvpView {
    void onUpdateView(List<Recording> allItems);
}
