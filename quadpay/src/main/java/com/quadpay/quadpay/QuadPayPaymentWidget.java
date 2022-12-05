package com.quadpay.quadpay;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public class QuadPayPaymentWidget extends FrameLayout {

    public QuadPayPaymentWidget(@NonNull Context context, AttributeSet attrs) {
        super(context,attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.QuadPayWidget);
        PaymentWidget(context,attributes);
    }

    private void PaymentWidget(Context context, TypedArray attributes) {
        PaymentWidget paymentWidget = new PaymentWidget(context, attributes);
        addView(paymentWidget);
        Timelapse d = new Timelapse(context);
        addView(d);
    }
}
