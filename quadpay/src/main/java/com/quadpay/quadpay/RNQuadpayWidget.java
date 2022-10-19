package com.quadpay.quadpay;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.text.DecimalFormat;


public class RNQuadpayWidget extends FrameLayout {

    private SpannableStringBuilder sb = new SpannableStringBuilder();
    private TextView textView;
    private String min = "35";
    private String max  = "1500";
    private SpannableString amountString = null;
    private SpannableString widgetText = null;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private String currencySymbol = "$";
    private String amount = "";

    public RNQuadpayWidget(@NonNull Context context) {
        super(context);
        this.setPadding(16,16,16,16);
        this.setBackgroundColor(Color.parseColor("#33B5FF"));

        this.textView = new TextView(context);
        this.addView(this.textView);
    }


    private void setWidgetText(){
        this.sb = new SpannableStringBuilder();
        if (amount== null || amount.equals("")){
            widgetText = new SpannableString("4 payments on order over");
        }else if(Float.parseFloat(amount)< Float.parseFloat(min)){
            widgetText = new SpannableString("4 payments on order over");
        }else if(Float.parseFloat(amount)> Float.parseFloat(max)){
            widgetText = new SpannableString("4 payments on order up to");
        }else{
            widgetText = new SpannableString("4 payments of");
        }
        sb.append(widgetText + " " + amountString);
        this.textView.setText(sb);
    }

    public void setMerchantId(String merchantId){
        this.textView.setText(sb);
    }

    public void setSize(String size){
        Float sizePercentage = Float.parseFloat(size.replace("%",""));

        if(sizePercentage<90.0) {
            sizePercentage = 90f/100;
        }else if(sizePercentage>120.0)
        {
            sizePercentage =120f/100;
        }else{
            sizePercentage = sizePercentage/100;
        }

        this.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 39.0f * (size.equals("") ? 100 / 100 : sizePercentage));
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setAlignment(String alignment){
        switch(alignment.toLowerCase()){
            case "left":
                this.textView.setGravity(Gravity.LEFT);
                break;
            case "right":
                this.textView.setGravity(Gravity.RIGHT);
                break;
            case "center":
                this.textView.setGravity(Gravity.CENTER);
                break;
        }
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setDisplayMode(String displayMode){

    }

    public void setLogoOption(String logoOption){

    }

    public void setAmount(String amount){
        this.amount = amount;
        if(amount== null|| amount.equals("")){
            amountString = new SpannableString(" " + currencySymbol + decimalFormat.format(Float.parseFloat(this.min)));
        }else if (Float.parseFloat(amount)< Float.parseFloat(this.min)){
            amountString = new SpannableString(" " + currencySymbol + decimalFormat.format(Float.parseFloat(this.min)));
        }else if(Float.parseFloat(amount)> Float.parseFloat(this.max)){
            amountString = new SpannableString(" " + currencySymbol + decimalFormat.format(Float.parseFloat(this.max)));
        }else{
            amountString = new SpannableString(" " + currencySymbol + decimalFormat.format(Float.parseFloat(amount)/4));
        }
        setWidgetText();
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setMin(String min){
        this.min = min;
        setAmount(this.amount);
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setMax(String max){
        this.max = max;
        setAmount(this.amount);
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
