package com.quadpay.quadpay;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.style.URLSpan;
import android.view.View;

import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

public class QuadPayInfoSpan extends URLSpan {

    String learnMoreUrl;
    String merchantId;
    String isMFPPMerchant;
    String minModal;

    public QuadPayInfoSpan(String url,String merchantId,String learnMoreUrl, String isMFPPMerchant,String minModal) {
        super(url);
        this.merchantId= merchantId;
        this.learnMoreUrl = learnMoreUrl;
        this.isMFPPMerchant = isMFPPMerchant;
        this.minModal = minModal;
    }

    @Override
    public void onClick(View widget) {
        if(!BuildConfig.DEBUG) {
            segmentAnalytics(widget.getContext());
        }

        Context context = widget.getContext();


        Intent intent = new Intent(context, ZipWidgetActivity.class);
        intent.putExtra("URL", this.getURL());
        intent.putExtra("MerchantId",this.merchantId);
        intent.putExtra("learnMoreUrl",this.learnMoreUrl);
        intent.putExtra("isMFPPMerchant",this.isMFPPMerchant);
        intent.putExtra("minModal",this.minModal);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            super.onClick(widget);
        }
    }

    public void segmentAnalytics(Context context){

        Analytics analytics = new Analytics.Builder(context, BuildConfig.SegmentKey).build();

        Analytics.setSingletonInstance(analytics);

        // Safely call Analytics.with(context) from anywhere within your app!
        analytics.with(context).track("Widget Pressed from SDK",new Properties()
                .putValue("Merchant", this.merchantId ));
        analytics.reset();
    }
}