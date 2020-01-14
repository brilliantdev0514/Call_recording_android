package vn.harry.callrecorder.ui.setting.language;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import vn.harry.callrecorder.R;
import vn.harry.callrecorder.ui.DetailActivity;
import vn.harry.callrecorder.util.Constants;
import vn.harry.callrecorder.util.MySharedPreferences;

public class LanguagesDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Map<String, String> localeMap = new HashMap<String, String>();
        String[] availableLocales = {"en", "ja"};
        String[] availableLocalesName = getResources().getStringArray(
                R.array.languages_name);
        final String[] values = new String[availableLocales.length];

        for (int i = 0; i < availableLocales.length; ++i) {
            String localString = availableLocales[i];
            if (localString.contains("-")) {
                localString = localString.substring(0,
                        localString.indexOf("-"));
            }
            values[i] = availableLocalesName[i] + " ("
                    + availableLocales[i] + ")";
            localeMap.put(values[i], availableLocales[i]);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getActivity());
        dialogBuilder.setTitle(getString(R.string.language));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        ListView listView = new ListView(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                String localString = localeMap.get(values[position]);
                if (localString.contains("-")) {
                    conf.locale = new Locale(localString.substring(0,
                            localString.indexOf("-")), localString.substring(
                            localString.indexOf("-"), localString.length()));
                } else {
                    conf.locale = new Locale(localString);
                }
                res.updateConfiguration(conf, dm);
                MySharedPreferences.getInstance(getActivity()).putString(MySharedPreferences.KEY_LANGUAGE, localString);
                Intent intentSetting = new Intent(getActivity(), DetailActivity.class);
                intentSetting.putExtra(Constants.KEY_SCREEN, "Settings");
                startActivity(intentSetting);
                getActivity().finish();
            }
        });
        dialogBuilder.setView(listView);

        return dialogBuilder.create();
    }

}
