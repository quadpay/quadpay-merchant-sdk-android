package com.quadpay.quadpay.PaymentWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;

import com.quadpay.quadpay.Network.GatewayClient;
import com.quadpay.quadpay.Network.MerchantConfigResult;
import com.quadpay.quadpay.R;
import com.quadpay.quadpay.Network.MerchantConfigClient;
import com.quadpay.quadpay.Network.WidgetData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuadPayPaymentWidget extends LinearLayout {
    private ArrayList<WidgetData.FeeTier> feeTiers = null;

    private String merchantId = null;
    private String learnMoreUrl = null;
    private String isMFPPMerchant = null;
    private String minModal = null;
    private String hideTimelineText = null;
    private String color = null;
    private String amount = null;
    private String hideHeaderText = null;
    private String hideSubtitleText = null;
    private Boolean hideHeader = false;
    private Boolean hideSubtitle = false;
    private Boolean hideTimeline = false;
    private Boolean applyFee = false;
    private Float maxFee = 0f;

    public QuadPayPaymentWidget(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.QuadPayWidget);
        merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        learnMoreUrl = attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        isMFPPMerchant = attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);
        hideTimelineText = attributes.getString(R.styleable.QuadPayWidget_hideTimeline);
        color = attributes.getString(R.styleable.QuadPayWidget_timelineColor);
        amount = attributes.getString(R.styleable.QuadPayWidget_amount);
        hideHeaderText = attributes.getString(R.styleable.QuadPayWidget_hideHeader);
        hideSubtitleText = attributes.getString(R.styleable.QuadPayWidget_hideSubtitle);
        hideHeader = hideHeaderText != null && hideHeaderText.equalsIgnoreCase("true");
        hideSubtitle = hideSubtitleText != null && hideSubtitleText.equalsIgnoreCase("true");
        hideTimeline = hideTimelineText != null && hideTimelineText.equalsIgnoreCase("true");

        PaymentWidget(context, merchantId, amount);
    }

    private void PaymentWidget(Context context, String merchantId, String amount) {
        if (merchantId != null) {
            if (amount != null) {
                getWidgetData(merchantId, context);
            } else {
                setLayout(context, false, merchantId);
            }

        } else {
            setLayout(context, false, null);
        }
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
            public void onResponse(Call<WidgetData> call, Response<WidgetData> response) {
                if (!response.isSuccessful()) {

                    setLayout(context, false, merchantId);
                }

                WidgetData widgetData = response.body();
                feeTiers = widgetData.getFeeTierList();

                Float maxTier = 0f;

                System.out.println(amount);
                if (feeTiers != null) {
                    for (WidgetData.FeeTier feeTier : feeTiers) {
                        applyFee = true;
                        float tierAmount = feeTier.getFeeStartsAt();
                        if (tierAmount <= Float.parseFloat(amount)) {
                            if (maxTier < tierAmount) {
                                maxTier = tierAmount;
                                maxFee = feeTier.getTotalFeePerOrder();
                            }
                        }
                    }

                    amount = String.valueOf((Float.parseFloat(amount) + maxFee));
                }
                setLayout(context, false, merchantId);

            }

            @Override
            public void onFailure(Call<WidgetData> call, Throwable t) {
                setLayout(context, false, merchantId);
            }
        });
    }

    private void setLayout(Context context, Boolean applyGrayLabel, String merchantId) {
        PaymentWidgetHeader paymentWidgetHeader = new PaymentWidgetHeader(context, merchantId, learnMoreUrl, isMFPPMerchant, minModal, applyGrayLabel);
        PaymentWidgetSubtitle paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
        Timelapse timelapse = new Timelapse(context, color, applyGrayLabel, amount, paymentWidgetHeader.getTextSize());


        addView(paymentWidgetHeader);
        addView(paymentWidgetSubtitle);
        addView(timelapse);
        if (applyFee && maxFee != 0f ) {
            FeeTierText feeTier = new FeeTierText(context, maxFee);
            addView(feeTier);
        }
        if(hideHeader)

    {
        paymentWidgetHeader.setVisibility(View.GONE);
    }else

    {
        paymentWidgetHeader.setVisibility(View.VISIBLE);
    }

        if(hideSubtitle)

    {
        paymentWidgetSubtitle.setVisibility(View.GONE);
    }else

    {
        paymentWidgetSubtitle.setVisibility(View.VISIBLE);
    }

        if(hideTimeline)

    {
        timelapse.setVisibility(View.GONE);
    }else

    {
        timelapse.setVisibility(View.VISIBLE);
    }
}
}
