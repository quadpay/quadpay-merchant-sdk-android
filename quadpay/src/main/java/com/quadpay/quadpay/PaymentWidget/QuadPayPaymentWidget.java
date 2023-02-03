package com.quadpay.quadpay.PaymentWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


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
        PaymentWidget(context,attributes);
    }

    private void PaymentWidget(Context context, TypedArray attributes) {
        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        String hideTimelineText = attributes.getString(R.styleable.QuadPayWidget_hideTimeline);
        String color = attributes.getString(R.styleable.QuadPayWidget_timelineColor);
        String amount = attributes.getString(R.styleable.QuadPayWidget_amount);
        if(merchantId!= null){
            Call<MerchantConfigResult> call = RetrofitClient.getInstance().getMerchantConfigApi().getMerchantAssets(merchantId);
            call.enqueue(new Callback<MerchantConfigResult>() {
                @Override
                public void onResponse(Call<MerchantConfigResult> call, Response<MerchantConfigResult> response) {
                    if(response.isSuccessful()){
                        PaymentWidget paymentWidget = new PaymentWidget(context, attributes, true);
                        addView(paymentWidget);
                        Boolean hideTimeline = hideTimelineText != null && hideTimelineText.equalsIgnoreCase("true");
                        if(!hideTimeline) {
                            Timelapse timelapse = new Timelapse(context, color,true , amount);
                            addView(timelapse);
                        }
                    }else{
                        PaymentWidget paymentWidget = new PaymentWidget(context, attributes, false);
                        addView(paymentWidget);
                        Boolean hideTimeline = hideTimelineText != null && hideTimelineText.equalsIgnoreCase("true");
                        if(!hideTimeline) {
                            Timelapse timelapse = new Timelapse(context, color,false , amount);
                            addView(timelapse);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MerchantConfigResult> call, Throwable t) {
                    PaymentWidget paymentWidget = new PaymentWidget(context, attributes, false);
                    addView(paymentWidget);
                    Boolean hideTimeline = hideTimelineText != null && hideTimelineText.equalsIgnoreCase("true");
                    if(!hideTimeline) {
                        Timelapse timelapse = new Timelapse(context, color,false , amount);
                        addView(timelapse);
                    }
                }
            });
        }else{
            PaymentWidget paymentWidget = new PaymentWidget(context, attributes, false);
            addView(paymentWidget);
            Boolean hideTimeline = hideTimelineText != null && hideTimelineText.equalsIgnoreCase("true");
            if(!hideTimeline) {
                Timelapse timelapse = new Timelapse(context, color,false , amount);
                addView(timelapse);
            }
        }
    }
}
