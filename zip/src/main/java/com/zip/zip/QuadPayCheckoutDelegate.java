package com.zip.zip;

public interface QuadPayCheckoutDelegate {
    void checkoutSuccessful(String orderId, QuadPayCustomer customer);
    void checkoutCancelled(String reason);
    void checkoutError(String error);
}
