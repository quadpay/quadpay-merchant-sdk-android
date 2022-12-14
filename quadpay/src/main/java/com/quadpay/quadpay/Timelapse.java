package com.quadpay.quadpay;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import java.text.DecimalFormat;


public class Timelapse extends View {

    Paint paint;
    Paint amountPaint;
    Paint timelineText;
    Boolean skew;
    String amount;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    int extraHeight = 0;
    int extraWidth = 0;

    public Timelapse(Context context, TypedArray attrs){
        super(context);
        setMinimumHeight(100);
        init(attrs);
    }

    public void init(TypedArray attrs){
        paint = new Paint();
        amountPaint = new Paint();
        timelineText = new Paint();
        amountPaint.setColor(Color.BLACK);
        amountPaint.setTextSize(32);
        timelineText.setColor(Color.GRAY);
        timelineText.setTextSize(32);
        String color = attrs.getString(R.styleable.QuadPayWidget_timelineColor);
        String merchantId = attrs.getString(R.styleable.QuadPayWidget_merchantId);
        amount = attrs.getString(R.styleable.QuadPayWidget_amount);
        if(amount == null){
            amount = "0";
        }else{
            amount = decimalFormat.format((Float.parseFloat(amount) / 4));
        }
        if(merchantId == null){
            skew = true;
            extraHeight = -30;
            extraWidth =15;
        }else{
            skew = false;
            extraHeight = 80;
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
            int windowWidth = canvas.getWidth();
            int windowHeight = canvas.getHeight() + extraHeight;
            int amountHeight = windowHeight / 2 + 60;
            int timelineTextHeight = windowHeight / 2 + 90;
            canvas.drawText("$" + amount, 0+extraWidth, amountHeight, amountPaint);
            canvas.drawText("$" + amount, windowWidth / 4+extraWidth, amountHeight, amountPaint);
            canvas.drawText("$" + amount, windowWidth / 4 * 2+extraWidth, amountHeight, amountPaint);
            canvas.drawText("$" + amount, windowWidth / 4 * 3+extraWidth, amountHeight, amountPaint);
            canvas.drawText("Due today", 0+extraWidth, timelineTextHeight, timelineText);
            canvas.drawText("In 2 weeks", windowWidth / 4+extraWidth, timelineTextHeight, timelineText);
            canvas.drawText("In 4 weeks", windowWidth / 4 * 2+extraWidth, timelineTextHeight, timelineText);
            canvas.drawText("In 6 weeks", windowWidth / 4 * 3+extraWidth, timelineTextHeight, timelineText);
            if (skew) {
                canvas.skew(0.1F, 0F);
            }

            System.out.println("Look here 2 " + canvas.getWidth() + "," + canvas.getHeight());

            canvas.drawRect(0, windowHeight / 2, 20, windowHeight / 2 + 20, paint);
            canvas.drawRect(windowWidth / 4, windowHeight / 2, windowWidth / 4 + 20, windowHeight / 2 + 20, paint);
            canvas.drawRect(windowWidth / 4 * 2, windowHeight / 2, windowWidth / 4 * 2 + 20, windowHeight / 2 + 20, paint);
            canvas.drawRect(windowWidth / 4 * 3, windowHeight / 2, windowWidth / 4 * 3 + 20, windowHeight / 2 + 20, paint);
            canvas.drawLine(0, ((windowHeight / 2 + 20) + (windowHeight / 2)) / 2, windowWidth / 4 * 3, ((windowHeight / 2 + 20) + (windowHeight / 2)) / 2, paint);
        }


}
