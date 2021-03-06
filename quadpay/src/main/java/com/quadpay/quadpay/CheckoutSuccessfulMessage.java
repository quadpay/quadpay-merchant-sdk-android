package com.quadpay.quadpay;

import org.json.JSONException;
import org.json.JSONObject;

class CheckoutSuccessfulMessage extends QuadPayJSInterfaceMessage {
    static final String messageType = "CheckoutSuccessfulMessage";
    String orderId;
    QuadPayCustomer customer;
    CheckoutSuccessfulMessage(JSONObject jsonMessage) throws JSONException {
        JSONObject message = jsonMessage.getJSONObject("message");
        orderId = message.getString("orderId");
        customer = new QuadPayCustomer(message.getJSONObject("customer"));
    }
}
