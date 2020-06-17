package com.quadpay.quadpay;

import org.json.JSONException;
import org.json.JSONObject;

class CheckoutCancelledMessage extends QuadPayJSInterfaceMessage {
    static final String messageType = "CheckoutCancelledMessage";
    String reason;
    CheckoutCancelledMessage(JSONObject jsonMessage) throws JSONException {
        if (jsonMessage.has("reason")) {
            reason = jsonMessage.getString("reason");
        }
    }
}
