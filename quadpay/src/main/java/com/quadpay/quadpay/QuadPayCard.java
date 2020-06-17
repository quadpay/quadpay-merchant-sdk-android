package com.quadpay.quadpay;

import org.json.JSONException;
import org.json.JSONObject;

public class QuadPayCard {
    public String cvc;
    public String number;
    public String expirationMonth;
    public String expirationYear;
    public String brand;

    QuadPayCard(JSONObject jsonMessage) throws JSONException {
        cvc = jsonMessage.getString("cvc");
        number = jsonMessage.getString("number");
        expirationMonth = jsonMessage.getString("expirationMonth");
        expirationYear = jsonMessage.getString("expirationYear");
        brand = jsonMessage.getString("brand");
    }

    @Override
    public String toString() {
        return number + " " + cvc + " " + expirationMonth + " " + expirationYear + " " + brand;
    }
}
