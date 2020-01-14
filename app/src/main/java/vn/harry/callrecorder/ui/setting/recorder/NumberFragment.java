package vn.harry.callrecorder.ui.setting.recorder;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import vn.harry.callrecorder.R;
import vn.harry.callrecorder.ui.BaseFragment;
import vn.harry.callrecorder.util.Constants;

import static android.app.Activity.RESULT_OK;

public class NumberFragment extends BaseFragment {
    @BindView(R.id.txtScreen)
    TextView txtScreen;
    @BindView(R.id.txtNone)
    TextView txtNone;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivAdd)
    ImageView addNumber;
    @BindView(R.id.ivAddContact)
    ImageView addContact;
    @BindView(R.id.listNumber)
    ListView mListView;
    private static final String ARG_PARAM1 = "param1";
    private static final int PICK_CONTACT_REQUEST = 1001;

    private boolean isExcluded = false;
    private List<NumberPhone> mData = new ArrayList<>();
    private ArrayAdapter<NumberPhone> mAdapter;
    private String number = "";

    public NumberFragment() {
        // Required empty public constructor
    }

    public static NumberFragment newInstance(boolean isExcluded) {
        NumberFragment fragment = new NumberFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isExcluded);
        fragment.setArguments(args);
        return fragment;
    }

    public static NumberFragment newInstance(String number, boolean isExcluded) {
        NumberFragment fragment = new NumberFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isExcluded);
        args.putString(Constants.KEY_PHONE_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isExcluded = getArguments().getBoolean(ARG_PARAM1);
            number = getArguments().getString(Constants.KEY_PHONE_NUMBER);
        }
        mData = NumberPhone.getItemsByType(isExcluded);
    }

    @Override
    protected void initData() {
        mAdapter = new ArrayAdapter<NumberPhone>(getActivity(), android.R.layout.simple_list_item_1, mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogDelete(mData.get(position));
            }
        });
        String title = isExcluded ? getString(R.string.recording_excluded) : getString(R.string.recording_included);
        setUpToolBarView(title, txtScreen, ivBack);
        addNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNumberDialog(null);
            }
        });
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact();
            }
        });

    }

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getActivity().getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int column1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String number = cursor.getString(column).replaceAll("\\s", "");
                String name = cursor.getString(column1);
                if (!isExist(number)) {
                    NumberPhone numberPhone = NumberPhone.getItemsById(number);
                    if (numberPhone == null) {
                        numberPhone = new NumberPhone(name, number, isExcluded);
                    }
                    numberPhone.setExcluded(isExcluded);
                    numberPhone.save();
                    mData.add(numberPhone);
                    mAdapter.notifyDataSetChanged();
                    onShowTextNone();
                }
            }
        }
    }

    private boolean isExist(String phoneNumber) {
        for (NumberPhone numberPhone : mData) {
            if (TextUtils.equals(numberPhone.getPhoneNumber(), phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    private void showDialogDelete(NumberPhone phoneNumber) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.dialog_delete_number);
        alertDialog.setMessage(phoneNumber.getPhoneNumber());
        alertDialog.setPositiveButton(getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        phoneNumber.delete();
                        mData.remove(phoneNumber);
                        mAdapter.notifyDataSetChanged();
                        onShowTextNone();
                    }
                });

        alertDialog.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void onShowTextNone() {
        txtNone.setVisibility(mData.size() == 0 ? View.VISIBLE : View.GONE);
    }

    private void showAddNumberDialog(String number) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.add_number));
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (number != null) {
            input.setText(number);
        }
        alertDialog.setView(input);
        alertDialog.setPositiveButton(getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String pin = input.getText().toString();
                        if (!TextUtils.isEmpty(pin)) {
                            if (!isExist(pin)) {
                                NumberPhone numberPhone = new NumberPhone("", pin, isExcluded);
                                numberPhone.save();
                                mData.add(numberPhone);
                                mAdapter.notifyDataSetChanged();
                                onShowTextNone();
                            }
                        }
                    }
                });

        alertDialog.setNeutralButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_number;
    }

    @Override
    protected void initView(View root) {
        onShowTextNone();
        if (!number.equals("") && number != null) {
            showAddNumberDialog(number);
            number = "";
        }
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void initPresenter() {

    }


}
