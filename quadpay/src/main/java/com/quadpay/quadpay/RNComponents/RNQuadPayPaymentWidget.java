package com.quadpay.quadpay.RNComponents;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.quadpay.quadpay.MerchantConfigResult;
import com.quadpay.quadpay.PaymentWidget.Timelapse;
import com.quadpay.quadpay.QuadPayInfoSpan;
import com.quadpay.quadpay.R;
import com.quadpay.quadpay.RetrofitClient;
import com.quadpay.quadpay.VerticalImageSpan;

import java.sql.Time;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RNQuadPayPaymentWidget extends LinearLayout {

    Timelapse timelapse = null;
    private TextView textView;
    private String merchantId = "";
    private String amount = null;
    private String timelineColor = null;
    private String isMFPPMerchant = "";
    private String learnMoreUrl = "";
    private String minModal = "";
    private Boolean applyGrayLabel = false;
    private VerticalImageSpan imageSpanMerchant = null;
    private Drawable info = ContextCompat.getDrawable(getContext(), R.drawable.info);
    private SpannableStringBuilder sb = new SpannableStringBuilder();
    private VerticalImageSpan imageSpanInfo = null;
    Boolean hideHeader = false;
    Boolean hideSubtitle = false;
    Boolean hideTimeline = false;


    public RNQuadPayPaymentWidget(@NonNull Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        this.setPadding(16,16,16,16);
        this.textView = new TextView(context);
        this.timelapse = new Timelapse(context, null, null,null);
    }

    private void setWidgetText(){
        this.sb = new SpannableStringBuilder();
        SetDrawableBounds(info);
        imageSpanInfo = new VerticalImageSpan(info);
        if(!applyGrayLabel) {
            if(!hideHeader) {
                sb.append("Split your order in 4 easy payments with Welcome Pay (powered by Zip).");
                StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
                sb.setSpan(colorSpan, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.setSpan(boldStyle, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.append(" ");
                sb.append("Zip pay", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.setSpan(new QuadPayInfoSpan("file///android_asset/index.html",
                        merchantId,
                        learnMoreUrl,
                        isMFPPMerchant,
                        minModal
                ) {
                }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }else{
            if(!hideHeader) {
                sb.append("Split your order in 4 easy payments with Zip.");
                StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
                sb.setSpan(colorSpan, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.setSpan(boldStyle, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.append("Info", imageSpanMerchant, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                        merchantId,
                        learnMoreUrl,
                        isMFPPMerchant,
                        minModal) {

                }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }

        }
        if(!hideSubtitle){
        sb.append("\n");
        sb.append("You will be redirected to Zip to complete your order.");
        }
        this.textView.setClickable(true);
        this.textView.setText(sb);
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());

        this.addView(this.textView);

        if(!hideTimeline) {
            this.timelapse = new Timelapse(getContext(),timelineColor,merchantId,amount);
            addView(this.timelapse);
        }

    }

    private void SetDrawableBounds(Drawable drawable){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = this.textView.getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent);
        float drawableWidth = drawableHeight * aspectRatio;
        drawable.setBounds(0,0,(int) drawableWidth,(int) drawableHeight);
    }

    public void setMerchantId(String merchantId){
        if(merchantId != null){
            this.merchantId = merchantId;
            Call<MerchantConfigResult> call = RetrofitClient.getInstance().getMerchantConfigApi().getMerchantAssets(this.merchantId);
            call.enqueue(new Callback<MerchantConfigResult>(){
                public void onResponse(Call<MerchantConfigResult> call, Response<MerchantConfigResult> response){
                    if(response.isSuccessful()){
                        applyGrayLabel = true;
                        setWidgetText();
                    }else{
                        applyGrayLabel = false;
                        setWidgetText();
                    }
                }

                public void onFailure(Call<MerchantConfigResult> call , Throwable t){
                    applyGrayLabel = false;
                    setWidgetText();
                }
            });
        }else{
            this.merchantId = "";
            applyGrayLabel = false;
            setWidgetText();
        }
    }

    public void setLearnMoreUrl(String learnMoreUrl){
        if(learnMoreUrl != null) {
            this.learnMoreUrl = learnMoreUrl;
        }
        setWidgetText();
    }

    public void setIsMFPPMerchant(String isMFPPMerchant){
        if(isMFPPMerchant !=null) {
            this.isMFPPMerchant = isMFPPMerchant;
        }
        setWidgetText();
    }

    public void setMinModal(String minModal){
        if(minModal != null) {
            this.minModal = minModal;
        }
        setWidgetText();
    }

    public void setHideHeader(String hideHeader){
        if(hideHeader!=null){
            this.hideHeader = hideHeader.equalsIgnoreCase("true") ? true : false;
        }
        setWidgetText();
    }

    public void setHideSubtitle(String hideSubtitle){
        if(hideSubtitle!=null){
            this.hideSubtitle = hideSubtitle.equalsIgnoreCase("true") ? true : false;
        }
        setWidgetText();
    }

    public void setHideTimeline(String hideTimeline){
        if(hideTimeline!=null){
            this.hideTimeline = hideTimeline.equalsIgnoreCase("true") ? true : false;
        }
        setWidgetText();
    }

    public void setTimelineColor(String timelineColor){
        this.timelineColor = timelineColor;
        setWidgetText();
    }

    public void setAmount(String amount){
        this.amount = amount;
        setWidgetText();
    }
}
