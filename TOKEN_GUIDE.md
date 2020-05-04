
## Overview
Items you will need:

-The QuadPay Android SDK
-The Start Checkout call in your Android app
-An order confirmation endpoint on your server

## Implementation

### The QuadPay Android SDK

- Our SDK is currently only available privately, please inquire with your QuadPay account manager.

### How to start a QuadPay checkout

#### Initialize the QuadPay SDK

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

QuadPay.getInstance().startCheckout(this, "123.45", "unique-order-id", customer);
```

#### Implement the checkout delegate functions

These functions give your application information regarding the result of the QuadPay checkout flow.

##### Register for activity results

```
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (QuadPay.getInstance().handleCheckoutResult(this, requestCode, resultCode, data)) {
        return;
    }
    super.onActivityResult(requestCode, resultCode, data);
}
```

##### Checkout Success

This function returns a token you may exchange to confirm an order has been created.

```
@Override
public void checkoutSuccessful(String token) {
  // Submit the token through the QuadPay token exchange to confirm the order (see below)
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

### Exchanging QuadPay tokens for confirmed orders

As soon as your customer has confirmed their order you must exchange the QuadPay token. Under the default account settings tokens have a duration of 24 hours after which they can no longer be exchanged for orders.

The timing of your shipping can be any time *after* you have exchanged the token.

#### Post the token

See https://docs.quadpay.com/docs/custom-integration-guide#signing-requests for information regarding signing requests

Post the token:

```
curl -X post https://gateway.quadpay.com/checkout/exchange-token
  -H 'content-type: application/json' 
  -H 'X-QP-Signature: [signature]'
  -d '{
    "token": [token],
    "totalOrderAmount": <number>, (optional)
    "taxAmount": <number>, (optional),
    "shippingAmount": <number>, (optional)
  }'
```

An order id will be returned:

```
{
  "orderId": [order id]
}
```

#### Service after confirmation

Once the order has been confirmed and an order id has been received the user should be guided to a success page.

This order id can also be used to update the order using our normal orders integration api at https://docs.quadpay.com/docs/custom-integration-guide

