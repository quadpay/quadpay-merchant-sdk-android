package com.quadpay.quadpay.PaymentWidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.widget.TextView;

import java.text.DecimalFormat;

@SuppressLint({"AppCompatCustomView", "ViewConstructor"})
public class FeeTierText extends TextView {
    private final SpannableStringBuilder sb = new SpannableStringBuilder();
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final String PRE_FEE_TEXT = "There may be a $";
    private static final String POST_FEE_TEXT = " finance charge to use Zip. This charge is included above.";

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
            sb.append(PRE_FEE_TEXT)
                    .append(String.valueOf(x))
                    .append(POST_FEE_TEXT);
        }else{
            sb.append(PRE_FEE_TEXT)
                    .append(decimalFormat.format(maxFee))
                    .append(POST_FEE_TEXT);
        }

        setText(sb);
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
    }
}
