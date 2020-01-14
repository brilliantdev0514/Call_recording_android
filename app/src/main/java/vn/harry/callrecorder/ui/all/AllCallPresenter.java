package vn.harry.callrecorder.ui.all;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import vn.harry.callrecorder.entity.Recording;
import vn.harry.callrecorder.helper.FileHelper;
import vn.harry.callrecorder.mvp.BasePresenter;
import vn.harry.callrecorder.util.MySharedPreferences;

/**
 * Created by Harry_Hai on 2/16/2018.
 */

public class AllCallPresenter extends BasePresenter<AllCallMvpView> {
    private List<Recording> listDir = new ArrayList<>();
    private List<Recording> listDirNew = new ArrayList<>();

    public void updateDataToDB(FragmentActivity activity) {
        listDir = FileHelper.listRecordings(activity);
        Recording.insertList(listDir);
        String day = MySharedPreferences.getInstance(activity).getString(MySharedPreferences.KEY_AUTO_DELETE, "");
        if (!TextUtils.isEmpty(day)) {
            Recording.deleteRecordList(Recording.getAllItems(), Integer.parseInt(day));
        }

        getMvpView().onUpdateView(onDeleteEmpty(Recording.getAllItems()));
    }

    public List<Recording> onDeleteEmpty(List<Recording> allItems) {
        listDirNew.clear();
        for (int i = 0; i < allItems.size(); i++) {
            if (allItems.get(i).getUserName() != null && allItems.get(i).getUri() != null && allItems.get(i).getPhoneNumber() != null && allItems.get(i).getFileName() != null) {
                listDirNew.add(allItems.get(i));
            }
        }
        return listDirNew;
    }
}
