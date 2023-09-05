package com.zip.zip;

import android.webkit.JavascriptInterface;

public class QuadPayJSInterface {

  private QuadPayJSMessageListener listener;

  public QuadPayJSInterface(QuadPayJSMessageListener listener) {
    this.listener = listener;
  }

  @JavascriptInterface
  public void postMessage(String message) {
//    Log.d("SDKExample", "Looks like I read an object: " + message);
    listener.receiveMessage(message);
    return;
  }

}
