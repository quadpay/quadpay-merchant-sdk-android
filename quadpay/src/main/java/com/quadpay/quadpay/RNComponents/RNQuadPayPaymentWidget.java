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

import com.quadpay.quadpay.Network.MerchantConfigResult;
import com.quadpay.quadpay.PaymentWidget.Timelapse;
import com.quadpay.quadpay.QuadPayInfoSpan;
import com.quadpay.quadpay.R;
import com.quadpay.quadpay.Network.MerchantConfigClient;
import com.quadpay.quadpay.VerticalImageSpan;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RNQuadPayPaymentWidget extends LinearLayout {

    private Timelapse timelapse;
    private TextView textView;
    private String merchantId = "";
    private String amount = null;
    private String timelineColor = null;
    private String isMFPPMerchant = "";
    private String learnMoreUrl = "";
    private String minModal = "";
    private Boolean applyGrayLabel = false;
    private Boolean allowedToAddView = false;
    private Drawable info = ContextCompat.getDrawable(getContext(), R.drawable.info);
    private SpannableStringBuilder sb = new SpannableStringBuilder();
    private VerticalImageSpan imageSpanInfo = null;
    private Boolean hideHeader = false;
    private Boolean hideSubtitle = false;
    private Boolean hideTimeline = false;
    private float amountValue;


    public RNQuadPayPaymentWidget(@NonNull Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        amountValue =amount != null ? Float.parseFloat(amount): 0;
        this.textView = new TextView(context);
        this.timelapse = new Timelapse(context, this.timelineColor, false,amountValue, this.textView.getTextSize());
        setWidgetText();
        addView(this.textView);
        addView(this.timelapse);
    }

    private void setWidgetText(){
        this.sb = new SpannableStringBuilder();
        SetDrawableBounds(info);
        imageSpanInfo = new VerticalImageSpan(info,false);
        if(!applyGrayLabel) {
            if(!hideHeader) {
                sb.append("Split your order in 4 easy payments with Zip.");
                StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
                sb.setSpan(boldStyle, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.append("Zip pay", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.setSpan(new QuadPayInfoSpan("file///android_asset/index.html",
                        merchantId,
                        learnMoreUrl,
                        isMFPPMerchant,
                        minModal
                ) {
                }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.setSpan(colorSpan, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.append("\n");
                sb.append("\n");
            }
        }else{
            if(!hideHeader) {
                sb.append("Split your order in 4 easy payments with Welcome Pay (powered by Zip).");
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
                StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
                sb.setSpan(boldStyle, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.setSpan(colorSpan, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                        merchantId,
                        learnMoreUrl,
                        isMFPPMerchant,
                        minModal) {

                }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.append("\n");
            }
        }
        if(!hideSubtitle){
            sb.append("You will be redirected to Zip to complete your order.");
        }

        if(!hideTimeline) {
            this.timelapse.invalidate();
            this.timelapse.init(timelineColor,applyGrayLabel,amountValue, this.textView.getTextSize());
            if(allowedToAddView){
                addView(this.timelapse);
                allowedToAddView = false;
            }
        }else{
            removeView(this.timelapse);
            allowedToAddView = true;
        }

        this.textView.setClickable(true);
        this.textView.setText(sb);

        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
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
            Call<MerchantConfigResult> call = MerchantConfigClient.getInstance().getMerchantConfigApi().getMerchantAssets(this.merchantId);
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
        }else{
            this.learnMoreUrl = "";
        }
        setWidgetText();
    }

    public void setIsMFPPMerchant(String isMFPPMerchant){
        if(isMFPPMerchant !=null) {
            this.isMFPPMerchant = isMFPPMerchant;
        }else{
            this.isMFPPMerchant = "";
        }
        setWidgetText();
    }

    public void setMinModal(String minModal){
        if(minModal != null) {
            this.minModal = minModal;
        }else{
            this.minModal = "";
        }
        setWidgetText();
    }

    public void setHideHeader(String hideHeader){
        if(hideHeader!=null){
            this.hideHeader = hideHeader.equalsIgnoreCase("true") ? true : false;
        }else{
            this.hideHeader = false;
        }
        setWidgetText();
    }

    public void setHideSubtitle(String hideSubtitle){
        if(hideSubtitle!=null){
            this.hideSubtitle = hideSubtitle.equalsIgnoreCase("true") ? true : false;
        }else {
            this.hideSubtitle = false;
        }
        setWidgetText();
    }

    public void setHideTimeline(String hideTimeline){
        if(hideTimeline!=null){
            this.hideTimeline = hideTimeline.equalsIgnoreCase("true") ? true : false;
        }else {
            this.hideTimeline = false;
        }
        setWidgetText();
    }

    public void setTimelineColor(String timelineColor){
        this.timelineColor = timelineColor;
        setWidgetText();
    }

    public void setAmount(String amount){
        if(amount != null && !amount.equals("")){
            this.amount = amount;
        }else{
            this.amount = "0";
        }
        setWidgetText();
    }
}