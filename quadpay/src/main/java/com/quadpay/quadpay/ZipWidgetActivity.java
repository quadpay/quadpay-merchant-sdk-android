package com.quadpay.quadpay;


import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
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
        this.setContentView(R.layout.quadpay_activity_webview);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.webView = (WebView) this.findViewById(R.id.webview);
        this.updateContent();
        this.loadUrl();
    };

    private final void loadUrl() {
        Intent intent = this.getIntent();
        String url = intent.getStringExtra("URL");
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
        Intent intent = this.getIntent();
        String merchantId = intent.getStringExtra("MerchantId");
        String learnMoreUrl = intent.getStringExtra("learnMoreUrl");
        String minModal = intent.getStringExtra("minModal");
        String isMFPPMerchant = intent.getStringExtra("isMFPPMerchant");
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
