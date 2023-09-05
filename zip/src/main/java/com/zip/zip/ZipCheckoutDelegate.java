package com.zip.zip;

public interface ZipCheckoutDelegate {
    void checkoutSuccessful(String orderId, ZipCustomer customer);
    void checkoutCancelled(String reason);
    void checkoutError(String error);
}
