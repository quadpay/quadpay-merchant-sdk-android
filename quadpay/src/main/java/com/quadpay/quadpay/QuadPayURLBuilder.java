package com.quadpay.quadpay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;

public class QuadPayURLBuilder {
    static String baseURL() {
        switch (QuadPay.configuration.environment) {
            case SANDBOX:
                return "https://sandbox.gateway.quadpay.xyz";
            case PRODUCTION:
                return "https://gateway.quadpay.com";
            case CI:
                return "https://master.gateway.quadpay.xyz";
            default:
                return "https://sandbox.gateway.quadpay.com";

        }
    }

    private static String field(String name, String value) throws UnsupportedEncodingException {
        if (value != null && value != "") {
            return "&" + name + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        }
        return "";
    }

    private static String assembleParams(QuadPayCheckoutDetails details) throws UnsupportedEncodingException {
        return "MerchantId=" + QuadPay.configuration.merchantId
                + field("order.amount", details.amount)
                + field("merchantReference", details.merchantReference)
                + field("order.email", details.customerEmail)
                + field("order.firstName", details.customerFirstName)
                + field("order.lastName", details.customerLastName)
                + field("order.phone", details.customerPhoneNumber)
                + field("order.billingAddress.line1", details.customerAddressLine1)
                + field("order.billingAddress.line2", details.customerAddressLine2)
                + field("order.billingAddress.city", details.customerCity)
                + field("order.billingAddress.postalCode", details.customerPostalCode)
                + field("order.billingAddress.state", details.customerState)
                + field("order.billingAddress.country", details.customerCountry);
    }

    static String buildCheckoutURL(QuadPayCheckoutDetails details) throws InvalidParameterException {
        try {
            return baseURL() + "/mobile/authorize/?" + assembleParams(details);
        } catch (UnsupportedEncodingException e) {
            throw new InvalidParameterException("Unsupported encoding building url");
        }

    }

    public static String buildVirtualCheckoutURL(QuadPayCheckoutDetails details) throws InvalidParameterException {
        try {
            return baseURL() + "/mobile/virtual/authorize/?" + assembleParams(details);
        } catch (UnsupportedEncodingException e) {
            throw new InvalidParameterException("Unsupported encoding building url");
        }
    }

}
