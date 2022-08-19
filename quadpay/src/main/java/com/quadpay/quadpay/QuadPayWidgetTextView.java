package com.quadpay.quadpay;

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
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;

public class QuadPayWidgetTextView extends androidx.appcompat.widget.AppCompatTextView {

    private SpannableStringBuilder sb = new SpannableStringBuilder();
    private SpannableString amountString = null;
    private String widgetText = null;

    public QuadPayWidgetTextView(Context context, TypedArray attributes, Boolean result) {
        super(context);
        SetWidgetLayout(context,
                attributes,
                result);
    }

    private static float getLogoSize(String size){
        Float sizePercentage = Float.parseFloat((size.replace("%","")));
        if(sizePercentage<=90f){
            sizePercentage = 90/100f;
        }
        else if(sizePercentage>=150f){
            sizePercentage=150/100f;
        }else{
            sizePercentage =sizePercentage/100f;
        }
        return sizePercentage;
    }

    private Float getTextSizeFromAttributes(String size){
        Float sizePercentage = Float.parseFloat(size.replace("%",""));

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

    private void setTextViewSize(TypedArray attributes){

        String size = attributes.getString(R.styleable.QuadPayWidget_size);

        if(size!=null) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * (size.equals("") ? 100 / 100 : getTextSizeFromAttributes(size)));
        }
    }

    private static Drawable getLogo(String logoOption, Context context){
        Drawable logo=null;

        switch (logoOption) {
            case "secondary":
                logo = ContextCompat.getDrawable(context, R.drawable.secondary_logo);
                break;
            case "secondary-light":
                logo = ContextCompat.getDrawable(context, R.drawable.secondary_light);
                break;
            case "black-white":
                logo = ContextCompat.getDrawable(context, R.drawable.black_white);
        }
        return logo;
    }

    private void SetDrawableBounds(Drawable drawable){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent);
        float drawableWidth = drawableHeight * aspectRatio;
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }

    private void SetDrawableBoundsZip(Drawable drawable, String logoSize){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent)* (logoSize.equals("")? 100/100f:getLogoSize(logoSize));
        float drawableWidth = drawableHeight * aspectRatio* (logoSize.equals("") ? 100/100f:getLogoSize(logoSize));
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }

    private void setWidgetAlignment(TypedArray attributes){
        String alignment = attributes.getString(R.styleable.QuadPayWidget_alignment);
        if(alignment!=null) {
            switch (alignment) {
                case "left":
                    setGravity(Gravity.LEFT);
                    break;
                case "right":
                    setGravity(Gravity.RIGHT);
                    break;
                case "center":
                    setGravity(Gravity.CENTER);
                    break;
            }
        }
    }

    private void setAmountStyle(TypedArray attributes){
        String priceColor = attributes.getString(R.styleable.QuadPayWidget_priceColor);

        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        amountString.setSpan(boldStyle,0,amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if(priceColor!=null) {
            try{
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(priceColor));
                amountString.setSpan(colorSpan, 0, amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }catch(Exception e){

            }
        }
    }

    private static Drawable Logo(String logoOption, Context context){
        Drawable logo;
        if (logoOption != null) {
            logo = getLogo(logoOption,context);

        } else {
            logo = ContextCompat.getDrawable(context,R.drawable.zip_logo);
        }
        return logo;
    }

    private void WidgetLogoFirst(SpannableStringBuilder sb , VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, String widgetText, TypedArray attributes){
        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        String learnMoreUrl =attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        String isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        String minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);

        sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        SpannableString ss =new SpannableString(" "+ widgetText );
        sb.append(ss);
        sb.append(amountString);
        sb.append(" ");
        sb.append("Info",imageSpanInfo,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                merchantId,
                learnMoreUrl,
                isMFPPMerchant,
                minModal) {

        },sb.length()-3,sb.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        setWidgetAlignment(attributes);
        setTextViewSize(attributes);
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void WidgetDefault(SpannableStringBuilder sb, VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, String widgetText, TypedArray attributes, Boolean subTextLayout,String widgetSubText) {
        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        String learnMoreUrl =attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        String isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        String minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);

        if (subTextLayout) {
            SpannableString ss = new SpannableString(widgetSubText);
            sb.append(ss);
            sb.append(" with ");
            sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.append(" ");
            sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                    merchantId,
                    learnMoreUrl,
                    isMFPPMerchant,
                    minModal) {

            }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.append("\n" + widgetText);
            sb.append(amountString);
        } else {
            SpannableString ss = new SpannableString("or "+ widgetText);
            sb.append(ss);
            sb.append(amountString);
            sb.append(" with ");
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

        setWidgetAlignment(attributes);
        setTextViewSize(attributes);
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void WidgetWithMerchant(SpannableStringBuilder sb, VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, TypedArray attributes, String widget_subtext,VerticalImageSpan imageSpanMerchantLogo) {
        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        String learnMoreUrl =attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        String isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        String minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);


        SpannableString ss = new SpannableString(widget_subtext);
        sb.append(ss);
        sb.append(amountString);
        sb.append("\n");
        sb.append("with ");
        sb.append("Info", imageSpanMerchantLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ss = new SpannableString(" powered by ");
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.6F);
        ss.setSpan(sizeSpan, 0, ss.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        sb.append(ss);
        sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append(" ");
        sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                merchantId,
                learnMoreUrl,
                isMFPPMerchant,
                minModal) {

        }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        setWidgetAlignment(attributes);
        setTextViewSize(attributes);
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void SetAmount(TypedArray attributes) {

        String amount = attributes.getString(R.styleable.QuadPayWidget_amount);
        String min = attributes.getString(R.styleable.QuadPayWidget_min);
        String max = attributes.getString(R.styleable.QuadPayWidget_max);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String minOrder = "35";
        String maxOrder = "1500";
        if (min != null) {
            minOrder = min;
        }

        if (max != null) {
            maxOrder = max;
        }

        if (amount == null || amount.equals("")) {

            amountString = new SpannableString(" $" + minOrder);

        } else {
            if (Float.parseFloat(amount) < Float.parseFloat(minOrder))
            {
                amountString = new SpannableString(" $" + minOrder);
            } else if (Float.parseFloat(amount) > Float.parseFloat(maxOrder)) {
                amountString = new SpannableString(" $" + maxOrder);
            } else {
                amountString = new SpannableString(" $" +decimalFormat.format((Float.parseFloat(amount) / 4)));
            }
        }
    }

    private void SetWidgetText( TypedArray attributes, Context context) {

        String amount = attributes.getString(R.styleable.QuadPayWidget_amount);
        String widget_text = context.getString(R.string.widget_text);
        String widget_text_min = context.getString(R.string.widget_text_min);
        String widget_text_max = context.getString(R.string.widget_text_max);
        String min = attributes.getString(R.styleable.QuadPayWidget_min);
        String max = attributes.getString(R.styleable.QuadPayWidget_max);
        String minOrder = "35";
        String maxOrder = "1500";

        if (min != null) {
            minOrder = min;
        }

        if (max != null) {
            maxOrder = max;
        }

        if (amount == null || amount.equals("")) {
            widgetText = widget_text_min;

        } else {
            if (Float.parseFloat(amount) < Float.parseFloat(minOrder))
            {
                widgetText = widget_text_min;
            } else if (Float.parseFloat(amount) > Float.parseFloat(maxOrder)) {
                widgetText = widget_text_max;
            } else {
                widgetText = widget_text;
            }
        }
    }

    private void SetWidgetLayout(Context context, TypedArray attributes,Boolean result) {
        String subtextLayout = attributes.getString(R.styleable.QuadPayWidget_subTextLayout);
        Boolean subTextLayout = subtextLayout!=null && subtextLayout.equals("true")?true :false;
        String logoOption = attributes.getString(R.styleable.QuadPayWidget_logoOption);
        String displayMode = attributes.getString(R.styleable.QuadPayWidget_displayMode);
        String widget_subtext = context.getString(R.string.widget_subtext);
        String logoSize = attributes.getString(R.styleable.QuadPayWidget_logoSize);

        Drawable info = ContextCompat.getDrawable(context,R.drawable.info);
        SetDrawableBounds(info);
        VerticalImageSpan imageSpanInfo = new VerticalImageSpan(info);

        Drawable logo = Logo(logoOption,context);
        if(logoSize!=null){
            SetDrawableBoundsZip(logo, logoSize);
        }
        else{
            SetDrawableBounds(logo);
        }
        VerticalImageSpan imageSpanLogo = new VerticalImageSpan(logo);

        VerticalImageSpan imageSpanMerchantLogo=null;

        if(result){
            Drawable merchantLogo = ContextCompat.getDrawable(context,R.drawable.welcome_pay);
            SetDrawableBounds(merchantLogo);
            imageSpanMerchantLogo = new VerticalImageSpan(merchantLogo);
        }
        setSingleLine(false);
        SetWidgetText(attributes,context);
        SetAmount(attributes);
        setAmountStyle(attributes);
        if (displayMode != null && !subTextLayout  && !result) {
            switch (displayMode) {
                case "logoFirst":
                    WidgetLogoFirst(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes);
                    break;
                default:
                    WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes,false,widget_subtext);
                    break;
            }
        } else {
            if(result){
                WidgetWithMerchant(sb,imageSpanLogo,amountString,imageSpanInfo,attributes,widget_subtext,imageSpanMerchantLogo);
            }else {
                WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes,subTextLayout,widget_subtext);
            }
        }
    }
}
