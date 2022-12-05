package com.quadpay.quadpay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.style.ImageSpan;
import android.view.View;

public class Timelapse extends View {
    Paint paint = new Paint();

    public Timelapse(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.BLACK);
        canvas.drawRect(0,130,20,150,paint);
        canvas.drawRect(300,130,320,150,paint);
        canvas.drawRect(600,130,620,150,paint);
        canvas.drawRect(900,130,920,150,paint);
        canvas.drawLine(10, 140, 920, 140, paint);


        canvas.drawRect(0,230,20,250,paint);
        canvas.drawRect(290,230,320,250,paint);
        canvas.drawRect(600,230,620,250,paint);
        canvas.drawRect(900,230,920,250,paint);
        canvas.drawLine(10, 240, 920, 240, paint);
    }
}
