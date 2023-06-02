package com.quadpay.quadpay.RNComponents;

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
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.quadpay.quadpay.GatewayClient;
import com.quadpay.quadpay.Network.WidgetData;
import com.quadpay.quadpay.QuadPayInfoSpan;
import com.quadpay.quadpay.R;
import com.quadpay.quadpay.VerticalImageSpan;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RNQuadPayWidget extends FrameLayout {

    private final TextView widgetMessage;
    private String min = "35";
    private String max  = "1500";
    private final StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
    private String color = "#000000";
    private String logoOption = "zip";
    private String displayMode = "normal";
    private Float logoSize = 100/100f;
    private String merchantId = "";
    private String isMFPPMerchant = "";
    private String learnMoreUrl = "";
    private String minModal = "";
    private Boolean baseline = false;

    private SpannableString amountString = null;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private final Drawable info = ContextCompat.getDrawable(getContext(), R.drawable.info);
    private String bankPartner;
    private Boolean hasFees = false;
    private float maxFee = 0f;
    private float amount;

    public RNQuadPayWidget(@NonNull Context context) {
        super(context);
        this.widgetMessage = new TextView(context);
        this.widgetMessage.setTextColor(Color.BLACK);
        this.widgetMessage.setLineSpacing(1f,1.2f);
        this.widgetMessage.setPadding(0,30,0,30);
        this.addView(this.widgetMessage);
    }

    private void setWidgetText(){
        customiseAmount();
        Drawable drawableLogo = getLogo();
        SetDrawableBoundsLogo(drawableLogo);
        VerticalImageSpan imageSpanLogo = new VerticalImageSpan(drawableLogo, this.baseline);

        SetDrawableBounds(info);
        VerticalImageSpan imageSpanInfo = new VerticalImageSpan(info, false);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        SpannableString widgetText;
        if (amount == 0){
            widgetText = new SpannableString("4 payments on orders over");
        }else if(amount< Float.parseFloat(min)){
            widgetText = new SpannableString("4 payments on orders over");
        }else if(amount> Float.parseFloat(max)){
            widgetText = new SpannableString("4 payments on orders up to");
        }else{
            widgetText = new SpannableString("4 payments of");
        }

        if (displayMode.equals("logoFirst")) {
            sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    .append(" ")
                    .append(widgetText)
                    .append(" ")
                    .append(amountString);
        } else {
            sb.append("or ")
                    .append(String.valueOf(widgetText))
                    .append(" ")
                    .append(amountString)
                    .append(" with ")
                    .append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        sb.append(" ")
                .append("Zip pay", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new QuadPayInfoSpan("file///android_asset/index.html",
                merchantId,
                learnMoreUrl,
                isMFPPMerchant,
                minModal,
                hasFees,
                bankPartner
        ) {
        }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        this.widgetMessage.setClickable(true);
        this.widgetMessage.setText(sb);
        this.widgetMessage.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setWidgetData(){
        if(amount != 0){
            Map<String, String> parameters = new HashMap<>();
            parameters.put("merchantId", merchantId);
            parameters.put("websiteUrl","");
            parameters.put("environmentName","");
            parameters.put("userId","");

            Call<WidgetData> call = GatewayClient.getInstance(getContext()).getWidgetDataApi().getWidgetData(parameters);
            call.enqueue(new Callback<WidgetData>(){
                @Override
                public void onResponse(@NonNull Call<WidgetData> call , @NonNull Response<WidgetData> response){
                    if(!response.isSuccessful()){
                        setWidgetText();
                        return;
                    }

                    WidgetData widgetData= response.body();
                    if(widgetData == null){
                        setWidgetText();
                        return;
                    }

                    ArrayList<WidgetData.FeeTier> feeTiers = widgetData.getFeeTierList();
                    float maxTier = 0f;
                    maxFee = 0;
                    bankPartner = widgetData.getBankPartner();

                    if(feeTiers!=null) {
                        for(WidgetData.FeeTier feeTier : feeTiers){
                            float tierAmount = feeTier.getFeeStartsAt();
                            if(tierAmount <= amount){
                                if(maxTier < tierAmount){
                                    maxTier = tierAmount;
                                    maxFee = feeTier.getTotalFeePerOrder();
                                }
                            }
                        }
                    }
                    hasFees = maxFee != 0;
                    setWidgetText();
                }

                @Override
                public void onFailure(@NonNull Call<WidgetData>call, @NonNull Throwable t){
                    setWidgetText();
                }
            });
        }
        setWidgetText();
    }

    public void setMerchantId(String merchantId){
        if(merchantId != null){
            this.merchantId = merchantId;
            setWidgetData();
        }else{
            this.merchantId = "";
            setWidgetText();
        }
    }

    public void setAmount(String amount){
        if(amount == null || amount.equals("")){
            this.amount = 0;
        }else{
            this.amount = Float.parseFloat(amount);
        }
        setWidgetData();
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
        float sizePercentage;
        if(size == null || size.equals("")){
            sizePercentage = 100f / 100;
        }else {
            sizePercentage = Float.parseFloat(size.replace("%", ""));

            if (sizePercentage < 100.0) {
                sizePercentage = 100f / 100;
            } else if (sizePercentage > 130.0) {
                sizePercentage = 130f / 100;
            } else {
                sizePercentage = sizePercentage / 100;
            }
        }

        if (size != null) {
            this.widgetMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, 59.0f * (size.equals("") ? 1 : sizePercentage));
        }

    }

    public void setAlignment(String alignment){
        switch(alignment.toLowerCase()){
            case "right":
                this.widgetMessage.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                break;
            case "center":
                this.widgetMessage.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                break;
            default:
                this.widgetMessage.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
    }

    public void setLogoOption(String logoOption){
        if(logoOption ==null || logoOption.equals("")) {
            this.logoOption = "zip";
        }else{
            this.logoOption = logoOption;
        }
        setWidgetText();
    }

    public void SetDrawableBounds(Drawable drawable){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = this.widgetMessage.getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent);
        float drawableWidth = drawableHeight * aspectRatio;
        drawable.setBounds(0,0,(int) drawableWidth,(int) drawableHeight);
    }

    public void SetDrawableBoundsLogo(Drawable drawable){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = this.widgetMessage.getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent)*  logoSize;
        float drawableWidth = drawableHeight * aspectRatio*  logoSize;
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }

    public Drawable getLogo(){
        Drawable logo;
        switch(logoOption) {
            case "secondary":
                logo = ContextCompat.getDrawable(getContext(), R.drawable.secondary_logo);
                this.baseline = false;
                break;
            case "secondary-light":
                logo = ContextCompat.getDrawable(getContext(),R.drawable.secondary_light);
                this.baseline = false;
                break;
            case "black-white":
                logo = ContextCompat.getDrawable(getContext(),R.drawable.black_white);
                this.baseline = true;
                break;
            default:
                logo = ContextCompat.getDrawable(getContext(),R.drawable.zip_logo);
                this.baseline = true;
                break;
        }
        return logo;
    }

    private void customiseAmount(){
        decimalFormat.setMinimumFractionDigits(2);
        float amountValue = amount;
        amountValue += maxFee;
        String currencySymbol = "$";
        if(amountValue == 0 ){
            amountString = new SpannableString(currencySymbol + decimalFormat.format(Float.parseFloat(this.min)));
        }else if (amountValue< Float.parseFloat(this.min)){
            amountString = new SpannableString(currencySymbol + decimalFormat.format(Float.parseFloat(this.min)));
        }else if(amountValue> Float.parseFloat(this.max)){
            amountString = new SpannableString(currencySymbol + decimalFormat.format(Float.parseFloat(this.max)));
        }else{
            amountString = new SpannableString(currencySymbol + decimalFormat.format(amountValue/4));
        }

        amountString.setSpan(boldStyle,0,amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        try{
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(color));
            amountString.setSpan(colorSpan,0,amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }catch(Exception e){
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
            amountString.setSpan(colorSpan,0,amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    public void setMin(String min){
        if(min==null || min.equals("")){
            this.min = "35";
        }else {
            this.min = min;
        }
        setWidgetText();
    }

    public void setMax(String max){
        if(max == null ||max.equals("")){
            this.max = "1500";
        }else {
            this.max = max;
        }
        setWidgetText();
    }

    public void setColorPrice(String colorPrice){
        if(colorPrice == null || colorPrice.equals("")){
            this.color = "#000000";
        }else {
            this.color = colorPrice;
        }
        setWidgetText();
    }

    public void setDisplayMode(String displayMode){
        if(displayMode!=null) {
            if ("logoFirst".equals(displayMode)) {
                this.displayMode = displayMode;
            } else {
                this.displayMode = "normal";
            }
        }else{
            this.displayMode = "normal";
        }

        setWidgetText();
    }

    public void setLogoSize(String logoSize){
        if(logoSize == null || logoSize.equals("")) {
            this.logoSize = 100f/100;

        }else{
            try {
                float sizePercentage = Float.parseFloat(logoSize.replace("%", ""));

                if (sizePercentage <= 90.0) {
                    this.logoSize = 90f / 100;
                } else if (sizePercentage >= 120.0) {
                    this.logoSize = 120f / 100;
                } else {
                    this.logoSize = sizePercentage / 100;
                }
            }catch(Exception e){
                this.logoSize = 100f/100;
            }
        }
        setWidgetText();
    }
}