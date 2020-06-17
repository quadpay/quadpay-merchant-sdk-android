package com.quadpay.quadpay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

class QuadPayWebView extends WebView {
    private static final String USER_AGENT_PREFIX = "QuadPay-SDK-"
            + BuildConfig.VERSION_NAME;

    public QuadPayWebView(Context context) {
        this(context, null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public QuadPayWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("SDKExample", "QuadPayWebviewLoaded");
        final String userAgent = USER_AGENT_PREFIX + " " + getSettings().getUserAgentString();
        getSettings().setUserAgentString(userAgent);
        clearCache(true);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setSupportMultipleWindows(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setVerticalScrollBarEnabled(false);
    }
}