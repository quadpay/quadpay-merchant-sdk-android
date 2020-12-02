package com.quadpay.quadpay;

public interface QuadPayVirtualCheckoutDelegate {
    void checkoutSuccessful(QuadPayCard card, QuadPayCardholder cardholder, QuadPayCustomer customer);
    void checkoutCancelled(String reason);
    void checkoutError(String error);
}
