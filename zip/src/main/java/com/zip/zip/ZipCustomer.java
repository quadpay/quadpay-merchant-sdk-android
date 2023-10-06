package com.zip.zip;

import org.json.JSONException;
import org.json.JSONObject;

public class ZipCustomer {
    public String firstName;
    public String lastName;
    public String address1;
    public String address2;
    public String city;
    public String state;
    public String postalCode;
    public String country;
    public String email;
    public String phoneNumber;

    ZipCustomer(JSONObject jsonMessage) throws JSONException {
        firstName = jsonMessage.getString("firstName");
        lastName = jsonMessage.getString("lastName");
        address1 = jsonMessage.getString("address1");
        address2 = jsonMessage.getString("address2");
        city = jsonMessage.getString("city");
        state = jsonMessage.getString("state");
        postalCode = jsonMessage.getString("postalCode");
        country = jsonMessage.getString("country");
        email = jsonMessage.getString("email");
        phoneNumber = jsonMessage.getString("phoneNumber");
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " " + address1 + " " + address2 + " " + city + " " + state + " " + postalCode + " " + country + " " + email + " " + phoneNumber;
    }
}
