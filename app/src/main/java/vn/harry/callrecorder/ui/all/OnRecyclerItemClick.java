package vn.harry.callrecorder.ui.all;

import android.view.View;

import vn.harry.callrecorder.entity.Recording;

/**
 * Created by Harry_Hai on 2/21/2018.
 */

interface OnRecyclerItemClick<T> {
    void onItemClick(View view, Recording item, int position);
}
