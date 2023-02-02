package com.quadpay.quadpay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RNQuadPayWidget extends FrameLayout {

    private TextView textView;
    private String min = "35";
    private String max  = "1500";
    private String currencySymbol = "$";
    private String amount = "";
    private StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
    private String color = "#000000";
    private String logoOption = "zip";
    private String displayMode = "normal";
    private Float logoSize = 100/100f;
    private String merchantId = "";
    private String isMFPPMerchant = "";
    private String learnMoreUrl = "";
    private String minModal = "";
    private Boolean applyGrayLabel = false;

    private SpannableStringBuilder sb = new SpannableStringBuilder();
    private SpannableString amountString = null;
    private SpannableString widgetText = null;
    private SpannableString poweredBy = null;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private VerticalImageSpan imageSpanLogo = null;
    private VerticalImageSpan imageSpanInfo = null;
    private VerticalImageSpan imageSpanMerchant = null;
    private Drawable info = ContextCompat.getDrawable(getContext(),R.drawable.info);
    private Drawable grayLabel = ContextCompat.getDrawable(getContext(),R.drawable.welcome_pay);

    public RNQuadPayWidget(@NonNull Context context) {
        super(context);
        this.setPadding(16,16,16,16);

        this.textView = new TextView(context);
        this.addView(this.textView);
    }

    private void setWidgetText(){
        Drawable drawableLogo = getLogo();
        SetDrawableBoundsLogo(drawableLogo);
        imageSpanLogo = new VerticalImageSpan(drawableLogo);

        SetDrawableBoundsLogo(grayLabel);
        imageSpanMerchant = new VerticalImageSpan(grayLabel);

        SetDrawableBounds(info);
        imageSpanInfo = new VerticalImageSpan(info);
        this.sb = new SpannableStringBuilder();
        if (amount== null || amount.equals("")){
            widgetText = new SpannableString("4 payments on order over");
        }else if(Float.parseFloat(amount)< Float.parseFloat(min)){
            widgetText = new SpannableString("4 payments on order over");
        }else if(Float.parseFloat(amount)> Float.parseFloat(max)){
            widgetText = new SpannableString("4 payments on order up to");
        }else{
            widgetText = new SpannableString("4 payments of");
        }
        if(!applyGrayLabel) {
            if (displayMode.equals("logoFirst")) {
                sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.append(" ");
                sb.append(widgetText);
                sb.append(" ");
                sb.append(amountString);
            } else {
                sb.append("or " + widgetText);
                sb.append(" ");
                sb.append(amountString);
                sb.append(" with ");
                sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            sb.append(" ");
            sb.append("Zip pay", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.setSpan(new QuadPayInfoSpan("file///android_asset/index.html",
                    merchantId,
                    learnMoreUrl,
                    isMFPPMerchant,
                    minModal
            ) {
            }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }else{
            sb.append("or " + widgetText);
            sb.append(" ");
            sb.append(amountString);
            sb.append("\n");
            sb.append("with ");
            sb.append("Info", imageSpanMerchant, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            poweredBy = new SpannableString(" powered by ");
            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8F);
            poweredBy.setSpan(sizeSpan, 0, poweredBy.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

            sb.append(poweredBy);
            sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.append(" ");
            sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                    merchantId,
                    learnMoreUrl,
                    isMFPPMerchant,
                    minModal) {

            }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        }
        this.textView.setClickable(true);
        this.textView.setText(sb);
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
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

    public void setSize(String size){
        Float sizePercentage = Float.parseFloat(size.replace("%",""));

        if(sizePercentage<100.0) {
            sizePercentage = 100f/100;
        }else if(sizePercentage>150.0)
        {
            sizePercentage =150f/100;
        }else{
            sizePercentage = sizePercentage/100;
        }

        this.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 59.0f * (size.equals("") ? 100 / 100 : sizePercentage));
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setAlignment(String alignment){
        switch(alignment.toLowerCase()){
            case "left":
                this.textView.setGravity(Gravity.LEFT);
                break;
            case "right":
                this.textView.setGravity(Gravity.RIGHT);
                break;
            case "center":
                this.textView.setGravity(Gravity.CENTER);
                break;
        }
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setLogoOption(String logoOption){
        if(logoOption !=null) {
            this.logoOption = logoOption;
        }else{
            this.logoOption = "zip";
        }
        setWidgetText();
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void SetDrawableBounds(Drawable drawable){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = this.textView.getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent);
        float drawableWidth = drawableHeight * aspectRatio;
        drawable.setBounds(0,0,(int) drawableWidth,(int) drawableHeight);
    }

    public void SetDrawableBoundsLogo(Drawable drawable){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = this.textView.getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent)*  logoSize;
        float drawableWidth = drawableHeight * aspectRatio*  logoSize;
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }

    public Drawable getLogo(){
        Drawable logo = null;
        switch(logoOption) {
            case "secondary":
                logo = ContextCompat.getDrawable(getContext(), R.drawable.secondary_logo);
                break;
            case "secondary-light":
                logo = ContextCompat.getDrawable(getContext(),R.drawable.secondary_light);
                break;
            case "black-white":
                logo = ContextCompat.getDrawable(getContext(),R.drawable.black_white);
                break;
            default:
                logo = ContextCompat.getDrawable(getContext(),R.drawable.zip_logo);
                break;
        }
        return logo;
    }

    public void setAmount(String amount){
        this.amount = amount;
        if(amount== null|| amount.equals("")){
            amountString = new SpannableString(currencySymbol + decimalFormat.format(Float.parseFloat(this.min)));
        }else if (Float.parseFloat(amount)< Float.parseFloat(this.min)){
            amountString = new SpannableString(currencySymbol + decimalFormat.format(Float.parseFloat(this.min)));
        }else if(Float.parseFloat(amount)> Float.parseFloat(this.max)){
            amountString = new SpannableString(currencySymbol + decimalFormat.format(Float.parseFloat(this.max)));
        }else{
            amountString = new SpannableString(currencySymbol + decimalFormat.format(Float.parseFloat(amount)/4));
        }

        amountString.setSpan(boldStyle,0,amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(color));
        amountString.setSpan(colorSpan,0,amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        setWidgetText();
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setMin(String min){
        this.min = min;
        setAmount(this.amount);
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setMax(String max){
        this.max = max;
        setAmount(this.amount);
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setColorPrice(String colorPrice){
        this.color = colorPrice;
        setAmount(this.amount);
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setDisplayMode(String displayMode){
        if(displayMode!=null) {
            switch (displayMode) {
                case "logoFirst":
                    this.displayMode = displayMode;
                    break;
                default:
                    this.displayMode = "normal";
                    break;
            }
        }else{
            this.displayMode = "normal";
        }

        setWidgetText();
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setLogoSize(String logoSize){
        if(logoSize != null) {
            Float sizePercentage = Float.parseFloat(logoSize.replace("%", ""));

            if (sizePercentage < 100.0) {
                this.logoSize = 100f / 100;
            } else if (sizePercentage > 150.0) {
                this.logoSize = 120f / 100;
            } else {
                this.logoSize = sizePercentage / 100;
            }
        }else{
            this.logoSize = 100f/100;
        }
        setWidgetText();
    }
}