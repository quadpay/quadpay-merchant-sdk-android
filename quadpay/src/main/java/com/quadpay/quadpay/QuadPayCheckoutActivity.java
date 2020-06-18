package com.quadpay.quadpay;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.security.InvalidParameterException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class QuadPayCheckoutActivity extends QuadPayActivity
        implements QuadPayJSMessageListener, QuadPayWebViewClient.WebViewClientCallbacks {
    static void start(@NonNull Activity activity,
                      @NonNull QuadPayCheckoutDetails details,
                      boolean virtual) throws InvalidParameterException {
        final Intent intent = new Intent(activity, QuadPayCheckoutActivity.class);
        if (virtual) {
            String dest = QuadPayURLBuilder.buildVirtualCheckoutURL(details);
            Log.d("SDKExample", "start activity dest: " + dest);
            intent.putExtra(QuadPay.QUADPAY_ACTIVITY_EXTRA, dest);
            activity.startActivityForResult(intent, QuadPay.QUADPAY_ACTIVITY_REQUEST_CODE);
        } else {
            String dest = QuadPayURLBuilder.buildCheckoutURL(details);
            Log.d("SDKExample", "start activity dest: " + dest);
            intent.putExtra(QuadPay.QUADPAY_ACTIVITY_EXTRA, dest);
            activity.startActivityForResult(intent, QuadPay.QUADPAY_ACTIVITY_REQUEST_CODE);
        }
    }

    static void start(@NonNull Activity activity, @NonNull QuadPayCheckoutDetails details) {
        start(activity, details, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("SDKExample", "QCA onActivityResult - " + requestCode + " " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void receiveMessage(String message) {
        Log.d("SDKExample", "Message received by Activity - " + message);
        Intent data = new Intent();
        data.putExtra(QuadPay.QUADPAY_ACTIVITY_EXTRA, message);
        this.setResult(RESULT_OK, data);
        this.finish();
    }

    @Override
    void initViews() {
        webView.addJavascriptInterface(new QuadPayJSInterface(this), "quadpay");
        webView.setWebViewClient(new QuadPayWebViewClient(this) {
        });
    }

    @Override
    public void onWebViewError(@NonNull String error) {
        Log.d("SDKExample", "onWebViewError " + error);
        Log.d("SDKExample", "QCBA finishWithError " + error);
        final Intent intent = new Intent();
        // TODO: fix strings
        intent.putExtra("CO_ERROR", error.toString());
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
