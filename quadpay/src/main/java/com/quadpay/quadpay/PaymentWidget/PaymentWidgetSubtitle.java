package com.quadpay.quadpay.PaymentWidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class PaymentWidgetSubtitle extends TextView{

    private SpannableStringBuilder sb = new SpannableStringBuilder();
    public PaymentWidgetSubtitle(Context context) {
        super(context);
        float b = getTextSize();
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        float c = getTextSize();
        CreatePaymentWidgetSubtitle();
    }

    private void CreatePaymentWidgetSubtitle(){
        sb.append("You will be redirected to Zip to complete your order.");
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }


}
