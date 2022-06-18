package com.quadpay.quadpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ZipWidgetActivity extends AppCompatActivity {
    private WebView webView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.quadpay_activity_webview);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View var10001 = this.findViewById(R.id.webview);

        this.webView = ((WebView)var10001);

        this.loadUrl();
    }

    private final void loadUrl() {
        Intent var10000 = this.getIntent();
        String url = var10000.getStringExtra("URL");
        if (url == null) {
            this.dismiss();
        } else {
            WebView infoView = this.webView;
            infoView.loadUrl(url);
        }
    }

    private final void dismiss() {
        this.setResult(-1);
        this.finish();
    }
}
