package com.zip.zip;

import org.json.JSONException;
import org.json.JSONObject;

class CheckoutCancelledMessage extends ZipJSInterfaceMessage {
    static final String messageType = "CheckoutCancelledMessage";
    String reason;
    CheckoutCancelledMessage(JSONObject jsonMessage) throws JSONException {
        if (jsonMessage.has("reason")) {
            reason = jsonMessage.getString("reason");
        }
    }
}
