package com.zip.zip.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.zip.zip.R;



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





