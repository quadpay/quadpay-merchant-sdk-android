package com.quadpay.quadpay;

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
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("AppCompatCustomView")
public class QuadPayWidgetTextView extends TextView {

    private SpannableStringBuilder sb = new SpannableStringBuilder();
    private SpannableString amountString = null;
    private String widgetText = null;

    public QuadPayWidgetTextView(Context context, TypedArray attributes) {
        super(context);
        setTextColor(Color.BLACK);
        setLineSpacing(1f,1.2f);
        setPadding(0,30,0, 30);
        SetWidgetLayout(context,
                attributes);
    }

    public static float getLogoSize(String size){
        Float sizePercentage = Float.parseFloat((size.replace("%","")));
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

    public void setTextViewSize(String size){

        if(size!=null) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * (size.equals("") ? 1 : getTextSizeFromAttributes(size)));
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
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

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

    public void setAmountStyle(TypedArray attributes){
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
                minModal) {

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
                    minModal) {

            }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.append("\n" + widgetText.replace("4 payments ", ""));
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
                    minModal) {

            }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        setWidgetAlignment(alignment);
        setTextViewSize(size);
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void WidgetWithMerchant(SpannableStringBuilder sb, VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, String size, String widget_subtext, VerticalImageSpan imageSpanMerchantLogo, String isMFPPMerchant, String learnMoreUrl, String minModal, String merchantId, String alignment ) {
        SpannableString ss = new SpannableString(widget_subtext);
        sb.append(ss);
        sb.append(amountString);
        sb.append("\n");
        sb.append("with ");
        sb.append("Info", imageSpanMerchantLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ss = new SpannableString(" powered by ");
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8F);
        ss.setSpan(sizeSpan, 0, ss.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        sb.append(ss);
        sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(" ");
        sb.append("Info", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new QuadPayInfoSpan("file:///android_asset/index.html",
                merchantId,
                learnMoreUrl,
                isMFPPMerchant,
                minModal) {

        }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        setWidgetAlignment(alignment);
        setTextViewSize(size);
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void SetAmount(TypedArray attributes) {
        String amount = attributes.getString(R.styleable.QuadPayWidget_amount);
        String min = attributes.getString(R.styleable.QuadPayWidget_min);
        String max = attributes.getString(R.styleable.QuadPayWidget_max);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setMinimumFractionDigits(2);
        String minOrder = "35";
        String maxOrder = "1500";

        if (min != null) {
            minOrder = min;
        }

        if (max != null) {
            maxOrder = max;
        }

        if (amount == null || amount.equals("")) {

            amountString = new SpannableString(" $" + decimalFormat.format(Float.parseFloat(minOrder)));

        } else {
            if (Float.parseFloat(amount) < Float.parseFloat(minOrder))
            {
                amountString = new SpannableString(" $" + decimalFormat.format(Float.parseFloat(minOrder)));
            } else if (Float.parseFloat(amount) > Float.parseFloat(maxOrder)) {
                amountString = new SpannableString(" $" + decimalFormat.format(Float.parseFloat(maxOrder)));
            } else {
                decimalFormat.setMinimumFractionDigits(2);
                amountString = new SpannableString(" $" +decimalFormat.format((Float.parseFloat(amount) / 4)));
            }
        }
    }

    public void SetWidgetText( TypedArray attributes, Context context) {
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

    public void SetWidgetLayout(Context context, TypedArray attributes) {
        String subtextLayout = attributes.getString(R.styleable.QuadPayWidget_subTextLayout);
        Boolean subTextLayout = subtextLayout!=null && subtextLayout.equals("true")?true :false;
        String logoOption = attributes.getString(R.styleable.QuadPayWidget_logoOption);
        String displayMode = attributes.getString(R.styleable.QuadPayWidget_displayMode);
        String widget_subtext = context.getString(R.string.widget_subtext);
        String logoSize = attributes.getString(R.styleable.QuadPayWidget_logoSize);
        Drawable info = ContextCompat.getDrawable(context,R.drawable.info);
        String size = attributes.getString(R.styleable.QuadPayWidget_size);
        String alignment = attributes.getString(R.styleable.QuadPayWidget_alignment);
        SetDrawableBounds(info);
        VerticalImageSpan imageSpanInfo = new VerticalImageSpan(info,false);
        VerticalImageSpan imageSpanLogo = null;
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
        SetWidgetText(attributes,context);
        SetAmount(attributes);
        setAmountStyle(attributes);

        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        String isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        String learnMoreUrl =attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        String minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);

        if (merchantId != null) {
            Call<MerchantConfigResult> call = RetrofitClient.getInstance().getMerchantConfigApi().getMerchantAssets(merchantId);
            VerticalImageSpan finalImageSpanLogo = imageSpanLogo;
            call.enqueue(new Callback<MerchantConfigResult>() {
                @Override
                public void onResponse(Call<MerchantConfigResult> call, Response<MerchantConfigResult> response) {
                    //Need to make a call to get the svg from the result below.

                    if(response.isSuccessful()){
                        Drawable merchantLogo = ContextCompat.getDrawable(context,R.drawable.welcome_pay);
                        SetDrawableBounds(merchantLogo);
                        VerticalImageSpan imageSpanMerchantLogo = new VerticalImageSpan(merchantLogo, false);
                        WidgetWithMerchant(sb, finalImageSpanLogo,amountString,imageSpanInfo,size,widget_subtext,imageSpanMerchantLogo,isMFPPMerchant,learnMoreUrl,minModal, merchantId, alignment);
                    }else{
                        if (displayMode != null && !subTextLayout) {
                            switch (displayMode) {
                                case "logoFirst":
                                    WidgetLogoFirst(sb, finalImageSpanLogo,amountString,imageSpanInfo,widgetText,size,isMFPPMerchant,learnMoreUrl,minModal, merchantId,alignment);
                                    break;
                                default:
                                    WidgetDefault(sb, finalImageSpanLogo,amountString,imageSpanInfo,widgetText,size,false,widget_subtext,isMFPPMerchant,learnMoreUrl,minModal, merchantId,alignment);
                                    break;
                            }
                        } else {
                            WidgetDefault(sb, finalImageSpanLogo,amountString,imageSpanInfo,widgetText,size,subTextLayout,widget_subtext,isMFPPMerchant,learnMoreUrl,minModal, merchantId,alignment);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MerchantConfigResult> call, Throwable t) {
                    if (displayMode != null && !subTextLayout) {
                        switch (displayMode) {
                            case "logoFirst":
                                WidgetLogoFirst(sb, finalImageSpanLogo,amountString,imageSpanInfo,widgetText,size,isMFPPMerchant,learnMoreUrl,minModal, merchantId,alignment);
                                break;
                            default:
                                WidgetDefault(sb, finalImageSpanLogo,amountString,imageSpanInfo,widgetText,size,false,widget_subtext,isMFPPMerchant,learnMoreUrl,minModal, merchantId,alignment);
                                break;
                        }
                    } else {
                        WidgetDefault(sb, finalImageSpanLogo,amountString,imageSpanInfo,widgetText,size,subTextLayout,widget_subtext,isMFPPMerchant,learnMoreUrl,minModal, merchantId,alignment);
                    }
                }
            });

        }
        else{
            if (displayMode != null && !subTextLayout) {
                switch (displayMode) {
                    case "logoFirst":
                        WidgetLogoFirst(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,size,isMFPPMerchant,learnMoreUrl,minModal, merchantId,alignment);
                        break;
                    default:
                        WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,size,false,widget_subtext,isMFPPMerchant,learnMoreUrl,minModal, merchantId,alignment);
                        break;
                }
            } else {
                WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,size,subTextLayout,widget_subtext,isMFPPMerchant,learnMoreUrl,minModal, merchantId,alignment);
            }
        }
    }
}
