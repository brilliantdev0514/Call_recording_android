package vn.harry.callrecorder.ui;

import vn.harry.callrecorder.R;
import vn.harry.callrecorder.ui.about.AboutFragment;
import vn.harry.callrecorder.ui.donate.DonateOptionFragment;
import vn.harry.callrecorder.ui.recycleBin.RecycleBinFragment;
import vn.harry.callrecorder.ui.setting.SettingFragment;
import vn.harry.callrecorder.util.Constants;
import vn.harry.callrecorder.util.FragmentUtil;

/**
 * Created by Harry_Hai on 2/13/2018.
 */

public class DetailActivity extends BaseActivity {
    public static String KEY_SETTING = "Settings";
    public static String KEY_ABOUT = "About";
    public static String KEY_DONATE = "Donate";
    public static String KEY_DELETE = "Delete";
    public static String KEY_NUMBER = "Number";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        String screen = getIntent().getStringExtra(Constants.KEY_SCREEN);
        if (screen.equals(KEY_SETTING)) {
            FragmentUtil.showFragment(this, SettingFragment.newInstance(), true);
        } else if (screen.equals(KEY_ABOUT)) {
            FragmentUtil.showFragment(this, AboutFragment.newInstance(), false, null, AboutFragment.TAG, false, null);
        } else if (screen.equals(KEY_DONATE)) {
            FragmentUtil.showFragment(this, DonateOptionFragment.newInstance(), false, null, DonateOptionFragment.TAG, false, null);
        } else if (screen.equals(KEY_DELETE)) {
            FragmentUtil.showFragment(this, RecycleBinFragment.newInstance(), false, null, AboutFragment.TAG, false, null);
        } else {
            finish();
            return;
        }
    }

    @Override
    protected void initData() {

    }
}
