package com.quadpay.quadpay.PaymentWidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.quadpay.quadpay.QuadPayInfoSpan;
import com.quadpay.quadpay.R;
import com.quadpay.quadpay.VerticalImageSpan;

@SuppressLint("AppCompatCustomView")
public class PaymentWidgetHeader extends TextView{

    private SpannableStringBuilder sb = new SpannableStringBuilder();
    public PaymentWidgetHeader(Context context, String merchantId, String learnMoreUrl, String isMFPPMerchant, String minModal, Boolean applyGrayLabel) {
        super(context);

        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        CreatePaymentWidgetHeader(context, merchantId, learnMoreUrl, isMFPPMerchant,minModal,applyGrayLabel);
    }

    private void CreatePaymentWidgetHeader(Context context, String merchantId, String learnMoreUrl, String isMFPPMerchant, String minModal, Boolean applyGrayLabel){

        Drawable info = ContextCompat.getDrawable(context,R.drawable.info);
        SetDrawableBounds(info);
        VerticalImageSpan imageSpanInfo = new VerticalImageSpan(info,false);
        if(applyGrayLabel){
            PaymentWithMerchant(imageSpanInfo,merchantId,learnMoreUrl,isMFPPMerchant, minModal);
        }else {
            PaymentWidgetWithOutMerchant(imageSpanInfo,null,learnMoreUrl,isMFPPMerchant, minModal);
        }
    }

    private void PaymentWithMerchant(VerticalImageSpan imageSpanInfo, String merchantId, String learnMoreUrl, String isMFPPMerchant, String minModal) {

        sb.append("Split your order in 4 easy payments with Welcome Pay (powered by Zip).");
        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
        sb.setSpan(colorSpan, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(boldStyle, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                merchantId,
                learnMoreUrl,
                isMFPPMerchant,
                minModal) {

        }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void PaymentWidgetWithOutMerchant(VerticalImageSpan imageSpanInfo, String merchantId, String learnMoreUrl, String isMFPPMerchant, String minModal) {

        sb.append("Split your order in 4 easy payments with Zip.");
        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
        sb.setSpan(colorSpan, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(boldStyle, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                merchantId,
                learnMoreUrl,
                isMFPPMerchant,
                minModal) {

        }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void SetDrawableBounds(Drawable drawable){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent);
        float drawableWidth = drawableHeight * aspectRatio ;
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }
}