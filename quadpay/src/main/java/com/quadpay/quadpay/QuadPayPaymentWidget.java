package com.quadpay.quadpay;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.FrameLayout;


import androidx.annotation.NonNull;

public class QuadPayPaymentWidget extends FrameLayout {

    public QuadPayPaymentWidget(@NonNull Context context, AttributeSet attrs) {
        super(context,attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.QuadPayWidget);
        PaymentWidget(context,attributes);
    }

    private void PaymentWidget(Context context, TypedArray attributes) {

        WindowManager windowManager =
                (WindowManager)  context.getSystemService(Context.WINDOW_SERVICE);
        PaymentWidget paymentWidget = new PaymentWidget(context, attributes);
        addView(paymentWidget);
        Timelapse timelapse = new Timelapse(context, attributes);
        timelapse.setLayoutParams(new LayoutParams(windowManager.getCurrentWindowMetrics().getBounds().width()-30,300));
        addView(timelapse);
    }
}
