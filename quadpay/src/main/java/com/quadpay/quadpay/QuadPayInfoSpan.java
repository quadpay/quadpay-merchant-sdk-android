package com.quadpay.quadpay;

import android.content.Context;
import android.content.Intent;
import android.text.style.URLSpan;
import android.view.View;

public class QuadPayInfoSpan extends URLSpan {

    private String learnMoreUrl;
    private String merchantId;
    private String isMFPPMerchant;
    private String minModal;
    private String hasFees;


    public QuadPayInfoSpan(String url, String merchantId, String learnMoreUrl, String isMFPPMerchant, String minModal, String hasFees) {
        super(url);
        this.merchantId= merchantId;
        this.learnMoreUrl = learnMoreUrl;
        this.isMFPPMerchant = isMFPPMerchant;
        this.minModal = minModal;
        this.hasFees = hasFees;

    }

    @Override
    public void onClick(View widget) {

        Context context = widget.getContext();


        Intent intent = new Intent(context, ZipWidgetActivity.class);
        intent.putExtra("URL", this.getURL());
        intent.putExtra("MerchantId",this.merchantId);
        intent.putExtra("learnMoreUrl",this.learnMoreUrl);
        intent.putExtra("isMFPPMerchant",this.isMFPPMerchant);
        intent.putExtra("minModal",this.minModal);
        intent.putExtra("hasFees", this.hasFees);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            super.onClick(widget);
        }
    }

}
