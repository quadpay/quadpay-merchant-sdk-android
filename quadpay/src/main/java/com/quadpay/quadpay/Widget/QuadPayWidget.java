package com.quadpay.quadpay.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;

import com.quadpay.quadpay.R;
import com.quadpay.quadpay.Widget.QuadPayWidgetTextView;


public class QuadPayWidget extends FrameLayout {


    public QuadPayWidget(@NonNull Context context, AttributeSet attrs) {
        super(context,attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.QuadPayWidget);
        Widget(context,attributes);
    }

    public void Widget(Context context, TypedArray attributes) {
        QuadPayWidgetTextView quadPayWidgetTextView = new QuadPayWidgetTextView(context, attributes);
        addView(quadPayWidgetTextView);
    }
}





