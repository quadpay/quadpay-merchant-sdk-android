package com.zip.zip;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;


public class ZipWidgetActivity extends AppCompatActivity {
    private WebView webView;
    private String html = null;

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.zip_activity_webview);
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,  windowManager.getCurrentWindowMetrics().getBounds().height()*80/100);

        this.webView = this.findViewById(R.id.webview);
        this.webView.getSettings().setAllowFileAccess(true);
        this.webView.getSettings().setJavaScriptEnabled(true);

        this.updateContent();
        this.loadUrl();
    }

    private void loadUrl() {
        Intent intent = this.getIntent();
        String url = intent.getStringExtra("URL");
        if (url == null) {
            this.dismiss();
        } else {
            this.webView.loadDataWithBaseURL(null, html, "text/html","utf-8",null);
        }
    }

    private void dismiss() {
        this.setResult(-1);
        this.finish();
    }

    private void updateContent(){
        Intent intent = this.getIntent();
        String merchantId = intent.getStringExtra("MerchantId");
        String learnMoreUrl = intent.getStringExtra("learnMoreUrl");
        String minModal = intent.getStringExtra("minModal");
        String isMFPPMerchant = intent.getStringExtra("isMFPPMerchant");
        boolean hasFees = intent.getBooleanExtra("hasFees", false);
        String bankPartner = intent.getStringExtra("bankPartner");
        try {
            InputStream inputStream = getApplication().getAssets().open("index.html");
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            html = new String(bufferData,"UTF-8");
            html = html.replace("%merchantId%", merchantId !=null? merchantId: "");
            html = html.replace("%learnMoreUrl%", learnMoreUrl !=null? learnMoreUrl: "");
            html = html.replace("%isMFPPMerchant%", isMFPPMerchant !=null? isMFPPMerchant: "");
            html = html.replace("%minModal%", minModal !=null? minModal: "");
            html = html.replace("%hasFees%", String.valueOf(hasFees));
            html = html.replace("%bankPartner%", bankPartner !=null? bankPartner: Constants.NO_BANK_PARTNER);
            html = html.replace("%QuadPayJSUrl%", BuildConfig.QuadPayJSUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
