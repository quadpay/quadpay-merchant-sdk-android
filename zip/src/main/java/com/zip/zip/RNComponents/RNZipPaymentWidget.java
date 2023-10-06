package com.zip.zip.RNComponents;

import android.content.Context;
import android.content.res.Resources;
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
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.content.ContextCompat;
import com.zip.zip.GatewayClient;
import com.zip.zip.Network.UriUtility;
import com.zip.zip.Network.WidgetData;
import com.zip.zip.PaymentWidget.Timelapse;
import com.zip.zip.PaymentWidget.PaymentWidgetSubtitle;
import com.zip.zip.ZipInfoSpan;
import com.zip.zip.R;
import com.zip.zip.VerticalImageSpan;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RNZipPaymentWidget extends LinearLayout {

    private final Timelapse timelapse;
    private final TextView paymentWidgetHeader;
    private final PaymentWidgetSubtitle paymentWidgetSubtitle;
    private final TextView feeTierText;
    private String merchantId = "";
    private String timelineColor = null;
    private String isMFPPMerchant = "";
    private String learnMoreUrl;
    private String minModal = "";
    private final Drawable info = ContextCompat.getDrawable(getContext(), R.drawable.info);
    private int hideSubtitle ;
    private int hideTimeline ;


    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final String PRE_FEE_TEXT = "There may be a $";
    private static final String POST_FEE_TEXT = " finance charge to use Zip.";
    private static final String CHARGE_INCLUDED_TEXT= " This charge is included above.";



    private String bankPartner;
    private float amount;
    private float maxFee = 0f;
    private Boolean hasFees = false;


    public RNZipPaymentWidget(@NonNull Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        float amountValue = 0;
        this.paymentWidgetHeader = new TextView(context);
        this.paymentWidgetHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        this.paymentWidgetHeader.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        this.paymentWidgetHeader.setLineSpacing(0.5f,0.9f);
        this.timelapse = new Timelapse(context, this.timelineColor, amountValue, this.paymentWidgetHeader.getTextSize());
        this.timelapse.setLayoutParams(new Constraints.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels,260));
        this.feeTierText = new TextView(context);
        this.paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
        setWidgetText();

        addView(this.paymentWidgetHeader);
        addView(this.paymentWidgetSubtitle);
        addView(this.timelapse);
        addView(this.feeTierText);
    }

    private void generateFeeText(){
        //Added for fee message
        SpannableStringBuilder sb = new SpannableStringBuilder();
        if(maxFee % 2 == 0){
            int x = Math.round(maxFee);
            sb.append(PRE_FEE_TEXT)
                    .append(String.valueOf(x))
                    .append(POST_FEE_TEXT);
        }else{
            sb.append(PRE_FEE_TEXT)
                    .append(decimalFormat.format(maxFee))
                    .append(POST_FEE_TEXT);
        }
        if(hideTimeline == View.VISIBLE){
            sb.append(CHARGE_INCLUDED_TEXT);
        }

        this.feeTierText.setClickable(true);
        this.feeTierText.setText(sb);
        this.feeTierText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setWidgetText(){
        SpannableStringBuilder sb = new SpannableStringBuilder();

        SetDrawableBounds(info);
        VerticalImageSpan imageSpanInfo = new VerticalImageSpan(info, false);

        sb.append("Split your order in 4 easy payments with Zip.");
        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
        sb.setSpan(boldStyle, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append("Zip pay", imageSpanInfo, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ZipInfoSpan("file///android_asset/index.html",
                merchantId,
                learnMoreUrl,
                isMFPPMerchant,
                minModal,
                hasFees,
                bankPartner
        ) {
        }, sb.length() - 3, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(colorSpan, 0, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.append("\n");


        float amountValue = amount;
        amountValue += maxFee;
        this.timelapse.invalidate();
        this.timelapse.init(timelineColor,amountValue, this.paymentWidgetHeader.getTextSize());
        this.timelapse.setVisibility(View.VISIBLE);

        paymentWidgetSubtitle.setVisibility(hideSubtitle);
        timelapse.setVisibility(hideTimeline);

        this.paymentWidgetHeader.setClickable(true);
        this.paymentWidgetHeader.setText(sb);

        this.paymentWidgetHeader.setMovementMethod(LinkMovementMethod.getInstance());
        generateFeeText();

        if(hasFees) {
            this.feeTierText.setVisibility(View.VISIBLE);
        }else{
            this.feeTierText.setVisibility(View.INVISIBLE);

        }
    }

    private void SetDrawableBounds(Drawable drawable){
        float aspectRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
        TextPaint paint = this.paymentWidgetHeader.getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();

        float drawableHeight = (paintFontMetrics.descent - paintFontMetrics.ascent);
        float drawableWidth = drawableHeight * aspectRatio;
        drawable.setBounds(0,0,(int) drawableWidth,(int) drawableHeight);
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
                        bankPartner = null;
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
                    bankPartner = null;
                    maxFee = 0;
                    setWidgetText();
                }
            });
        }
        setWidgetText();
    }

    public void setLearnMoreUrl(String learnMoreUrl){
        if(!learnMoreUrl.equals("")) {
            this.learnMoreUrl = UriUtility.Scheme.addIfMissing(learnMoreUrl);
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

    public void setHideSubtitle(String hideSubtitle){
        this.hideSubtitle = hideSubtitle != null && hideSubtitle.equalsIgnoreCase("true") ? View.GONE : View.VISIBLE;
        setWidgetText();
    }

    public void setHideTimeline(String hideTimeline){
        this.hideTimeline = hideTimeline != null && hideTimeline.equalsIgnoreCase("true") ? View.GONE : View.VISIBLE;
        setWidgetText();
    }

    public void setTimelineColor(String timelineColor){
        this.timelineColor = timelineColor;
        setWidgetText();
    }

    public void setAmount(String amount){
        if(amount != null && !amount.equals("")){
            this.amount = Float.parseFloat(amount);
        }else{
            this.amount = 0;
        }
        setWidgetData();
    }
}



