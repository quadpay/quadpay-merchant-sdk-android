package com.zip.zip;

import android.webkit.JavascriptInterface;

public class ZipJSInterface {

  private ZipJSMessageListener listener;

  public ZipJSInterface(ZipJSMessageListener listener) {
    this.listener = listener;
  }

  @JavascriptInterface
  public void postMessage(String message) {
//    Log.d("SDKExample", "Looks like I read an object: " + message);
    listener.receiveMessage(message);
    return;
  }

}
