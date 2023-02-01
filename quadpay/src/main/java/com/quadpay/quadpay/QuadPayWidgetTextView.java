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
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("AppCompatCustomView")
public class QuadPayWidgetTextView extends TextView {

    private SpannableStringBuilder sb = new SpannableStringBuilder();
    private SpannableString amountString = null;
    private String widgetText = null;
    private StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
    private ForegroundColorSpan colorSpanBlack = new ForegroundColorSpan(Color.BLACK);


    public QuadPayWidgetTextView(Context context, TypedArray attributes) {
        super(context);
        SetWidgetLayout(context,
                attributes);
    }

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

    public void appendWith(SpannableStringBuilder stringBuilder){
        SpannableString withString = new SpannableString(" with ");
        ForegroundColorSpan colorSpanBlack2 = new ForegroundColorSpan(Color.BLACK);
        withString.setSpan(colorSpanBlack2, 0 , withString.length(),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        stringBuilder.append(withString);
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

    public void setTextViewSize(TypedArray attributes){

        String size = attributes.getString(R.styleable.QuadPayWidget_size);

        if(size!=null) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * (size.equals("") ? 100 / 100 : getTextSizeFromAttributes(size)));
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

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent)* (logoSize.equals("")? 100/100f:getLogoSize(logoSize));
        float drawableWidth = drawableHeight * aspectRatio* (logoSize.equals("") ? 100/100f:getLogoSize(logoSize));
        drawable.setBounds(0, 0, (int) drawableWidth, (int) drawableHeight);
    }

    public void setWidgetAlignment(TypedArray attributes){
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

    public void setAmountStyle(TypedArray attributes){
        String priceColor = attributes.getString(R.styleable.QuadPayWidget_priceColor);

        amountString.setSpan(boldStyle,0,amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if(priceColor!=null) {
            try{
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(priceColor));
                amountString.setSpan(colorSpan, 0, amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }catch(Exception e){

            }
        }else{
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
            amountString.setSpan(colorSpan, 0, amountString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
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

    public void WidgetLogoFirst(SpannableStringBuilder sb , VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, String widgetText, TypedArray attributes){
        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        String learnMoreUrl =attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        String isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        String minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);
        // add zip logo to the string builder
        sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        SpannableString ss =new SpannableString(" "+ widgetText );
        sb.append(ss);
        sb.setSpan(colorSpanBlack, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append(amountString);
        sb.append(" ");
        // add the info icon to the string builder
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

    public void WidgetDefault(SpannableStringBuilder sb, VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, String widgetText, TypedArray attributes, Boolean subTextLayout,String widgetSubText) {
        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        String learnMoreUrl =attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        String isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        String minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);

        if (subTextLayout) {
            SpannableString ss = new SpannableString(widgetSubText);
            sb.append(ss);
            sb.setSpan(colorSpanBlack, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


            appendWith(sb);
            // add zip logo to the string builder
            sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.append(" ");
            // add the info icon to the string builder
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

            sb.setSpan(colorSpanBlack, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.append(amountString);
            // add with text in the widget
            appendWith(sb);
            // add zip logo to the string builder
            sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.append(" ");
            // add the info icon to the string builder
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

    public void WidgetWithMerchant(SpannableStringBuilder sb, VerticalImageSpan imageSpanLogo, SpannableString amountString, VerticalImageSpan imageSpanInfo, TypedArray attributes, String widget_subtext,VerticalImageSpan imageSpanMerchantLogo) {
        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        String learnMoreUrl =attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        String isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        String minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);
        SpannableString withString = new SpannableString(" with ");
        withString.setSpan(colorSpanBlack, 0, withString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        // add widget text
        SpannableString ss = new SpannableString(widget_subtext);
        sb.append(ss);
        sb.setSpan(colorSpanBlack, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append(amountString);
        sb.append("\n");
        // add with text in the widget
        appendWith(sb);
        sb.append("Info", imageSpanMerchantLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        //powered by text
        ss = new SpannableString(" powered by ");
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.6F);
        ss.setSpan(sizeSpan, 0, ss.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        // add powered by text to the string builder
        sb.append(ss);
        // add zip logo to the string builder
        sb.append("Zip pay", imageSpanLogo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append(" ");
        // add the info icon to the string builder
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

    public void SetAmount(TypedArray attributes) {
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


        setSingleLine(false);
        SetWidgetText(attributes,context);
        SetAmount(attributes);
        setAmountStyle(attributes);

        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        if (merchantId != null) {
            Call<MerchantConfigResult> call = RetrofitClient.getInstance().getMerchantConfigApi().getMerchantAssets(merchantId);
            call.enqueue(new Callback<MerchantConfigResult>() {
                @Override
                public void onResponse(Call<MerchantConfigResult> call, Response<MerchantConfigResult> response) {
                    //Need to make a call to get the svg from the result below.

                    if(response.isSuccessful()){
                        Drawable merchantLogo = ContextCompat.getDrawable(context,R.drawable.welcome_pay);
                        SetDrawableBounds(merchantLogo);
                        VerticalImageSpan imageSpanMerchantLogo = new VerticalImageSpan(merchantLogo);
                        WidgetWithMerchant(sb,imageSpanLogo,amountString,imageSpanInfo,attributes,widget_subtext,imageSpanMerchantLogo);
                    }else{
                        if (displayMode != null && !subTextLayout) {
                            switch (displayMode) {
                                case "logoFirst":
                                    WidgetLogoFirst(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes);
                                    break;
                                default:
                                    WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes,false,widget_subtext);
                                    break;
                            }
                        } else {
                            WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes,subTextLayout,widget_subtext);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MerchantConfigResult> call, Throwable t) {
                    if (displayMode != null && !subTextLayout) {
                        switch (displayMode) {
                            case "logoFirst":
                                WidgetLogoFirst(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes);
                                break;
                            default:
                                WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes,false,widget_subtext);
                                break;
                        }
                    } else {
                        WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes,subTextLayout,widget_subtext);
                    }
                }
            });

        }
        else{
            if (displayMode != null && !subTextLayout) {
                switch (displayMode) {
                    case "logoFirst":
                        WidgetLogoFirst(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes);
                        break;
                    default:
                        WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes,false,widget_subtext);
                        break;
                }
            } else {
                WidgetDefault(sb,imageSpanLogo,amountString,imageSpanInfo,widgetText,attributes,subTextLayout,widget_subtext);
            }
        }
    }
}
