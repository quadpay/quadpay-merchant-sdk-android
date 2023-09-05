package com.zip.zip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

class ZipWebView extends WebView {
    private static final String USER_AGENT_PREFIX = "Zip-SDK-"
            + BuildConfig.VERSION_NAME;

    public ZipWebView(Context context) {
        this(context, null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public ZipWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("SDKExample", "ZipWebviewLoaded");
        final String userAgent = USER_AGENT_PREFIX + " " + getSettings().getUserAgentString();
        getSettings().setUserAgentString(userAgent);
        clearCache(true);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setSupportMultipleWindows(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setVerticalScrollBarEnabled(false);
        setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(context);
                WebSettings webSettings = newWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                ((WebView.WebViewTransport)resultMsg.obj).setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }
        });
    }
}

