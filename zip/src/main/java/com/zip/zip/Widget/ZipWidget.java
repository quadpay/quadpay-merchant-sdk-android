package com.zip.zip.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.zip.zip.R;



public class ZipWidget extends FrameLayout {


    public ZipWidget(@NonNull Context context, AttributeSet attrs) {
        super(context,attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ZipWidget);
        Widget(context,attributes);
    }

    public void Widget(Context context, TypedArray attributes) {
        ZipWidgetTextView zipWidgetTextView = new ZipWidgetTextView(context, attributes);
        addView(zipWidgetTextView);
    }
}





