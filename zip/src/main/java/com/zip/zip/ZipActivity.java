package com.zip.zip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

abstract class ZipActivity extends AppCompatActivity {

    ViewGroup container;
    WebView webView;
    String checkoutURL;

    abstract void initViews();

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("SDKExample", "QA onActivityResult - " + requestCode + " " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("SDKExample", "Zip.onCreate");

        // Hide the action bar before we create
        this.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if (this.getActionBar() != null) {
            this.getActionBar().hide();
        } else if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.zip_activity_webview);
        container = getWindow().getDecorView().findViewById(android.R.id.content);
        webView = findViewById(R.id.webview);

        initViews();

        checkoutURL = getIntent().getStringExtra(QuadPay.QUADPAY_ACTIVITY_EXTRA);

        Log.d("SDKExample", "Loading URL" + checkoutURL);
        webView.loadUrl(checkoutURL);
    }

    @Override
    protected void onDestroy() {
        Log.d("SDKExample", "QA onDestroy");
        container.removeView(webView);
        webView.removeAllViews();
        webView.clearCache(true);
        webView.destroyDrawingCache();
        webView.clearHistory();
        webView.destroy();
        webView = null;
        super.onDestroy();
    }
}