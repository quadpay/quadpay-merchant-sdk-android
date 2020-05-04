
## Overview
Items you will need:

-The QuadPay Android SDK
-The Start Checkout call in your Android app
-An order confirmation endpoint on your server

## Implementation

### The QuadPay Android SDK

- Our SDK is currently only available privately, please inquire with your QuadPay account manager.

### How to start a QuadPay checkout

#### Initialize the QuadPay SDK:

Your merchant id will be provided by your QuadPay account manager.

```
QuadPay.getInstance().initialize(
  "{{merchant_id}}", // merchant public key
  "production", // envrionment
  "US", // locale
);
```

#### Begin the QuadPay checkout flow

Once presented the customer will be shown the QuadPay checkout flow.

```
QuadPayCustomer customer = QuadPayCustomer.builder()
  .setFirstName("QuadPay")
  .setLastName("Customer")
  .setEmail("email@example.com")
  .setPhoneNumber("+15555555555)
  .setAddressLine1("123 Main St")
  .setAddressLine2("10th Floor")
  .setCity("New York")
  .setState("NY")
  .setPostalCode("10003")
  .setCountry("US")
  .build();

QuadPay.getInstance().startVirtualCheckout(this, "123.45", "unique-order-id", customer);
```

#### Implement the checkout delegate functions:

These functions give your application information regarding the result of the QuadPay checkout flow.

##### Register for activity results

```
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (QuadPay.getInstance().handleVirtualCheckoutResult(this, requestCode, resultCode, data)) {
        return;
    }
    super.onActivityResult(requestCode, resultCode, data);
}
```

##### Checkout Success

This function returns a token you may exchange to confirm an order has been created.

```
@Override
public void checkoutSuccessful(QuadPayCard card, QuadPayCardholder cardholder) {
  // Submit the card and cardholder details through your standard payment processor!
}
```

##### Checkout Cancelled

This function is called when the user cancels the QuadPay process or is declined.

```
@Override
public void checkoutCancelled(String reason) {
    // A reason describing why checkout was cancelled is returned
}
```

### Finishing the QuadPay order

Once you have received a virtual card number in a success delegate you may authorize and capture it up to the value provided at the beginning of checkout. The card that is issued is a standard Visa card and all authorize/capture/refund functionality will work as expected.

More information and frequently asked questions are available at https://docs.quadpay.com/docs/virtual-checkout

