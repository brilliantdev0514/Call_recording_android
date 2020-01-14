package vn.harry.callrecorder.ui.all;

import java.util.List;

import vn.harry.callrecorder.entity.Recording;
import vn.harry.callrecorder.mvp.BaseMvpView;

/**
 * Created by Harry_Hai on 2/16/2018.
 */

public interface AllCallMvpView extends BaseMvpView {
    void onUpdateView(List<Recording> allItems);
}
