package com.zip.zip.PaymentWidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class PaymentWidgetSubtitle extends TextView{

    private final SpannableStringBuilder sb = new SpannableStringBuilder();

    public PaymentWidgetSubtitle(Context context) {
        super(context);
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        CreatePaymentWidgetSubtitle();
    }

    private void CreatePaymentWidgetSubtitle(){
        sb.append("You will be redirected to Zip to complete your order.");
        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }


}
