package com.quadpay.quadpay;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class Timelapse extends View {

    Paint paint;
    Boolean skew;

    public Timelapse(Context context, TypedArray attrs){
        super(context);
        init(attrs);
    }

    public void init(TypedArray attrs){
        paint = new Paint();
        String color = attrs.getString(R.styleable.QuadPayWidget_timelineColor);
        String merchantId = attrs.getString(R.styleable.QuadPayWidget_merchantId);

        if(merchantId == null){
            skew = false;
        }else{
            skew = true;
        }
        if(color!=null)
        {
            if(color.equalsIgnoreCase("black")){
                paint.setColor(Color.BLACK);
            }else{
                paint.setColor(Color.parseColor("#AA8FFF"));
            }
        }
        else{
            paint.setColor(Color.parseColor("#AA8FFF"));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if(skew){
            canvas.skew(0.2F, 0F);
        }
        int windowWidth = getRootView().getMeasuredWidth();
        int windowHeight = getRootView().getMeasuredHeight();
        System.out.println("Look here 2 " + windowWidth + ","+ windowHeight);

        canvas.drawRect(0,190,20,210,paint);
        canvas.drawRect(windowWidth/4,190,windowWidth/4+20,210,paint);
        canvas.drawRect(windowWidth/4*2,190,windowWidth/4*2+20,210,paint);
        canvas.drawRect(windowWidth/4*3,190,windowWidth/4*3+20,210,paint);
        canvas.drawLine(0, 200, windowWidth/4*3, 200, paint);

    }
}
