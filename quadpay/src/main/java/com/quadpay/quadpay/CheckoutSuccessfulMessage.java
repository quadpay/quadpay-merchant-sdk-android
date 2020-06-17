package com.quadpay.quadpay;

import org.json.JSONException;
import org.json.JSONObject;

class CheckoutSuccessfulMessage extends QuadPayJSInterfaceMessage {
    static final String messageType = "CheckoutSuccessfulMessage";
    String orderId;
    CheckoutSuccessfulMessage(JSONObject jsonMessage) throws JSONException {
        JSONObject message = jsonMessage.getJSONObject("message");
        orderId = message.getString("orderId");
    }
}
