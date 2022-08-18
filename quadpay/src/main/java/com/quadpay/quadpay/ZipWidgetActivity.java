package com.quadpay.quadpay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ZipWidgetActivity extends AppCompatActivity {
    private WebView webView;
    String html = null;

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.quadpay_activity_webview);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View var10001 = this.findViewById(R.id.webview);
        this.webView = ((WebView)var10001);
        this.updateContent();
        this.loadUrl();
    };

    private final void loadUrl() {
        Intent var10000 = this.getIntent();
        String url = var10000.getStringExtra("URL");
        if (url == null) {
            this.dismiss();
        } else {
            WebView infoView = this.webView;
            infoView.loadData(html, "text/html; charset-UTF-8", null);
        }
    }

    private final void dismiss() {
        this.setResult(-1);
        this.finish();
    }

    private void updateContent(){
        Intent var10000 = this.getIntent();
        String merchantId = var10000.getStringExtra("MerchantId");
        String learnMoreUrl = var10000.getStringExtra("learnMoreUrl");
        String minModal = var10000.getStringExtra("minModal");
        String isMFPPMerchant = var10000.getStringExtra("isMFPPMerchant");
        try {
            InputStream inputStream = getApplication().getAssets().open("index.html");
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            html = new String(bufferData,"UTF-8");
            html = html.replace("merchantId=''", merchantId !=null? "merchantId='"+merchantId+"'": "merchantId=''");
            html = html.replace("learnMoreUrl=''", learnMoreUrl !=null? "learnMoreUrl='"+learnMoreUrl+"'": "learnMoreUrl=''");
            html = html.replace("isMFPPMerchant=''", isMFPPMerchant !=null? "isMFPPMerchant='"+isMFPPMerchant+"'": "isMFPPMerchant=''");
            html = html.replace("minModal=''", minModal !=null? "minModal='"+minModal+"'": "minModal=''");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
