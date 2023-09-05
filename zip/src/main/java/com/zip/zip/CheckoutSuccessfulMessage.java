package com.zip.zip;

import org.json.JSONException;
import org.json.JSONObject;

class CheckoutSuccessfulMessage extends ZipJSInterfaceMessage {
    static final String messageType = "CheckoutSuccessfulMessage";
    String orderId;
    QuadPayCustomer customer;
    CheckoutSuccessfulMessage(JSONObject jsonMessage) throws JSONException {
        JSONObject message = jsonMessage.getJSONObject("message");
        orderId = message.getString("orderId");
        customer = new QuadPayCustomer(message.getJSONObject("customer"));
    }
}
