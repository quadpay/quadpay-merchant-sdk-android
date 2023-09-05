package com.zip.zip;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;

public final class QuadPay {

    static QuadPay sharedInstance;
    static String QUADPAY_ACTIVITY_EXTRA = "QUADPAY_ACTIVITY_EXTRA";
    static int QUADPAY_ACTIVITY_REQUEST_CODE = 44444;

    static Configuration configuration;

    private QuadPay() {}

    private QuadPay(@NonNull Configuration configuration) {
        this.configuration = configuration;
    }

    public enum Locale {
        US,
        MX
    }

    public enum Environment {
        SANDBOX,
        PRODUCTION,
        CI
    }

    public static class Constants {
        static String ERROR_READING_MESSAGE = "QuadPaySDK: Error reading message";
        static String ACTIVITY_CANCELLED_MESSAGE = "Activity cancelled";
        static String ACTIVITY_ENDED_MESSAGE = "Activity ended unexpectedly";
    }

    public static void initialize(@NonNull Configuration configuration) {
        sharedInstance = new QuadPay(configuration);
    }

    public static void startCheckout(
            @NonNull Activity activity,
            @NonNull ZipCheckoutDetails details) {
        ZipCheckoutActivity.start(activity, details);
    }

    public static void startVirtualCheckout(
            @NonNull Activity activity,
            @NonNull ZipCheckoutDetails details) {
        ZipCheckoutActivity.start(activity, details, true);
    }

    public static boolean handleQuadPayActivityResults(ZipVirtualCheckoutDelegate delegate, int requestCode, int resultCode, Intent data) {
        if (requestCode == QUADPAY_ACTIVITY_REQUEST_CODE) {
            Log.d("SDKExample", "QuadPayActivity finished - " + requestCode + " " + resultCode);
            switch (resultCode) {
                case RESULT_OK:
                    // Unpack the intent data with message
                    String intentData = data.getStringExtra(QUADPAY_ACTIVITY_EXTRA);
                    try {
                        ZipJSInterfaceMessage message = ZipJSInterfaceMessage.createFromData(intentData);
                        // dispatch callback
                        if (message instanceof VirtualCheckoutSuccessfulMessage) {
                            VirtualCheckoutSuccessfulMessage m = (VirtualCheckoutSuccessfulMessage)message;
                            delegate.checkoutSuccessful(m.card, m.cardholder, m.customer, m.orderId);
                        } else if (message instanceof CheckoutCancelledMessage) {
                            CheckoutCancelledMessage m = (CheckoutCancelledMessage)message;
                            delegate.checkoutCancelled(m.reason);
                        } else {
                            delegate.checkoutCancelled(Constants.ERROR_READING_MESSAGE);
                        }
                    } catch(IllegalArgumentException e) {
                        delegate.checkoutCancelled(Constants.ERROR_READING_MESSAGE);
                    }
                    break;
                case RESULT_CANCELED:
                    delegate.checkoutCancelled(Constants.ACTIVITY_CANCELLED_MESSAGE);
                    break;
                default:
                    delegate.checkoutCancelled(Constants.ACTIVITY_ENDED_MESSAGE);
                    break;
            }
            return true;
        }

        return false;
    }

    public static boolean handleQuadPayActivityResults(ZipCheckoutDelegate delegate, int requestCode, int resultCode, Intent data) {
        if (requestCode == QUADPAY_ACTIVITY_REQUEST_CODE) {
//            Log.d("SDKExample", "QuadPayActivity finished - " + requestCode + " " + resultCode);
            switch (resultCode) {
                case RESULT_OK:
                    // Unpack the intent data with message
                    String intentData = data.getStringExtra(QUADPAY_ACTIVITY_EXTRA);
                    try {
                        ZipJSInterfaceMessage message = ZipJSInterfaceMessage.createFromData(intentData);
                        // dispatch callback
                        if (message instanceof CheckoutSuccessfulMessage) {
                            CheckoutSuccessfulMessage m = (CheckoutSuccessfulMessage)message;
                            delegate.checkoutSuccessful(m.orderId, m.customer);
                        } else if (message instanceof CheckoutCancelledMessage) {
                            CheckoutCancelledMessage m = (CheckoutCancelledMessage)message;
                            delegate.checkoutCancelled(m.reason);
                        } else {
                            delegate.checkoutCancelled(Constants.ERROR_READING_MESSAGE);
                        }
                    } catch(IllegalArgumentException e) {
                        delegate.checkoutCancelled(Constants.ERROR_READING_MESSAGE);
                    }
                    break;
                case RESULT_CANCELED:
                    delegate.checkoutCancelled(Constants.ACTIVITY_CANCELLED_MESSAGE);
                    break;
                default:
                    delegate.checkoutCancelled(Constants.ACTIVITY_ENDED_MESSAGE);
                    break;
            }
            return true;
        }

        return false;
    }

    public static final class Configuration {
        final String merchantId;
        final Environment environment;
        final Locale locale;

        Configuration(Builder builder) {
            this.merchantId = builder.merchantId;
            this.environment = builder.environment;
            this.locale = builder.locale;
        }

        public static final class Builder {
            private final String merchantId;
            private Environment environment;
            private Locale locale;

            public Builder(@NonNull String merchantId) {
                this.merchantId = merchantId;
            }

            public Builder setEnvironment(Environment environment) {
                this.environment = environment;
                return this;
            }

            public Builder setLocale(Locale locale) {
                this.locale = locale;
                return this;
            }

            public Configuration build() {
                if (merchantId == null) {
                    throw new IllegalArgumentException("merchant id was null");
                }
                if (environment == null) {
                    throw new IllegalArgumentException("environment was null");
                }
                if (locale == null) {
                    throw new IllegalArgumentException("locale was null");
                }
                return new Configuration(this);
            }
        }
    }

}