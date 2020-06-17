package com.quadpay.quadpay;

public class QuadPayURLBuilder {
    static String baseURL() {
        switch (QuadPay.configuration.environment) {
            case SANDBOX:
                return "https://sandbox.gateway.quadpay.xyz";
            case PRODUCTION:
                return "https://api.gateway.quadpay.xyz";
            case CI:
                return "https://master.gateway.quadpay.xyz";
            default:
                return "sandbox.gateway.quadpay.com";

        }
    }

    private static String assembleParams(QuadPayCheckoutDetails details) {
        return "MerchantId=" + QuadPay.configuration.merchantId
                + "&order.Amount=" + details.amount
                + "&customer.firstName=" + details.customerFirstName;
    }

    static String buildCheckoutURL(QuadPayCheckoutDetails details) {
        return baseURL() + "/mobile/authorize/?" + assembleParams(details);
    }

    public static String buildVirtualCheckoutURL(QuadPayCheckoutDetails details) {
        return baseURL() + "/mobile/virtual/authorize/?" + assembleParams(details);
    }

}
