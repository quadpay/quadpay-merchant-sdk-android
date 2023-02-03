package com.quadpay.quadpay.PaymentWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import com.quadpay.quadpay.R;

import java.text.DecimalFormat;


public class Timelapse extends View {

    Paint paint;
    Paint amountPaint;
    Paint timelineText;
    Boolean skew;
    String orderAmount;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    int extraWidth = 0;
    int sideWidth = 35;

    public Timelapse(Context context, String color, Boolean applyGrayLabel, String amount){
        super(context);
        setMinimumHeight(200);
        init(color, applyGrayLabel, amount);
    }

    public void init(String color, Boolean applyGrayLabel, String amount ){
        paint = new Paint();
        amountPaint = new Paint();
        timelineText = new Paint();
        amountPaint.setColor(Color.BLACK);
        amountPaint.setTextSize(45);
        amountPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        timelineText.setColor(Color.GRAY);
        timelineText.setTextSize(45);
        if(amount == null){
            orderAmount = "0";
        }else{
            decimalFormat.setMinimumFractionDigits(2);
            orderAmount = decimalFormat.format((Float.parseFloat(amount) / 4));
        }
        if(!applyGrayLabel){
            skew = true;
            extraWidth =15;
        }else{
            skew = false;
            extraWidth = 0;
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
            int windowWidth = canvas.getWidth() -60;
            int windowHeight = canvas.getHeight();
            int amountHeight = windowHeight / 3 + 40+ sideWidth;
            int timelineTextHeight = windowHeight / 3 + 85+ sideWidth;
            int skewAdjustment = 0;
            if (skew) {
                skewAdjustment= 10;
            }
            canvas.drawText("$" + orderAmount, 0 + extraWidth -skewAdjustment, amountHeight, amountPaint);
            canvas.drawText("$" + orderAmount, windowWidth / 4+ extraWidth -skewAdjustment, amountHeight, amountPaint);
            canvas.drawText("$" + orderAmount, windowWidth / 4 * 2+extraWidth -skewAdjustment, amountHeight, amountPaint);
            canvas.drawText("$" + orderAmount, windowWidth / 4 * 3+extraWidth -skewAdjustment, amountHeight, amountPaint);
            canvas.drawText("Due today", 0+extraWidth -skewAdjustment, timelineTextHeight, timelineText);
            canvas.drawText("2 weeks", windowWidth / 4+extraWidth-skewAdjustment, timelineTextHeight, timelineText);
            canvas.drawText("4 weeks", windowWidth / 4 * 2+extraWidth-skewAdjustment, timelineTextHeight, timelineText);
            canvas.drawText("6 weeks", windowWidth / 4 * 3+extraWidth-skewAdjustment, timelineTextHeight, timelineText);
            if (skew) {
                canvas.skew(0.2F, 0F);
            }
            paint.setStrokeWidth(4f);
            canvas.drawRect(0 - skewAdjustment, windowHeight / 3 -sideWidth/2 , sideWidth -skewAdjustment, windowHeight / 3 + sideWidth/2, paint);
            canvas.drawRect(windowWidth / 4- skewAdjustment, windowHeight / 3 -sideWidth/2, windowWidth / 4 + sideWidth -skewAdjustment, windowHeight / 3 + sideWidth/2, paint);
            canvas.drawRect(windowWidth / 4 * 2 - skewAdjustment, windowHeight / 3 -sideWidth/2, windowWidth / 4 * 2 + sideWidth -skewAdjustment, windowHeight / 3 + sideWidth/2, paint);
            canvas.drawRect(windowWidth / 4 * 3 - skewAdjustment, windowHeight / 3 -sideWidth/2, windowWidth / 4 * 3 + sideWidth -skewAdjustment, windowHeight / 3 + sideWidth/2, paint);

            //canvas.drawLine(0, ((windowHeight / 2 + sideWidth) + (windowHeight / 2)) / 2, windowWidth / 4 * 3, ((windowHeight / 2 + sideWidth) + (windowHeight / 2)) / 2, paint);
            canvas.drawLine(0, windowHeight / 3, windowWidth / 4 * 3, windowHeight / 3, paint);

        }


}
