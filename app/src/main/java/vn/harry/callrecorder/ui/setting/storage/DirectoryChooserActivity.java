package vn.harry.callrecorder.ui.setting.storage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.harry.callrecorder.R;
import vn.harry.callrecorder.util.Constants;
import vn.harry.callrecorder.util.UserPreferences;

public class DirectoryChooserActivity extends Activity {

    public static final String RESULT_SELECTED_DIR = "selected_dir";

    @BindView(R.id.confirm)
    Button butConfirm;
    @BindView(R.id.selected_folder)
    TextView tvSelectedFolder;
    @BindView(R.id.listDirectory)
    ListView listDirectories;

    private ArrayAdapter<String> listDirectoriesAdapter;
    private ArrayList<String> filenames;
    private File selectedDir;
    private File[] filesInDir;
    private FileObserver fileObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.directory_chooser);
        filenames = new ArrayList<>();
        listDirectoriesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, filenames);
        listDirectories.setAdapter(listDirectoriesAdapter);
        Uri uri = UserPreferences.getStorageUri();
        if (!uri.getScheme().equals("file")) {
            finish();
        }
        changeDirectory(new File(uri.getPath()));
    }

    @OnClick({R.id.confirm, R.id.cancel, R.id.nav_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                if (!isValidFile(selectedDir)) {
                    return;
                }
                if (selectedDir.listFiles().length == 0) {
                    returnSelectedFolder();
                } else {
                    showNonEmptyDirectoryWarning();
                }
                break;
            case R.id.cancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.nav_up:
                if (selectedDir != null) {
                    changeDirectory(selectedDir.getParentFile());
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fileObserver != null) {
            fileObserver.stopWatching();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fileObserver != null) {
            fileObserver.startWatching();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        listDirectoriesAdapter = null;
        fileObserver = null;
    }

    /**
     * Finishes the activity and returns the selected folder as a result.
     * The selected folder can also be null.
     */
    private void returnSelectedFolder() {
        Intent resultData = new Intent();
        if (selectedDir != null) {
            resultData.putExtra(RESULT_SELECTED_DIR,
                    selectedDir.getAbsolutePath());
        }
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

    private void showNonEmptyDirectoryWarning() {
        new AlertDialog.Builder(DirectoryChooserActivity.this)
                .setTitle(getString(R.string.choosefolder_title))
                .setMessage(getString(R.string.choosefolder_des))
                .setNegativeButton(R.string.cancel, (dialog, i) -> {
                    dialog.dismiss();
                })
                .setPositiveButton(R.string.confirm, (dialog, i) -> {
                    dialog.dismiss();
                    returnSelectedFolder();
                })
                .create()
                .show();
    }

    /**
     * Change the directory that is currently being displayed.
     *
     * @param dir The file the activity should switch to. This File must be
     *            non-null and a directory, otherwise the displayed directory
     *            will not be changed.
     */
    private void changeDirectory(File dir) {
        if (dir == null) {
            Log.d(Constants.TAG, "Could not change folder: dir was null");
            return;
        }
        if (!dir.isDirectory()) {
            Log.d(Constants.TAG, "Could not change folder: dir is no directory");
            return;
        }
        File[] contents = dir.listFiles();
        if (contents == null) {
            Log.d(Constants.TAG, "Could not change folder: contents of dir were null");
            return;
        }
        int numDirectories = 0;
        for (File f : contents) {
            if (f.isDirectory()) {
                numDirectories++;
            }
        }
        filesInDir = new File[numDirectories];
        filenames.clear();
        for (int i = 0, counter = 0; i < numDirectories; counter++) {
            if (contents[counter].isDirectory()) {
                filesInDir[i] = contents[counter];
                filenames.add(contents[counter].getName());
                i++;
            }
        }
        Arrays.sort(filesInDir);
        Collections.sort(filenames);
        selectedDir = dir;

        String path = dir.getAbsolutePath();

        tvSelectedFolder.setText(path);
        listDirectoriesAdapter.notifyDataSetChanged();
        fileObserver = createFileObserver(path);
        fileObserver.startWatching();
        Log.d(Constants.TAG, "Changed directory to " + path);
        refreshButtonState();
    }

    /**
     * Changes the state of the buttons depending on the currently selected
     * file or folder.
     */
    private void refreshButtonState() {
        if (selectedDir == null) {
            return;
        }
        butConfirm.setEnabled(isValidFile(selectedDir));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            invalidateOptionsMenu();
    }

    /// Refresh the contents of the directory that is currently shown.
    private void refreshDirectory() {
        if (selectedDir != null) {
            changeDirectory(selectedDir);
        }
    }

    /// Sets up a FileObserver to watch the current directory.
    private FileObserver createFileObserver(String path) {
        return new FileObserver(path, FileObserver.CREATE |
                FileObserver.DELETE | FileObserver.MOVED_FROM |
                FileObserver.MOVED_TO) {
            @Override
            public void onEvent(int event, String path) {
                Log.d(Constants.TAG, "FileObserver received event " + event);
                runOnUiThread(() -> refreshDirectory());
            }
        };
    }

    private static int convertDpToPixel(int dp, DisplayMetrics metrics) {
        return Math.round(dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /// Shows a dialog that asks the user for a folder name for a new folder.
    private void openNewFolderDialog() {
        final EditText inputWidget = new EditText(this);
        inputWidget.setHint(R.string.new_folder);

        final FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        params.leftMargin = convertDpToPixel(24, dm);
        params.rightMargin = convertDpToPixel(24, dm);

        inputWidget.setLayoutParams(params);
        container.addView(inputWidget);

        new AlertDialog.Builder(this)
                .setTitle(R.string.enter_new_folder_name)
                .setView(container)
                .setNegativeButton(R.string.cancel, (dialog, i) -> {
                    dialog.dismiss();
                })
                .setPositiveButton(R.string.confirm, (dialog, i) -> {
                    dialog.dismiss();
                    int msg = createFolder(inputWidget.getText().toString());
                    Toast t = Toast.makeText(DirectoryChooserActivity.this,
                            msg, Toast.LENGTH_SHORT);
                    t.show();
                })
                .create()
                .show();
    }

    /// Creates a new folder in the current directory.
    private int createFolder(String name) {
        if (selectedDir == null) {
            return R.string.create_folder_error;
        }
        if (!selectedDir.canWrite()) {
            return R.string.create_folder_error_no_write_access;
        }
        File newDir = new File(selectedDir, name);
        if (newDir.exists()) {
            return R.string.create_folder_error_already_exists;
        }
        return newDir.mkdir() ? R.string.create_folder_success :
                R.string.create_folder_error;
    }

    /// Returns true if the selected file or directory would be valid selection.
    private boolean isValidFile(File file) {
        return file != null && file.isDirectory() && file.canRead() && file.canWrite();
    }

}

