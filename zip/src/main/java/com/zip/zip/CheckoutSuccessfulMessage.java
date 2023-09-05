package com.zip.zip;

import org.json.JSONException;
import org.json.JSONObject;

class CheckoutSuccessfulMessage extends ZipJSInterfaceMessage {
    static final String messageType = "CheckoutSuccessfulMessage";
    String orderId;
    ZipCustomer customer;
    CheckoutSuccessfulMessage(JSONObject jsonMessage) throws JSONException {
        JSONObject message = jsonMessage.getJSONObject("message");
        orderId = message.getString("orderId");
        customer = new ZipCustomer(message.getJSONObject("customer"));
    }
}
