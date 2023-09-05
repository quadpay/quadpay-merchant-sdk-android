package com.zip.zip;

public interface ZipVirtualCheckoutDelegate {
    void checkoutSuccessful(QuadPayCard card, QuadPayCardholder cardholder, QuadPayCustomer customer, String orderId);
    void checkoutCancelled(String reason);
    void checkoutError(String error);
}
