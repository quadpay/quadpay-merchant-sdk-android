package com.quadpay.quadpay.PaymentWidget;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.quadpay.quadpay.QuadPayInfoSpan;
import com.quadpay.quadpay.R;
import com.quadpay.quadpay.VerticalImageSpan;

@SuppressLint({"AppCompatCustomView", "ViewConstructor"})
public class PaymentWidgetHeader extends TextView{

    private final SpannableStringBuilder sb = new SpannableStringBuilder();
    public PaymentWidgetHeader(Context context, String merchantId, String learnMoreUrl, String isMFPPMerchant, String minModal, Boolean hasFees, String bankPartner) {
        super(context);

        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        CreatePaymentWidgetHeader(context, merchantId, learnMoreUrl, isMFPPMerchant,minModal, hasFees, bankPartner);
    }

    private void CreatePaymentWidgetHeader(Context context, String merchantId, String learnMoreUrl, String isMFPPMerchant, String minModal,
                                    Boolean hasFees, String bankPartner){

        Drawable info = ContextCompat.getDrawable(context,R.drawable.info);
        if(info == null){
            return;
        }
        SetDrawableBounds(info);
        VerticalImageSpan imageSpanInfo = new VerticalImageSpan(info,false);
        PaymentWidgetTitle(imageSpanInfo,merchantId,learnMoreUrl,isMFPPMerchant, minModal, hasFees, bankPartner);

    }

    private void PaymentWidgetTitle(VerticalImageSpan imageSpanInfo, String merchantId, String learnMoreUrl, String isMFPPMerchant, String minModal, Boolean hasFees, String bankPartner) {

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
                minModal,
                hasFees,
                bankPartner) {

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
