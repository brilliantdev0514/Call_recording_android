package vn.harry.callrecorder.ui.donate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import butterknife.BindView;
import vn.harry.callrecorder.R;
import vn.harry.callrecorder.entity.PayPalEntity;
import vn.harry.callrecorder.ui.BaseFragment;

/**
 * Created by hainm on 2/21/2018.
 */

public class DonateOptionFragment extends BaseFragment<DonateOptionPresenter> implements DonateOptionMvp {

    public static final String TAG = "DonateOptionFragment";
    private static final String CONFIG_CLIENT_ID_TEST = "AYVJHU4xlo2_CHnvePev4nPSTaC1mVpzyedTNAXo4SrG0J4jPGEXXYKTCEAMnGITZwLdFfmE5s6TgzBW";
    private static final String CONFIG_CLIENT_ID = "AZ9PAT_sVqzniX-3bgjMaDzXt-DctVDOZL7Km4iC2fHMOBv7ZhJieVrKvhYVzHyzgnUGuyaHHV9ktAZ-";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(CONFIG_CLIENT_ID);

    @BindView(R.id.editText1)
    EditText editText1;
    @BindView(R.id.donate)
    TextView donate;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.txtScreen)
    TextView txtScreen;

    public static DonateOptionFragment newInstance() {
        Bundle args = new Bundle();
        DonateOptionFragment fragment = new DonateOptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.donate_fragment;
    }

    @Override
    protected void initView(View root) {
        setUpToolBarView(getString(R.string.donate_title), txtScreen, ivBack);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText1.getText().toString().equals("")) return;
                beginPayment(new PayPalEntity(editText1.getText().toString(), "USD", "Call Recorder Is Awesome"));
            }
        });
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onResponseError(int apiMethod, int statusCode) {

    }

    public void beginPayment(PayPalEntity payPalEntity) {
        Intent serviceConfig = new Intent(getActivity(), PayPalService.class);
        serviceConfig.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(serviceConfig);

        PayPalPayment payment = new PayPalPayment(new BigDecimal(payPalEntity.getPrice()), payPalEntity.getTypePrice(), payPalEntity.getContent(), PayPalPayment.PAYMENT_INTENT_SALE);

        Intent paymentConfig = new Intent(getActivity(), PaymentActivity.class);
        paymentConfig.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config); //send the same configuration for restart resiliency
        paymentConfig.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(paymentConfig, 0);
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    getActivity().finish();
                    Log.i("sampleapp", confirm.toJSONObject().toString(4));
                    Toast.makeText(getActivity(), getString(R.string.pay_success), Toast.LENGTH_SHORT).show();
                    // TODO: send 'confirm' to your server for verification.
                } catch (JSONException e) {
                    Log.e("sampleapp", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("sampleapp", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("sampleapp", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
}
