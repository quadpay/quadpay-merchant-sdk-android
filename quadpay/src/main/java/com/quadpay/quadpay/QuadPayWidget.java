package com.quadpay.quadpay;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.text.SpannableStringBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class QuadPayWidget extends FrameLayout {

    public QuadPayWidget(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.QuadPayWidget);

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

    public void Widget(Context context, TypedArray attributes,Boolean result) {

        TextView textView = new TextView(context);
        textView.setSingleLine(false);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String widgetText = Utils.SetWidgetText(attributes,context);

        SpannableString amountString = Utils.SetAmount(attributes);
        // Amount Styling
        Utils.setAmountStyle(amountString,attributes);
        // Layout of the widget
        Utils.SetWidgetLayout(context,
                textView,
                sb,
                amountString,
                widgetText,
                attributes,
                result);
        Utils.setWidgetAlignment(textView,attributes);
        Utils.setTextViewSize(textView,attributes);
        textView.setText(sb);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        addView(textView);

    }
}






