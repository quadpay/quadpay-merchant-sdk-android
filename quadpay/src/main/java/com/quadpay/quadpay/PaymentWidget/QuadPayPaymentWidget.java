package com.quadpay.quadpay.PaymentWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.quadpay.quadpay.MerchantConfigResult;
import com.quadpay.quadpay.R;
import com.quadpay.quadpay.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuadPayPaymentWidget extends LinearLayout {



    public QuadPayPaymentWidget(@NonNull Context context, AttributeSet attrs) {
        super(context,attrs);
        setOrientation(LinearLayout.VERTICAL);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.QuadPayWidget);
        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        String learnMoreUrl =attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        String isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        String minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);
        String hideTimelineText = attributes.getString(R.styleable.QuadPayWidget_hideTimeline);
        String color = attributes.getString(R.styleable.QuadPayWidget_timelineColor);
        String amount = attributes.getString(R.styleable.QuadPayWidget_amount);
        String hideHeaderText = attributes.getString(R.styleable.QuadPayWidget_hideHeader);
        String hideSubtitleText = attributes.getString(R.styleable.QuadPayWidget_hideSubtitle);
        Boolean hideHeader = hideHeaderText != null && hideHeaderText.equalsIgnoreCase("true");
        Boolean hideSubtitle = hideSubtitleText != null && hideSubtitleText.equalsIgnoreCase("true");
        Boolean hideTimeline = hideTimelineText != null && hideTimelineText.equalsIgnoreCase("true");
        PaymentWidget(context,merchantId,learnMoreUrl,isMFPPMerchant,minModal,color, amount,hideHeader,hideSubtitle,hideTimeline );
    }

    private void PaymentWidget(Context context, String merchantId,String learnMoreUrl,String isMFPPMerchant,String minModal,String color,String amount, Boolean hideHeader,Boolean hideSubtitle,Boolean hideTimeline ) {
        if(merchantId!= null){
            Call<MerchantConfigResult> call = RetrofitClient.getInstance().getMerchantConfigApi().getMerchantAssets(merchantId);
            call.enqueue(new Callback<MerchantConfigResult>() {
                @Override
                public void onResponse(Call<MerchantConfigResult> call, Response<MerchantConfigResult> response) {
                    if(response.isSuccessful()){
                        PaymentWidgetHeader paymentWidgetHeader = new PaymentWidgetHeader(context,merchantId,learnMoreUrl,isMFPPMerchant,minModal,true);
                        PaymentWidgetSubtitle paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
                        Timelapse timelapse = new Timelapse(context, color,true , amount, paymentWidgetHeader.getTextSize());

                        addView(paymentWidgetHeader);
                        addView(paymentWidgetSubtitle);
                        addView(timelapse);
                        if(hideHeader){
                            paymentWidgetHeader.setVisibility(View.GONE);
                        }else{
                            paymentWidgetHeader.setVisibility(View.VISIBLE);
                        }
                        if(hideSubtitle){
                            paymentWidgetSubtitle.setVisibility(View.GONE);
                        }else{
                            paymentWidgetSubtitle.setVisibility(View.VISIBLE);
                        }

                        if(hideTimeline) {
                            timelapse.setVisibility(View.GONE);
                        }

                    }else{
                        PaymentWidgetHeader paymentWidgetHeader = new PaymentWidgetHeader(context, merchantId,learnMoreUrl,isMFPPMerchant,minModal, false);
                        PaymentWidgetSubtitle paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
                        Timelapse timelapse = new Timelapse(context, color,false , amount,paymentWidgetHeader.getTextSize());

                        addView(paymentWidgetHeader);
                        addView(paymentWidgetSubtitle);
                        addView(timelapse);

                        if(hideHeader){
                            paymentWidgetHeader.setVisibility(View.GONE);
                        }

                        if(hideSubtitle){
                            paymentWidgetSubtitle.setVisibility(View.GONE);
                        }

                        if(hideTimeline) {
                            timelapse.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MerchantConfigResult> call, Throwable t) {
                    PaymentWidgetHeader paymentWidgetHeader = new PaymentWidgetHeader(context, merchantId,learnMoreUrl,isMFPPMerchant,minModal, false);
                    PaymentWidgetSubtitle paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
                    Timelapse timelapse = new Timelapse(context, color,false , amount,paymentWidgetHeader.getTextSize());

                    addView(paymentWidgetHeader);
                    addView(paymentWidgetSubtitle);
                    addView(timelapse);

                    if(hideHeader){
                        paymentWidgetHeader.setVisibility(View.GONE);
                    }

                    if(hideSubtitle){
                        paymentWidgetSubtitle.setVisibility(View.GONE);
                    }

                    if(hideTimeline) {
                        timelapse.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            PaymentWidgetHeader paymentWidgetHeader = new PaymentWidgetHeader(context, null,learnMoreUrl,isMFPPMerchant,minModal, false);
            PaymentWidgetSubtitle paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
            Timelapse timelapse = new Timelapse(context, color,false , amount,paymentWidgetHeader.getTextSize());

            addView(paymentWidgetHeader);
            addView(paymentWidgetSubtitle);
            addView(timelapse);

            if(hideHeader){
                paymentWidgetHeader.setVisibility(View.GONE);
            }
            if(hideSubtitle){
                paymentWidgetSubtitle.setVisibility(View.GONE);
            }

            if(hideTimeline) {
              timelapse.setVisibility(View.GONE);
            }
        }
    }
}
