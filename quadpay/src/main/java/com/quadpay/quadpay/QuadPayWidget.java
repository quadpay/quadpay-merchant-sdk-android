package com.quadpay.quadpay;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuadPayWidget extends FrameLayout {

    public QuadPayWidget(@NonNull Context context,AttributeSet attrs) {
        super(context,attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.QuadPayWidget);
        String merchantId = attributes.getString(R.styleable.QuadPayWidget_merchantId);
        if (merchantId != null) {
            Call<MerchantConfigResult> call = RetrofitClient.getInstance().getMerchantConfigApi().getMerchantAssets(merchantId);
            call.enqueue(new Callback<MerchantConfigResult>() {
                @Override
                public void onResponse(Call<MerchantConfigResult> call, Response<MerchantConfigResult> response) {
                    //Need to make a call to get the svg from the result below.
                    MerchantConfigResult cdnResult = response.body();
                    try {

                        Widget(context,attributes,true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MerchantConfigResult> call, Throwable t) {
                    Widget(context,attributes,false);
                }
            });
        }
        else{
            Widget(context,attributes,false);
        }
    }

    public void Widget(Context context, TypedArray attributes, Boolean result) {
        QuadPayWidgetTextView quadPayWidgetTextView = new QuadPayWidgetTextView(context, attributes,result);
        addView(quadPayWidgetTextView);
    }
}
