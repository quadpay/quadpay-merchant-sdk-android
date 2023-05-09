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
    private String amount;
    private final Boolean hideHeader;
    private final Boolean hideSubtitle;
    private final Boolean hideTimeline;
    private Boolean applyFee = false;
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
        amount = attributes.getString(R.styleable.QuadPayPaymentWidget_amount);
        String hideHeaderText = attributes.getString(R.styleable.QuadPayPaymentWidget_hideHeader);
        String hideSubtitleText = attributes.getString(R.styleable.QuadPayPaymentWidget_hideSubtitle);
        hideHeader = hideHeaderText != null && hideHeaderText.equalsIgnoreCase("true");
        hideSubtitle = hideSubtitleText != null && hideSubtitleText.equalsIgnoreCase("true");
        hideTimeline = hideTimelineText != null && hideTimelineText.equalsIgnoreCase("true");
        attributes.recycle();
        PaymentWidget(context, merchantId, amount);
    }

    private void PaymentWidget(Context context, String merchantId, String amount) {
        if (merchantId != null) {
            if (amount != null) {
                getWidgetData(merchantId, context);
            } else {
                setLayout(context, merchantId);
            }

        } else {
            setLayout(context, null);
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
            public void onResponse(@NonNull Call<WidgetData> call, @NonNull Response<WidgetData> response) {
                if (!response.isSuccessful()) {

                    setLayout(context, merchantId);
                }

                WidgetData widgetData = response.body();
                assert widgetData != null;
                feeTiers = widgetData.getFeeTierList();

                float maxTier = 0f;

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
                setLayout(context, merchantId);

            }

            @Override
            public void onFailure(@NonNull Call<WidgetData> call, @NonNull Throwable t) {
                setLayout(context, merchantId);
            }
        });
    }

    private void setLayout(Context context, String merchantId) {
        PaymentWidgetHeader paymentWidgetHeader = new PaymentWidgetHeader(context, merchantId, learnMoreUrl, isMFPPMerchant, minModal, false);
        PaymentWidgetSubtitle paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
        Timelapse timelapse = new Timelapse(context, color, false, amount, paymentWidgetHeader.getTextSize());


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
