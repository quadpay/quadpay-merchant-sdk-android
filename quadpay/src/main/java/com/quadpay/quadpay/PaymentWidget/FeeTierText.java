package com.quadpay.quadpay.PaymentWidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.widget.TextView;

import java.text.DecimalFormat;

@SuppressLint("AppCompatCustomView")
public class FeeTierText extends TextView {
    private SpannableStringBuilder sb = new SpannableStringBuilder();
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public FeeTierText(Context context, Float maxFee){
        super(context);
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        decimalFormat.setMinimumFractionDigits(2);
        CreateFeeTierText(maxFee);
    }

    private void CreateFeeTierText(Float maxFee) {
        sb.append("\n");
        if(maxFee % 2 == 0){
            int x = Math.round(maxFee);
            sb.append("There may be a $" + x +" finance charge to use Zip. This charge is included above.");
        }else{
            sb.append("There may be a $" + decimalFormat.format(maxFee) +" finance charge to use Zip. This charge is included above.");
        }

        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }
}
