package com.quadpay.quadpay.PaymentWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;

import com.quadpay.quadpay.MerchantConfigResult;
import com.quadpay.quadpay.R;
import com.quadpay.quadpay.RetrofitClient;
import com.quadpay.quadpay.WidgetData;
import com.quadpay.quadpay.WidgetDataApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuadPayPaymentWidget extends LinearLayout {
    private WidgetDataApi widgetDataApi;
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
    private  Boolean hideHeader = false;
    private Boolean hideSubtitle = false;
    private Boolean hideTimeline = false;

    public QuadPayPaymentWidget(@NonNull Context context, AttributeSet attrs) {
        super(context,attrs);
        setOrientation(LinearLayout.VERTICAL);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.QuadPayWidget);
        merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        learnMoreUrl =attributes.getString(R.styleable.QuadPayWidget_learnMoreUrl);
        isMFPPMerchant =attributes.getString(R.styleable.QuadPayWidget_isMFPPMerchant);
        minModal = attributes.getString(R.styleable.QuadPayWidget_minModal);
        hideTimelineText = attributes.getString(R.styleable.QuadPayWidget_hideTimeline);
        color = attributes.getString(R.styleable.QuadPayWidget_timelineColor);
        amount = attributes.getString(R.styleable.QuadPayWidget_amount);
        hideHeaderText = attributes.getString(R.styleable.QuadPayWidget_hideHeader);
        hideSubtitleText = attributes.getString(R.styleable.QuadPayWidget_hideSubtitle);
        hideHeader = hideHeaderText != null && hideHeaderText.equalsIgnoreCase("true");
        hideSubtitle = hideSubtitleText != null && hideSubtitleText.equalsIgnoreCase("true");
        hideTimeline = hideTimelineText != null && hideTimelineText.equalsIgnoreCase("true");

        PaymentWidget(context,merchantId,learnMoreUrl,isMFPPMerchant,minModal,color, amount,hideHeader,hideSubtitle,hideTimeline);
    }

    private void PaymentWidget(Context context, String merchantId,String learnMoreUrl,String isMFPPMerchant,String minModal,String color,String amount, Boolean hideHeader,Boolean hideSubtitle,Boolean hideTimeline ) {
        if(merchantId!= null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://gateway.dev.us.zip.co/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            widgetDataApi = retrofit.create(WidgetDataApi.class);
            getWidgetData(merchantId, context);

//            Call<MerchantConfigResult> call = RetrofitClient.getInstance().getMerchantConfigApi().getMerchantAssets(merchantId);
//            call.enqueue(new Callback<MerchantConfigResult>() {
//                @Override
//                public void onResponse(Call<MerchantConfigResult> call, Response<MerchantConfigResult> response) {
//                    if(response.isSuccessful()){
//                        PaymentWidgetHeader paymentWidgetHeader = new PaymentWidgetHeader(context,merchantId,learnMoreUrl,isMFPPMerchant,minModal,true);
//                        PaymentWidgetSubtitle paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
//                        Timelapse timelapse = new Timelapse(context, color,true , amount, paymentWidgetHeader.getTextSize());
//
//                        addView(paymentWidgetHeader);
//                        addView(paymentWidgetSubtitle);
//                        addView(timelapse);
//                        if(hideHeader){
//                            paymentWidgetHeader.setVisibility(View.GONE);
//                        }else{
//                            paymentWidgetHeader.setVisibility(View.VISIBLE);
//                        }
//                        if(hideSubtitle){
//                            paymentWidgetSubtitle.setVisibility(View.GONE);
//                        }else{
//                            paymentWidgetSubtitle.setVisibility(View.VISIBLE);
//                        }
//
//                        if(hideTimeline) {
//                            timelapse.setVisibility(View.GONE);
//                        }
//
//                    }else{
//                        PaymentWidgetHeader paymentWidgetHeader = new PaymentWidgetHeader(context, merchantId,learnMoreUrl,isMFPPMerchant,minModal, false);
//                        PaymentWidgetSubtitle paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
//                        Timelapse timelapse = new Timelapse(context, color,false , amount,paymentWidgetHeader.getTextSize());
//
//                        addView(paymentWidgetHeader);
//                        addView(paymentWidgetSubtitle);
//                        addView(timelapse);
//
//                        if(hideHeader){
//                            paymentWidgetHeader.setVisibility(View.GONE);
//                        }
//
//                        if(hideSubtitle){
//                            paymentWidgetSubtitle.setVisibility(View.GONE);
//                        }
//
//                        if(hideTimeline) {
//                            timelapse.setVisibility(View.GONE);
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<MerchantConfigResult> call, Throwable t) {
//                    PaymentWidgetHeader paymentWidgetHeader = new PaymentWidgetHeader(context, merchantId,learnMoreUrl,isMFPPMerchant,minModal, false);
//                    PaymentWidgetSubtitle paymentWidgetSubtitle = new PaymentWidgetSubtitle(context);
//                    Timelapse timelapse = new Timelapse(context, color,false , amount,paymentWidgetHeader.getTextSize());
//
//                    addView(paymentWidgetHeader);
//                    addView(paymentWidgetSubtitle);
//                    addView(timelapse);
//
//                    if(hideHeader){
//                        paymentWidgetHeader.setVisibility(View.GONE);
//                    }
//
//                    if(hideSubtitle){
//                        paymentWidgetSubtitle.setVisibility(View.GONE);
//                    }
//
//                    if(hideTimeline) {
//                        timelapse.setVisibility(View.GONE);
//                    }
//                }
//            });
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

    private void getMerchantConfig(Context context){
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
    }

    private void getWidgetData(String merchantId, Context context){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("merchantId", merchantId);
        parameters.put("websiteUrl","");
        parameters.put("environmentName","");
        parameters.put("userId","");

        Call<WidgetData> call = widgetDataApi.getWidgetData(parameters);

        call.enqueue(new Callback<WidgetData>(){
            @Override
            public void onResponse(Call<WidgetData> call , Response<WidgetData> response){
                if(!response.isSuccessful()){
                    //do something#
                    getMerchantConfig(context);
                }

                WidgetData widgetData= response.body();
                feeTiers = widgetData.getFeeTierList();

                Float maxTier = 0f;
                Float maxFee = 0f;
                if(feeTiers!=null) {
                    for(WidgetData.FeeTier feeTier : feeTiers){
                        float tierAmount = feeTier.getFeeStartsAt();
                        if(tierAmount <= Float.parseFloat(amount)){
                            if(maxTier < tierAmount){
                                maxTier = tierAmount;
                                maxFee = feeTier.getTotalFeePerOrder();
                            }
                        }
                    }
                    amount = String.valueOf((Float.parseFloat(amount)+ maxFee));
                }
                getMerchantConfig(context);

            }

            @Override
            public void onFailure(Call<WidgetData>call, Throwable t){
                getMerchantConfig(context);
            }
        });
    }
}
