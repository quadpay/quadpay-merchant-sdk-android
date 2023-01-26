package com.quadpay.quadpay.PaymentWidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.quadpay.quadpay.MerchantConfigResult;
import com.quadpay.quadpay.QuadPayInfoSpan;
import com.quadpay.quadpay.R;
import com.quadpay.quadpay.RetrofitClient;
import com.quadpay.quadpay.VerticalImageSpan;

import java.sql.Time;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("AppCompatCustomView")
public class PaymentWidget extends TextView{

    private SpannableStringBuilder sb = new SpannableStringBuilder();
    public PaymentWidget(Context context, TypedArray attributes) {
        super(context);
        CreatePaymentWidget(context, attributes);
    }

    private void CreatePaymentWidget(Context context, TypedArray attributes){
        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        String learnMoreUrl =attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        String isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        String minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);
        Drawable info = ContextCompat.getDrawable(context,R.drawable.info);
        SetDrawableBounds(info);
        VerticalImageSpan imageSpanInfo = new VerticalImageSpan(info);
        if(merchantId !=null){
            Call<MerchantConfigResult> call = RetrofitClient.getInstance().getMerchantConfigApi().getMerchantAssets(merchantId);
            call.enqueue(new Callback<MerchantConfigResult>() {
                @Override
                public void onResponse(Call<MerchantConfigResult> call, Response<MerchantConfigResult> response) {
                    if(response.isSuccessful()){
                        PaymentWithMerchant(imageSpanInfo,merchantId,learnMoreUrl,isMFPPMerchant, minModal);
                    }else{
                        PaymentWidgetWithOutMerchant(imageSpanInfo,merchantId,learnMoreUrl,isMFPPMerchant, minModal);
                    }
                }

                @Override
                public void onFailure(Call<MerchantConfigResult> call, Throwable t) {
                    PaymentWidgetWithOutMerchant(imageSpanInfo,merchantId,learnMoreUrl,isMFPPMerchant, minModal);
                }
            });
        }else {
            PaymentWidgetWithOutMerchant(imageSpanInfo,merchantId,learnMoreUrl,isMFPPMerchant, minModal);
        }
    }

    private void PaymentWithMerchant(VerticalImageSpan imageSpanInfo, String merchantId, String learnMoreUrl, String isMFPPMerchant, String minModal) {
        sb.append("Split your order in 4 easy payments with Welcome Pay (powered by Zip).");
        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
        sb.setSpan(colorSpan, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(boldStyle,0,sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                merchantId,
                learnMoreUrl,
                isMFPPMerchant,
                minModal) {

        }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append("\n");
        sb.append("You will be redirected to Zip to complete your order.");
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void PaymentWidgetWithOutMerchant(VerticalImageSpan imageSpanInfo, String merchantId, String learnMoreUrl, String isMFPPMerchant, String minModal) {
        sb.append("Split your order in 4 easy payments with Zip.");
        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
        sb.setSpan(colorSpan, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(boldStyle,0,sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                merchantId,
                learnMoreUrl,
                isMFPPMerchant,
                minModal) {

        }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append("\n");
        sb.append("You will be redirected to Zip to complete your order.");
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void SetDrawableBounds(Drawable drawable){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent);
        float drawableWidth = drawableHeight * aspectRatio;
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }
}
