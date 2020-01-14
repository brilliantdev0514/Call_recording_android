package vn.harry.callrecorder.ui.recycleBin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import vn.harry.callrecorder.R;
import vn.harry.callrecorder.entity.Recording;
import vn.harry.callrecorder.helper.FileHelper;
import vn.harry.callrecorder.ui.all.AllCallFragment;
import vn.harry.callrecorder.ui.all.TextViewItem;
import vn.harry.callrecorder.util.DateTimeUtil;

/**
 * Created by nguye on 2/25/2018.
 */

public class RecycleBinFragment extends AllCallFragment {

    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.txtScreen)
    TextView txtScreen;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivDelete)
    ImageView ivTrash;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.ivRefresh)
    ImageView ivRefresh;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    private List<Recording> listRecoder = new ArrayList<>();

    public static RecycleBinFragment newInstance() {
        Bundle args = new Bundle();
        RecycleBinFragment fragment = new RecycleBinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View root) {
        super.initView(root);
        llBottom.setVisibility(View.GONE);
        setUpToolBarView(getString(R.string.recycle_bin), txtScreen, ivBack);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycle_bin;
    }

    @Override
    protected void formatData(List<Recording> allItems) {
        Calendar date = null;
        mData = new ArrayList<>();
        String stringTime;
        for (int i = 0; i < allItems.size(); i++) {

            Calendar currentDate = Calendar.getInstance();
            if (!allItems.get(i).isDelete()) continue;
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
    }

    @Override
    public void onItemClick(View view, Recording item, int position) {
        ImageView imageCheck = view.findViewById(R.id.ivCheck);
        ImageView imageContact = view.findViewById(R.id.ivContact);
        RelativeLayout root = view.findViewById(R.id.root);
        if (imageCheck.getVisibility() == View.GONE) {
            onHandleView(imageContact, false);
            onHandleView(imageCheck, true);
            root.setBackgroundColor(getResources().getColor(R.color.colorGray79));
            listRecoder.add(item);
        } else {
            onHandleView(imageContact, true);
            onHandleView(imageCheck, false);
            root.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            for (int i = 0; i < listRecoder.size(); i++) {
                if (item.getIdCall().equals(listRecoder.get(i).getIdCall()))
                    listRecoder.remove(i);
            }
        }
        if (listRecoder.size() > 0) {
            ivRefresh.setVisibility(View.VISIBLE);
        } else {
            ivRefresh.setVisibility(View.GONE);
        }
    }

    private void onHandleView(View view, boolean b) {
        view.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.ivDelete, R.id.ivSearch, R.id.ivRefresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSearch:
//                filter(event.getMessage());
                break;
            case R.id.ivRefresh:
                if (listRecoder.size() == 0) return;
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.dialog_refresh_recorder)
                        .setMessage(R.string.dialog_refresh_item_content)
                        .setPositiveButton(R.string.yes, (dialog, id) -> {
                            Recording.refreshRecorder(listRecoder);
                            onUpdateView(Recording.getAllItems());
                            dialog.cancel();
                        })
                        .setNegativeButton(R.string.no, (dialog, id) -> {
                            dialog.cancel();
                        })
                        .show();
                break;
            case R.id.ivDelete:
                if (listRecoder.size() == 0) return;
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.dialog_delete_recorder)
                        .setMessage(R.string.dialog_delete_item_content)
                        .setPositiveButton(R.string.yes, (dialog, id) -> {

                            for (int i = 0; i < listRecoder.size(); i++) {
                                //delete db
                                listRecoder.get(i).delete();
                                //delete Storage File
                                FileHelper.deleteRecord(getActivity(), listRecoder.get(i).getUri());
                            }
                            onUpdateView(Recording.getAllItems());
                            dialog.cancel();
                        })
                        .setNegativeButton(R.string.no, (dialog, id) -> {
                            dialog.cancel();
                        })
                        .show();
                break;
            default:
                break;
        }
    }
}
