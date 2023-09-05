package com.zip.zip;

import org.json.JSONException;
import org.json.JSONObject;

class VirtualCheckoutSuccessfulMessage extends ZipJSInterfaceMessage {
    static final String messageType = "VirtualCheckoutSuccessfulMessage";
    ZipCard card;
    ZipCardholder cardholder;
    ZipCustomer customer;
    String orderId;
    VirtualCheckoutSuccessfulMessage(JSONObject jsonMessage) throws JSONException {
        JSONObject message = jsonMessage.getJSONObject("message");
        card = new ZipCard(message.getJSONObject("card"));
        cardholder = new ZipCardholder(message.getJSONObject("cardholder"));
        customer = new ZipCustomer(message.getJSONObject("customer"));
        orderId = message.getString("orderId");
    }
}
