package vn.harry.callrecorder.ui.theme;

import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vn.harry.callrecorder.R;

public class MaterialTheme implements Serializable {
    // App themes
    public static final MaterialTheme THEME_RED =
            new MaterialTheme(R.string.material_theme_red, R.style.AppTheme_Red);
    public static final MaterialTheme THEME_ORANGE =
            new MaterialTheme(R.string.material_theme_orange, R.style.AppTheme_Orange);
    public static final MaterialTheme THEME_GREEN =
            new MaterialTheme(R.string.material_theme_green, R.style.AppTheme_Green);
    public static final MaterialTheme THEME_BLUE =
            new MaterialTheme(R.string.material_theme_blue, R.style.AppTheme_Blue);

    // Dialog themes
    public static final MaterialTheme THEME_DIALOG_RED =
            new MaterialTheme(R.string.material_theme_red, R.style.AppTheme_Dialog_Red);
    public static final MaterialTheme THEME_DIALOG_ORANGE =
            new MaterialTheme(R.string.material_theme_orange, R.style.AppTheme_Dialog_Orange);
    public static final MaterialTheme THEME_DIALOG_GREEN =
            new MaterialTheme(R.string.material_theme_green, R.style.AppTheme_Dialog_Green);
    public static final MaterialTheme THEME_DIALOG_BLUE =
            new MaterialTheme(R.string.material_theme_blue, R.style.AppTheme_Dialog_Blue);

    // Alert dialog themes
    public static final MaterialTheme THEME_ALERT_DIALOG_RED =
            new MaterialTheme(R.string.material_theme_red, R.style.AppTheme_Dialog_Alert_Red);
    public static final MaterialTheme THEME_ALERT_DIALOG_ORANGE =
            new MaterialTheme(R.string.material_theme_orange, R.style.AppTheme_Dialog_Alert_Orange);
    public static final MaterialTheme THEME_ALERT_DIALOG_GREEN =
            new MaterialTheme(R.string.material_theme_green, R.style.AppTheme_Dialog_Alert_Green);
    public static final MaterialTheme THEME_ALERT_DIALOG_BLUE =
            new MaterialTheme(R.string.material_theme_blue, R.style.AppTheme_Dialog_Alert_Blue);

    private static List<MaterialTheme> sThemeList;
    private static List<MaterialTheme> sDialogThemeList;
    private static List<MaterialTheme> sAlertDialogThemeList;

    @StringRes
    private int nameResId;
    @StyleRes
    private int themeResId;

    public MaterialTheme(@StringRes int nameResId, @StyleRes int themeResId) {
        this.nameResId = nameResId;
        this.themeResId = themeResId;
    }

    public MaterialTheme(@StyleRes int themeResId) {
        this.themeResId = themeResId;
        List<MaterialTheme> list = getThemeList();
        for (MaterialTheme materialTheme : list){
            if(materialTheme.getThemeResId() == themeResId){
                this.nameResId = materialTheme.getNameResId();
                break;
            }
        }
    }
    public static List<MaterialTheme> getThemeList() {
        if (sThemeList == null) {
            sThemeList = new ArrayList<MaterialTheme>();
            sThemeList.add(THEME_RED);
            sThemeList.add(THEME_ORANGE);
            sThemeList.add(THEME_GREEN);
            sThemeList.add(THEME_BLUE);
        }
        return sThemeList;
    }

    public static List<MaterialTheme> getDialogThemeList() {
        if (sDialogThemeList == null) {
            sDialogThemeList = new ArrayList<MaterialTheme>();
            sDialogThemeList.add(THEME_DIALOG_RED);
            sDialogThemeList.add(THEME_DIALOG_ORANGE);
            sDialogThemeList.add(THEME_DIALOG_GREEN);
            sDialogThemeList.add(THEME_DIALOG_BLUE);
        }
        return sDialogThemeList;
    }

    public static List<MaterialTheme> getAlertDialogThemeList() {
        if (sAlertDialogThemeList == null) {
            sAlertDialogThemeList = new ArrayList<MaterialTheme>();
            sAlertDialogThemeList.add(THEME_ALERT_DIALOG_RED);
            sAlertDialogThemeList.add(THEME_ALERT_DIALOG_ORANGE);
            sAlertDialogThemeList.add(THEME_ALERT_DIALOG_GREEN);
            sAlertDialogThemeList.add(THEME_ALERT_DIALOG_BLUE);
        }
        return sAlertDialogThemeList;
    }

    public int getNameResId() {
        return nameResId;
    }

    public int getThemeResId() {
        return themeResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MaterialTheme that = (MaterialTheme) o;

        if (nameResId != that.nameResId) {
            return false;
        }
        return themeResId == that.themeResId;
    }

    @Override
    public int hashCode() {
        int result = nameResId;
        result = 31 * result + themeResId;
        return result;
    }
}
