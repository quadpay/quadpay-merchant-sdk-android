package com.quadpay.quadpay.PaymentWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;

import com.quadpay.quadpay.R;

public class QuadPayPaymentWidget extends LinearLayout {

    public QuadPayPaymentWidget(@NonNull Context context, AttributeSet attrs) {
        super(context,attrs);
        setOrientation(LinearLayout.VERTICAL);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.QuadPayWidget);
        PaymentWidget(context,attributes);
    }

    private void PaymentWidget(Context context, TypedArray attributes) {


        PaymentWidget paymentWidget = new PaymentWidget(context, attributes);
        addView(paymentWidget);
        String hideTimelineText = attributes.getString(R.styleable.QuadPayWidget_hideTimeline);
        Boolean hideTimeline = hideTimelineText != null && hideTimelineText.equalsIgnoreCase("true");
        if(!hideTimeline) {
            String color = attributes.getString(R.styleable.QuadPayWidget_timelineColor);
            String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
            String amount = attributes.getString(R.styleable.QuadPayWidget_amount);
            Timelapse timelapse = new Timelapse(context, color,merchantId , amount);
            addView(timelapse);
        }
    }
}
