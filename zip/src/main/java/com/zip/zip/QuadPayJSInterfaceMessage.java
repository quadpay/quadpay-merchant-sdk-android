package com.zip.zip;

import org.json.JSONObject;

class QuadPayJSInterfaceMessage {
    private static final String messageTypeKey = "messageType";

    static QuadPayJSInterfaceMessage createFromData(String data) {
        if (data == null ) {
            throw new IllegalArgumentException();
        }
        try {
            JSONObject jsonMessage = new JSONObject(data); //Convert from string to object, can also use JSONArray
            String messageType = jsonMessage.getString(messageTypeKey);
//            Log.d("SDKExample", "Decoded message of type " + messageType);
            switch (messageType) {
                case CheckoutSuccessfulMessage.messageType:
                    return new CheckoutSuccessfulMessage(jsonMessage);
                case VirtualCheckoutSuccessfulMessage.messageType:
                    return new VirtualCheckoutSuccessfulMessage(jsonMessage);
                case CheckoutCancelledMessage.messageType:
                    return new CheckoutCancelledMessage(jsonMessage);
                default:
                    throw new IllegalArgumentException();
            }
        } catch (Exception ex) {
//            Log.d("SDKExample", "IllegalArgumentException decoding " + ex.getLocalizedMessage());
            throw new IllegalArgumentException();
        }
    }
}
