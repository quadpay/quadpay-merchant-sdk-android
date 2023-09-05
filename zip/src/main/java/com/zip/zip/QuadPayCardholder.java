package com.zip.zip;

import org.json.JSONException;
import org.json.JSONObject;

public class QuadPayCardholder {
    public String firstName;
    public String lastName;
    public String name;
    public String addressLine1;
    public String addressLine2;
    public String city;
    public String state;
    public String postalCode;
    public String country;

    QuadPayCardholder(JSONObject jsonMessage) throws JSONException {
        firstName = jsonMessage.getString("firstName");
        lastName= jsonMessage.getString("lastName");
        name = jsonMessage.getString("name");
        addressLine1 = jsonMessage.getString("address1");
        addressLine2 = jsonMessage.getString("address2");
        city = jsonMessage.getString("city");
        state = jsonMessage.getString("state");
        postalCode = jsonMessage.getString("postalCode");
        country= jsonMessage.getString("country");
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " " + name + " " + addressLine1 + " " + addressLine2 + " " + city + " " + state + " " + postalCode + " " + country;
    }
}
