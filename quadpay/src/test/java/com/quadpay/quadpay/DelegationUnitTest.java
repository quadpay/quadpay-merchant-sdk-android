package com.quadpay.quadpay;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static android.app.Activity.RESULT_OK;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DelegationUnitTest {
    QuadPayCheckoutDelegate delegate;
    boolean calledSuccess;
    boolean calledCancelled;
    boolean calledError;

    class MockIntent extends Intent {
        String extra;
        String key;

        @Override
        public Intent putExtra(String k, String x) {
            key = k;
            extra = x;
            return this;
        }

        @Override
        public String getStringExtra(String k) {
            if (k == key) {
                return extra;
            }
            return null;
        }
    }

    @Before
    public void initialize() {
        delegate = new QuadPayCheckoutDelegate() {
            @Override
            public void checkoutSuccessful(String orderId, QuadPayCustomer customer) {
                calledSuccess = true;
            }

            @Override
            public void checkoutCancelled(String reason) {
                calledCancelled = true;
            }

            @Override
            public void checkoutError(String error) {
                calledError = true;
            }
        };
    }

    public void clearCallbacks() {
        calledSuccess = false;
        calledCancelled = false;
        calledError = false;
    }

    @Test
    public void when_receive_success_message_callback_fires() {
        clearCallbacks();
        MockIntent data = new MockIntent();
        data.putExtra(QuadPay.QUADPAY_ACTIVITY_EXTRA, "{\"messageType\":\"CheckoutSuccessfulMessage\",\"message\":{\"orderId\":\"1234-1234\",\"customer\":{\"firstName\":\"Quincy\",\"lastName\":\"Payman\",\"address1\":\"240 Meeker Avenue\",\"address2\":\"Apt 35\",\"city\":\"Brooklyn\",\"state\":\"NY\",\"postalCode\":\"11211\",\"country\":\"US\",\"email\":\"paul.sauer+ckkeee@quadpay.com\",\"phoneNumber\":\"+14076901147\"}}}");

        QuadPay.handleQuadPayActivityResults(delegate, QuadPay.QUADPAY_ACTIVITY_REQUEST_CODE, RESULT_OK, data);
        assertTrue(calledSuccess);
        assertFalse(calledCancelled);
        assertFalse(calledError);
    }

    @Test
    public void when_receive_cancel_message_callback_fires() {
        clearCallbacks();
        MockIntent data = new MockIntent();
        data.putExtra(QuadPay.QUADPAY_ACTIVITY_EXTRA, "{\"messageType\":\"CheckoutCancelledMessage\",\"message\":{\"reason\":\"idk\"}}");
        QuadPay.handleQuadPayActivityResults(delegate, QuadPay.QUADPAY_ACTIVITY_REQUEST_CODE, RESULT_OK, data);
        assertFalse(calledSuccess);
        assertTrue(calledCancelled);
        assertFalse(calledError);
    }
}
