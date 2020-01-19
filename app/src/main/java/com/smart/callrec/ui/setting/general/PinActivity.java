package com.smart.callrec.ui.setting.general;


import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import com.smart.callrec.R;
import com.smart.callrec.ui.BaseActivity;
import com.smart.callrec.util.MySharedPreferences;

public class PinActivity extends BaseActivity {
    @BindView(R.id.num0)
    protected TextView mTvNum0;
    @BindView(R.id.num1)
    protected TextView mTvNum1;
    @BindView(R.id.num2)
    protected TextView mTvNum2;
    @BindView(R.id.num3)
    protected TextView mTvNum3;
    @BindView(R.id.num4)
    protected TextView mTvNum4;
    @BindView(R.id.num5)
    protected TextView mTvNum5;
    @BindView(R.id.num6)
    protected TextView mTvNum6;
    @BindView(R.id.num7)
    protected TextView mTvNum7;
    @BindView(R.id.num8)
    protected TextView mTvNum8;
    @BindView(R.id.num9)
    protected TextView mTvNum9;
    @BindView(R.id.delete)
    protected TextView mTvDelete;
    @BindView(R.id.ok)
    protected TextView mTvOK;
    @BindView(R.id.pin)
    protected TextView mTvPin;
    private TextView[] textViews;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pin;
    }

    @Override
    protected void initView() {
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = mTvPin.getText().toString();
                if (!TextUtils.isEmpty(pin)) {
                    String pinCorrect = MySharedPreferences.getInstance(PinActivity.this).getString(MySharedPreferences.KEY_PIN_PASS, "");
                    if (TextUtils.equals(pin.trim(), pinCorrect)) {
                        finish();
                    } else {
                        Toast.makeText(PinActivity.this, getString(R.string.pin_error_input), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PinActivity.this, getString(R.string.pin_error_input), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvPin.setText("");
            }
        });
        mTvNum0.setOnClickListener(mOnClickListener);
        mTvNum1.setOnClickListener(mOnClickListener);
        mTvNum2.setOnClickListener(mOnClickListener);
        mTvNum3.setOnClickListener(mOnClickListener);
        mTvNum4.setOnClickListener(mOnClickListener);
        mTvNum5.setOnClickListener(mOnClickListener);
        mTvNum6.setOnClickListener(mOnClickListener);
        mTvNum7.setOnClickListener(mOnClickListener);
        mTvNum8.setOnClickListener(mOnClickListener);
        mTvNum9.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.num0:
                    setNumber("0");
                    break;
                case R.id.num1:
                    setNumber("1");
                    break;
                case R.id.num2:
                    setNumber("2");
                    break;
                case R.id.num3:
                    setNumber("3");
                    break;
                case R.id.num4:
                    setNumber("4");
                    break;
                case R.id.num5:
                    setNumber("5");
                    break;
                case R.id.num6:
                    setNumber("6");
                    break;
                case R.id.num7:
                    setNumber("7");
                    break;
                case R.id.num8:
                    setNumber("8");
                    break;
                case R.id.num9:
                    setNumber("9");
                    break;
            }
        }
    };

    private void setNumber(String text) {
        String pin = mTvPin.getText().toString();
        mTvPin.setText(pin + text);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onBackPressed() {
    }
}
