package com.quadpay.quadpay;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;


public class Utils {
    public static float getLogoSize(String size){
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

    public static Float getTextSize(String size){
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

    public static void setTextViewSize(TextView textView, TypedArray attributes){

        String size = attributes.getString(R.styleable.QuadPayWidget_size);

        if(size!=null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() * (size.equals("") ? 100 / 100 : getTextSize(size)));
        }
    }

    public static Drawable getLogo(String logoOption, Context context){
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

    public static void SetDrawableBounds(Drawable drawable, TextView textView){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = textView.getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent);
        float drawableWidth = drawableHeight * aspectRatio;
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }

    public static void SetDrawableBoundsZip(Drawable drawable, TextView textView, String logoSize){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = textView.getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent)* (logoSize.equals("")? 100/100f:getLogoSize(logoSize));
        float drawableWidth = drawableHeight * aspectRatio* (logoSize.equals("") ? 100/100f:getLogoSize(logoSize));
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }

    public static void setWidgetAlignment(TextView textView, TypedArray attributes){
        String alignment = attributes.getString(R.styleable.QuadPayWidget_alignment);
        if(alignment!=null) {
            switch (alignment) {
                case "left":
                    textView.setGravity(Gravity.LEFT);
                    break;
                case "right":
                    textView.setGravity(Gravity.RIGHT);
                    break;
                case "center":
                    textView.setGravity(Gravity.CENTER);
                    break;
            }
        }
    }

    public static void setAmountStyle(SpannableString spannableString, TypedArray attributes){
        String priceColor = attributes.getString(R.styleable.QuadPayWidget_priceColor);

        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldStyle,0,spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if(priceColor!=null) {
            try{
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(priceColor));
                spannableString.setSpan(colorSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }catch(Exception e){

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

    public static void WidgetLogoFirst(SpannableStringBuilder sb , VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, String widgetText, TypedArray attributes){
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
    }

    public static void WidgetDefault(SpannableStringBuilder sb, VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, String widgetText, TypedArray attributes, Boolean subTextLayout,String widgetSubText) {
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
    }

    public static void WidgetWithMerchant(SpannableStringBuilder sb, VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, TypedArray attributes, String widget_subtext,VerticalImageSpan imageSpanMerchantLogo) {
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
    }

    public static SpannableString SetAmount(TypedArray attributes) {

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

        SpannableString amountString;
        if (amount == null || amount.equals("")) {

            amountString = new SpannableString(" $" + minOrder);
            return amountString;
        } else {
            if (Float.parseFloat(amount) < Float.parseFloat(minOrder))
            {

                amountString = new SpannableString(" $" + minOrder);
                return amountString;
            } else if (Float.parseFloat(amount) > Float.parseFloat(maxOrder)) {

                amountString = new SpannableString(" $" + maxOrder);
                return amountString;
            } else {

                amountString = new SpannableString(" $" +decimalFormat.format((Float.parseFloat(amount) / 4)));
                return amountString;
            }
        }
    }

    public static String SetWidgetText( TypedArray attributes, Context context) {

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
            return widget_text_min;

        } else {
            if (Float.parseFloat(amount) < Float.parseFloat(minOrder))
            {
                return widget_text_min;

            } else if (Float.parseFloat(amount) > Float.parseFloat(maxOrder)) {
                return widget_text_max;

            } else {
                return widget_text;

            }
        }
    }

    public static void SetWidgetLayout(Context context, TextView textView, SpannableStringBuilder sb, SpannableString amountString, String widgetText, TypedArray attributes,Boolean result) {
        String subtextLayout = attributes.getString(R.styleable.QuadPayWidget_subTextLayout);
        Boolean subTextLayout = subtextLayout!=null && subtextLayout.equals("true")?true :false;
        String logoOption = attributes.getString(R.styleable.QuadPayWidget_logoOption);
        String displayMode = attributes.getString(R.styleable.QuadPayWidget_displayMode);
        String widget_subtext = context.getString(R.string.widget_subtext);
        String logoSize = attributes.getString(R.styleable.QuadPayWidget_logoSize);

        Drawable info = ContextCompat.getDrawable(context,R.drawable.info);
        SetDrawableBounds(info,textView);
        VerticalImageSpan imageSpanInfo = new VerticalImageSpan(info);

        Drawable logo = Logo(logoOption,context);
        if(logoSize!=null){
        Utils.SetDrawableBoundsZip(logo,textView, logoSize);
        }
        else{
            Utils.SetDrawableBounds(logo,textView);
        }
        VerticalImageSpan imageSpanLogo = new VerticalImageSpan(logo);

        VerticalImageSpan imageSpanMerchantLogo=null;

        if(result){
            Drawable merchantLogo = ContextCompat.getDrawable(context,R.drawable.welcome_pay);
            Utils.SetDrawableBounds(merchantLogo,textView);
            imageSpanMerchantLogo = new VerticalImageSpan(merchantLogo);
        }

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


class VerticalImageSpan extends ImageSpan{
    public VerticalImageSpan(Drawable drawable) {
        super(drawable);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.descent - fmPaint.ascent;
            int drHeight = rect.bottom - rect.top;
            int centerY = fmPaint.ascent + fontHeight / 2;

            fontMetricsInt.ascent = centerY - drHeight / 2;
            fontMetricsInt.top = fontMetricsInt.ascent;
            fontMetricsInt.bottom = centerY + drHeight / 2;
            fontMetricsInt.descent = fontMetricsInt.bottom;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {

        Drawable drawable = getDrawable();
        canvas.save();
        Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
        int fontHeight = fmPaint.descent - fmPaint.ascent;
        int centerY = y + fmPaint.descent - fontHeight / 2;
        int transY = centerY - (drawable.getBounds().bottom - drawable.getBounds().top) / 2;
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
