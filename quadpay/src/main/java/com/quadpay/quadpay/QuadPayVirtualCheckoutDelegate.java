package com.quadpay.quadpay;

public interface QuadPayVirtualCheckoutDelegate {
    void checkoutSuccessful(QuadPayCard card, QuadPayCardholder cardholder, QuadPayCustomer customer, string orderId);
    void checkoutCancelled(String reason);
    void checkoutError(String error);
}
