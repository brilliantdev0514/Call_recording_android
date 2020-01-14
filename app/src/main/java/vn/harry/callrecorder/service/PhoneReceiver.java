package vn.harry.callrecorder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import vn.harry.callrecorder.App;
import vn.harry.callrecorder.helper.FileHelper;
import vn.harry.callrecorder.util.Constants;

public class PhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i("HaiNm", "PhoneReceiver: Received unexpected intent: " + action);
        if (!action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED) &&
                !action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.e(Constants.TAG, "PhoneReceiver: Received unexpected intent: " + action);
            return;
        }

        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        String extraState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        Log.d(Constants.TAG, "PhoneReceiver phone number: " + phoneNumber);
        if (!FileHelper.isStorageWritable(context))
            return;
        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())) {
            App.isOutComming = true;
        }
        if (extraState != null) {
            dispatchExtra(context, intent, phoneNumber, extraState);
        } else if (phoneNumber != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, RecordService.class)
                        .putExtra("commandType", Constants.STATE_INCOMING_NUMBER)
                        .putExtra("phoneNumber", phoneNumber));
            } else {
                context.startService(new Intent(context, RecordService.class)
                        .putExtra("commandType", Constants.STATE_INCOMING_NUMBER)
                        .putExtra("phoneNumber", phoneNumber));
            }
        } else {
            Log.d(Constants.TAG, "phone number x:x " + null);
        }
    }

    private void dispatchExtra(Context context, Intent intent,
                               String phoneNumber, String extraState) {
        if (extraState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, RecordService.class)
                        .putExtra("commandType", Constants.STATE_CALL_START));
            } else {
                context.startService(new Intent(context, RecordService.class)
                        .putExtra("commandType", Constants.STATE_CALL_START));
            }
        } else if (extraState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, RecordService.class)
                        .putExtra("commandType", Constants.STATE_CALL_END));
            } else {
                context.startService(new Intent(context, RecordService.class)
                        .putExtra("commandType", Constants.STATE_CALL_END));
            }
        } else if (extraState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            if (phoneNumber == null)
                phoneNumber = intent.getStringExtra(
                        TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, RecordService.class)
                        .putExtra("commandType", Constants.STATE_INCOMING_NUMBER)
                        .putExtra("phoneNumber", phoneNumber));
            } else {
                context.startService(new Intent(context, RecordService.class)
                        .putExtra("commandType", Constants.STATE_INCOMING_NUMBER)
                        .putExtra("phoneNumber", phoneNumber));
            }
        }
    }
}
