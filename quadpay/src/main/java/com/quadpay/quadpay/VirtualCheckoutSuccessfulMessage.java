package com.quadpay.quadpay;

import org.json.JSONException;
import org.json.JSONObject;

class VirtualCheckoutSuccessfulMessage extends QuadPayJSInterfaceMessage {
    static final String messageType = "VirtualCheckoutSuccessfulMessage";
    QuadPayCard card;
    QuadPayCardholder cardholder;
    QuadPayCustomer customer;
    VirtualCheckoutSuccessfulMessage(JSONObject jsonMessage) throws JSONException {
        JSONObject message = jsonMessage.getJSONObject("message");
        card = new QuadPayCard(message.getJSONObject("card"));
        cardholder = new QuadPayCardholder(message.getJSONObject("cardholder"));
        customer = new QuadPayCustomer(message.getJSONObject("customer"));
    }
}
