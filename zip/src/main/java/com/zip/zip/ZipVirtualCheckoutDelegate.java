package com.zip.zip;

public interface ZipVirtualCheckoutDelegate {
    void checkoutSuccessful(ZipCard card, ZipCardholder cardholder, ZipCustomer customer, String orderId);
    void checkoutCancelled(String reason);
    void checkoutError(String error);
}
