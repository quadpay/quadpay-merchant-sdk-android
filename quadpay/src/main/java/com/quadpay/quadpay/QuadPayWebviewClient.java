package com.quadpay.quadpay;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

class QuadPayWebViewClient extends WebViewClient {

    private final WebViewClientCallbacks callbacks;

    QuadPayWebViewClient(@NonNull WebViewClientCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("SDKExample", "QuadPayWebViewClient.shouldOverrideUrlLoading: " + url);
        return !url.startsWith("http");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReceivedError(WebView view, int errorCode, String description,
                                String failingUrl) {
        Log.d("SDKExample", "QuadPayWebViewClient.onReceivedError: " + description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return;
        }
        callbacks.onWebViewError(errorCode + ", " + description);
    }

    @TargetApi(android.os.Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.d("SDKExample", "QuadPayWebViewClient.onReceivedError: " + error.getDescription().toString());
        if (request.isForMainFrame()) {
            callbacks.onWebViewError(
                    error.getErrorCode() + ", " + error.getDescription().toString());
        }
    }

    @Override
    public void onReceivedHttpAuthRequest (WebView view,
                                           HttpAuthHandler handler,
                                           String host,
                                           String realm) {
        Log.d("SDKExample", "QuadPayWebViewClient.onReceivedHttpAuthRequest");
    }

    @Override
    public void onPageStarted(WebView webview, String url, Bitmap favicon) {
        Log.d("SDKExample", "QuadPayWebViewClient.onPageStarted: " + url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d("SDKExample", "QuadPayWebViewClient.onPageFinished: " + url);
    }

    public interface WebViewClientCallbacks {
        void onWebViewError(@NonNull String error);
    }
}