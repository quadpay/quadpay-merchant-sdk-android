package com.quadpay.quadpay;

import org.json.JSONException;
import org.json.JSONObject;

public class QuadPayCardholder {
    public String name;
    public String addressLine1;
    public String addressLine2;
    public String city;
    public String state;
    public String postalCode;

    QuadPayCardholder(JSONObject jsonMessage) throws JSONException {
        name = jsonMessage.getString("name");
        addressLine1 = jsonMessage.getString("address1");
        addressLine2 = jsonMessage.getString("address2");
        city = jsonMessage.getString("city");
        state = jsonMessage.getString("state");
        postalCode = jsonMessage.getString("postalCode");
    }

    @Override
    public String toString() {
        return name + " " + addressLine1 + " " + addressLine2 + " " + city + " " + state + " " + postalCode;
    }
}
