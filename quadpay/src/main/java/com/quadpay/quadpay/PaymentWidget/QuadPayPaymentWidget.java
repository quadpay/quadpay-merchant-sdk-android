package com.quadpay.quadpay.PaymentWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;

import com.quadpay.quadpay.Network.GatewayClient;
import com.quadpay.quadpay.R;
import com.quadpay.quadpay.Network.WidgetData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuadPayPaymentWidget extends LinearLayout {
    private ArrayList<WidgetData.FeeTier> feeTiers = null;

    private final String learnMoreUrl;
    private final String isMFPPMerchant;
    private final String minModal;
    private final String color;
    private final int hideHeader;
    private final int hideSubtitle;
    private final int hideTimeline;
    private float amountValue;
    private Float maxFee = 0f;

    public QuadPayPaymentWidget(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.QuadPayPaymentWidget);
        String merchantId = attributes.getString(R.styleable.QuadPayPaymentWidget_merchantId);
        learnMoreUrl = attributes.getString(R.styleable.QuadPayPaymentWidget_learnMoreUrl);
        isMFPPMerchant = attributes.getString(R.styleable.QuadPayPaymentWidget_isMFPPMerchant);
        minModal = attributes.getString(R.styleable.QuadPayPaymentWidget_minModal);
        String hideTimelineText = attributes.getString(R.styleable.QuadPayPaymentWidget_hideTimeline);
        color = attributes.getString(R.styleable.QuadPayPaymentWidget_timelineColor);
        String amount = attributes.getString(R.styleable.QuadPayPaymentWidget_amount);
        amountValue = amount != null ? Float.parseFloat(amount) : 0;
        String hideHeaderText = attributes.getString(R.styleable.QuadPayPaymentWidget_hideHeader);
        String hideSubtitleText = attributes.getString(R.styleable.QuadPayPaymentWidget_hideSubtitle);
        hideHeader = hideHeaderText != null && hideHeaderText.equalsIgnoreCase("true") ? View.GONE : View.VISIBLE;
        hideSubtitle = hideSubtitleText != null && hideSubtitleText.equalsIgnoreCase("true") ? View.GONE : View.VISIBLE;
        hideTimeline = hideTimelineText != null && hideTimelineText.equalsIgnoreCase("true") ? View.GONE : View.VISIBLE;
        attributes.recycle();
        PaymentWidget(context, merchantId);
    }

    private void PaymentWidget(Context context, String merchantId) {
        if (merchantId == null) {
            setLayout(context, null);
            return;
        }

        if (amountValue == 0) {
            setLayout(context, merchantId);
            return;
        }

        getWidgetData(merchantId,context);
    }


    private void getWidgetData(String merchantId, Context context) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("merchantId", merchantId);
        parameters.put("websiteUrl", "");
        parameters.put("environmentName", "");
        parameters.put("userId", "");

        Call<WidgetData> call = GatewayClient.getInstance(context).getWidgetDataApi().getWidgetData(parameters);

        call.enqueue(new Callback<WidgetData>() {
            @Override
            public void onResponse(@NonNull Call<WidgetData> call, @NonNull Response<WidgetData> response) {
                if (!response.isSuccessful()) {
                    setLayout(context, merchantId);
                    return;
                }

                WidgetData widgetData = response.body();
                if(widgetData == null){
                    setLayout(context,merchantId);
                    return;
                }

                feeTiers = widgetData.getFeeTierList();
                float maxTier = 0f;

                if (feeTiers == null) {
                    setLayout(context, merchantId);
                    return;
                }
                for (WidgetData.FeeTier feeTier : feeTiers) {

                    float tierAmount = feeTier.getFeeStartsAt();
                    if (tierAmount <= amountValue) {
                        if (maxTier < tierAmount) {
                            maxTier = tierAmount;
                            maxFee = feeTier.getTotalFeePerOrder();
                        }
                    }
                }

                amountValue += maxFee;
                setLayout(context, merchantId);
            }

            @Override
            public void onFailure(@NonNull Call<WidgetData> call, @NonNull Throwable t) {
                setLayout(context, merchantId);
            }
        });
    }

    private void setLayout(Context context, String merchantId) {
        Boolean hasFees = maxFee != 0f;
        PaymentWidgetHeader paymentWidgetHeader = new PaymentWidgetHeader(context, merchantId, learnMoreUrl, isMFPPMerchant, minModal, hasFees);
        PaymentWidgetSubtitle paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
        Timelapse timelapse = new Timelapse(context, color, false, amountValue, paymentWidgetHeader.getTextSize());


        addView(paymentWidgetHeader);
        addView(paymentWidgetSubtitle);
        addView(timelapse);
        if (maxFee != 0f ) {
            FeeTierText feeTier = new FeeTierText(context, maxFee);
            addView(feeTier);
        }

        paymentWidgetHeader.setVisibility(hideHeader);
        paymentWidgetSubtitle.setVisibility(hideSubtitle);
        timelapse.setVisibility(hideTimeline);
    }
}
