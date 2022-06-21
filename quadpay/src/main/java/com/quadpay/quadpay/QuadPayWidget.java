package com.quadpay.quadpay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.text.SpannableStringBuilder;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;
 class CenteredImageSpan extends ImageSpan {

    // Extra variables used to redefine the Font Metrics when an ImageSpan is added
    private int initialDescent = 0;
    private int extraSpace = 0;

    public CenteredImageSpan(final Drawable drawable) {
        this(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
    }

    public CenteredImageSpan(final Drawable drawable, final int verticalAlignment) {
        super(drawable, verticalAlignment);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, Paint paint) {
        getDrawable().draw(canvas);
    }

    // Method used to redefined the Font Metrics when an ImageSpan is added
    @Override
    public int getSize(Paint paint, CharSequence text,
                       int start, int end,
                       Paint.FontMetricsInt fm) {
        Drawable d = getCachedDrawable();
        Rect rect = d.getBounds();

        if (fm != null) {
            // Centers the text with the ImageSpan
            if (rect.bottom - (fm.descent - fm.ascent) >= 0) {
                // Stores the initial descent and computes the margin available
                initialDescent = fm.descent;
                extraSpace = rect.bottom - (fm.descent - fm.ascent);
            }

            fm.descent = extraSpace / 2 + initialDescent;
            fm.bottom = fm.descent;

            fm.ascent = -rect.bottom + fm.descent;
            fm.top = fm.ascent;
        }

        return rect.right;
    }

    // Redefined locally because it is a private member from DynamicDrawableSpan
    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<>(d);
        }

        return d;
    }

    private WeakReference<Drawable> mDrawableRef;
}
class QuadPayWidget extends FrameLayout {


    public QuadPayWidget(@NonNull Context context, AttributeSet attrs) {
        super(context);

        TextView textView = new TextView(context);

        Drawable logo = ContextCompat.getDrawable(context, R.drawable.zip_logo);

        float aspectRatio = (float)logo.getIntrinsicWidth() / (float)logo.getIntrinsicHeight();
        TextPaint var25 = textView.getPaint();

        Paint.FontMetrics var17 = var25.getFontMetrics();
        float drawableHeight = var17.descent - var17.ascent;
        float drawableWidth = drawableHeight * aspectRatio;
        logo.setBounds(0, 0, (int)drawableWidth, (int)drawableHeight);


        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        CenteredImageSpan imageSpan = new CenteredImageSpan(logo);

        spannableStringBuilder.append("Zip pay", imageSpan, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(" ");
        //spannableStringBuilder.append("more info", new QuadPayInfoSpan("https://static.afterpay.com/modal/en_US.html"), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        spannableStringBuilder.append("more info", new QuadPayInfoSpan("https://laitangzip.github.io/laitang.widget/"), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        textView.setText(spannableStringBuilder);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        addView(textView);
    }
}

