package com.quadpay.quadpay.Widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.quadpay.quadpay.Constants;
import com.quadpay.quadpay.Network.UriUtility;
import com.quadpay.quadpay.Network.WidgetData;
import com.quadpay.quadpay.GatewayClient;
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



@SuppressLint({"AppCompatCustomView", "ViewConstructor"})
public class QuadPayWidgetTextView extends TextView {

    private final SpannableStringBuilder sb = new SpannableStringBuilder();
    private SpannableString amountString = null;
    private final String amount;
    private final Integer min;
    private final Integer max;
    private final String widgetVerbiage;
    private final String widgetTextMin;
    private final String widgetTextMax;
    private Boolean hasFees;
    private final Boolean subTextLayout;
    private final String logoOption;
    private final String displayMode;
    private final String widgetSubtext;
    private final String logoSize;
    private final Drawable info;
    private final String size;
    private final String alignment;
    private final String priceColor;
    private Float maxFee = 0f;
    private String bankPartner;

    private final String merchantId;
    private final String isMFPPMerchant;
    private final String learnMoreUrl;
    private final String minModal;


    private String widgetText = null;
    private ArrayList<WidgetData.FeeTier> feeTiers = null;

    public QuadPayWidgetTextView(Context context, TypedArray attributes) {
        super(context);
        amount = attributes.getString(R.styleable.QuadPayWidget_amount);
        min = attributes.getInteger(R.styleable.QuadPayWidget_min, Constants.min);
        max = attributes.getInteger(R.styleable.QuadPayWidget_max, Constants.max);
        widgetVerbiage = context.getString(R.string.widget_text);
        widgetTextMin = context.getString(R.string.widget_text_min);
        widgetTextMax = context.getString(R.string.widget_text_max);
        String subtextLayout = attributes.getString(R.styleable.QuadPayWidget_subTextLayout);
        subTextLayout = subtextLayout != null && subtextLayout.equals("true");
        logoOption = attributes.getString(R.styleable.QuadPayWidget_logoOption);
        displayMode = attributes.getString(R.styleable.QuadPayWidget_displayMode);
        widgetSubtext = context.getString(R.string.widget_subtext);
        logoSize = attributes.getString(R.styleable.QuadPayWidget_logoSize);
        info = ContextCompat.getDrawable(context,R.drawable.info);
        size = attributes.getString(R.styleable.QuadPayWidget_size);
        alignment = attributes.getString(R.styleable.QuadPayWidget_alignment);
        priceColor = attributes.getString(R.styleable.QuadPayWidget_priceColor);

        merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        learnMoreUrl =UriUtility.Scheme.addIfMissing(attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl));
        minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);


        setTextColor(Color.BLACK);
        setLineSpacing(1f,1.2f);
        setPadding(0,30,0, 30);
        SetWidgetLayout(context);
    }

    public static float getLogoSize(String size){
        float sizePercentage = Float.parseFloat((size.replace("%","")));
        if(sizePercentage<=90f){
            sizePercentage = 90/100f;
        }
        else if(sizePercentage>=120f){
            sizePercentage=120/100f;
        }else{
            sizePercentage =sizePercentage/100f;
        }
        return sizePercentage;
    }

    public Float getTextSizeFromAttributes(String size){
        float sizePercentage = Float.parseFloat(size.replace("%",""));

        if(sizePercentage<90.0) {
            sizePercentage = 90f/100;
        }else if(sizePercentage>120.0)
        {
            sizePercentage =120f/100;
        }else{
            sizePercentage = sizePercentage/100;
        }
        return sizePercentage;
    }

    public void setTextViewSize(String size){

        if(size!=null) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * (size.equals("") ? 1 : getTextSizeFromAttributes(size)));
        }
    }

    public static Drawable getLogo(String logoOption, Context context){
        Drawable logo;

        switch (logoOption) {
            case "secondary":
                logo = ContextCompat.getDrawable(context, R.drawable.secondary_logo);
                break;
            case "secondary-light":
                logo = ContextCompat.getDrawable(context, R.drawable.secondary_light);
                break;
            case "black-white":
                logo = ContextCompat.getDrawable(context, R.drawable.black_white);
                break;
            default:
                logo = ContextCompat.getDrawable(context, R.drawable.zip_logo);
                break;
        }
        return logo;
    }

    public void SetDrawableBounds(Drawable drawable){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent);
        float drawableWidth = drawableHeight * aspectRatio;
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }

    public void SetDrawableBoundsZip(Drawable drawable, String logoSize){
        float drawableHeight = drawable.getIntrinsicHeight() * (logoSize.equals("")? 1:getLogoSize(logoSize));
        float drawableWidth = drawable.getIntrinsicWidth() * (logoSize.equals("") ? 1:getLogoSize(logoSize));
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }

    public void setWidgetAlignment(String alignment){
        if(alignment!=null) {
            switch (alignment) {
                case "left":
                    setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    break;
                case "right":
                    setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    break;
                case "center":
                    setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                    break;
            }
        }else{
            setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
    }

    public void setAmountStyle(){
        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        amountString.setSpan(boldStyle,0,amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if(priceColor!=null) {
            try{
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(priceColor));
                amountString.setSpan(colorSpan, 0, amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }catch(Exception ignored){

            }
        }
    }

    public static Drawable Logo(String logoOption, Context context){
        Drawable logo;
        if (logoOption != null) {
            logo = getLogo(logoOption,context);
        } else {
            logo = ContextCompat.getDrawable(context,R.drawable.zip_logo);
        }
        return logo;
    }

    public void WidgetLogoFirst(SpannableStringBuilder sb , VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, String widgetText, String size, String isMFPPMerchant, String learnMoreUrl, String minModal, String merchantId, String alignment){

        sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString ss =new SpannableString(" "+ widgetText );
        sb.append(ss);
        sb.append(amountString);
        sb.append(" ");
        sb.append("Info",imageSpanInfo,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                merchantId,
                learnMoreUrl,
                isMFPPMerchant,
                minModal,
                hasFees,
                bankPartner) {

        },sb.length()-3,sb.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        setWidgetAlignment(alignment);
        setTextViewSize(size);
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void WidgetDefault(SpannableStringBuilder sb, VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, String widgetText, String size, Boolean subTextLayout, String widgetSubText, String isMFPPMerchant, String learnMoreUrl, String minModal, String merchantId, String alignment) {
         if (subTextLayout) {
            SpannableString ss = new SpannableString(widgetSubText);
            sb.append(ss);
            sb.append(" with ");
            sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.append(" ");
            sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                    merchantId,
                    learnMoreUrl,
                    isMFPPMerchant,
                    minModal,
                    hasFees,
                    bankPartner) {

            }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.append("\n").append(widgetText.replace("4 payments ", ""));
            sb.append(amountString);
        } else {
            SpannableString ss = new SpannableString("or "+ widgetText);
            sb.append(ss);
            sb.append(amountString);
            sb.append(" with ");
            sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.append(" ");
            sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                    merchantId,
                    learnMoreUrl,
                    isMFPPMerchant,
                    minModal,
                    hasFees,
                    bankPartner) {

            }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        setWidgetAlignment(alignment);
        setTextViewSize(size);
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void SetAmount() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setMinimumFractionDigits(2);

        if (amount == null || amount.equals("")) {

            amountString = new SpannableString(" $" + decimalFormat.format(min));

        } else {
            if (Float.parseFloat(amount) < min)
            {
                amountString = new SpannableString(" $" + decimalFormat.format(min));
            } else if (Float.parseFloat(amount) > max) {
                amountString = new SpannableString(" $" + decimalFormat.format(max));
            } else {
                decimalFormat.setMinimumFractionDigits(2);
                float instalment = (Float.parseFloat(amount)+ maxFee) / 4;
                amountString = new SpannableString(" $" +decimalFormat.format(instalment));
            }
        }
    }

    public void SetWidgetText() {
        if (amount == null || amount.equals("")) {
            widgetText = widgetTextMin;

        } else {
            if (Float.parseFloat(amount) < min)
            {
                widgetText = widgetTextMin;
            } else if (Float.parseFloat(amount) > max) {
                widgetText = widgetTextMax;
            } else {
                widgetText = widgetVerbiage;
            }
        }
    }

    public void SetWidgetLayout(Context context) {
        SetDrawableBounds(info);
        VerticalImageSpan imageSpanInfo = new VerticalImageSpan(info,false);
        VerticalImageSpan imageSpanLogo;
        Drawable logo = Logo(logoOption,context);
        if(logoSize!=null){
            SetDrawableBoundsZip(logo, logoSize);
        }
        else{
            SetDrawableBounds(logo);
        }

        if(logoOption!=null) {
            switch (logoOption) {
                case "secondary":
                case "secondary-light":
                    imageSpanLogo = new VerticalImageSpan(logo, false);
                    break;
                default:
                    imageSpanLogo = new VerticalImageSpan(logo, true);
            }
        }else{
            imageSpanLogo = new VerticalImageSpan(logo, true);
        }

        setSingleLine(false);
        SetWidgetText();


        if (merchantId != null) {
            getWidgetData(imageSpanLogo,imageSpanInfo, context);
        }
        else{
            setLayout(imageSpanLogo,imageSpanInfo);
        }
    }

    private void setLayout(VerticalImageSpan imageSpanLogo, VerticalImageSpan imageSpanInfo) {
        SetAmount();
        setAmountStyle();
        if (displayMode != null && !subTextLayout) {
            if ("logoFirst".equals(displayMode)) {
                WidgetLogoFirst(sb, imageSpanLogo, amountString, imageSpanInfo, widgetText, size, isMFPPMerchant, learnMoreUrl, minModal, merchantId, alignment);
            } else {
                WidgetDefault(sb, imageSpanLogo, amountString, imageSpanInfo, widgetText, size, false, widgetSubtext, isMFPPMerchant, learnMoreUrl, minModal, merchantId, alignment);
            }
        } else {
            WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,size,subTextLayout,widgetSubtext,isMFPPMerchant,learnMoreUrl,minModal, merchantId,alignment);
        }
    }

    private void getWidgetData(VerticalImageSpan imageSpanLogo, VerticalImageSpan imageSpanInfo, Context context){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("merchantId", merchantId);
        parameters.put("websiteUrl","");
        parameters.put("environmentName","");
        parameters.put("userId","");


        Call<WidgetData> call = GatewayClient.getInstance(context).getWidgetDataApi().getWidgetData(parameters);
        call.enqueue(new Callback<WidgetData>(){
            @Override
            public void onResponse(@NonNull Call<WidgetData> call , @NonNull Response<WidgetData> response){
                if(!response.isSuccessful()){
                    setLayout(imageSpanLogo,imageSpanInfo);
                    return;
                }

                WidgetData widgetData= response.body();
                if(widgetData == null){
                    setLayout(imageSpanLogo,imageSpanInfo);
                    return;
                }
                feeTiers = widgetData.getFeeTierList();
                float maxTier = 0f;

                bankPartner = widgetData.getBankPartner();

                if(feeTiers!=null && amount != null) {
                    for(WidgetData.FeeTier feeTier : feeTiers){
                        float tierAmount = feeTier.getFeeStartsAt();
                        if(tierAmount <= Float.parseFloat(amount)){
                            if(maxTier < tierAmount){
                                maxTier = tierAmount;
                                maxFee = feeTier.getTotalFeePerOrder();
                            }
                        }
                    }
                }

                hasFees = maxFee != 0f;

                setLayout(imageSpanLogo,imageSpanInfo);

            }

            @Override
            public void onFailure(@NonNull Call<WidgetData>call, @NonNull Throwable t){
                setLayout(imageSpanLogo,imageSpanInfo);
            }
        });

    }
}
