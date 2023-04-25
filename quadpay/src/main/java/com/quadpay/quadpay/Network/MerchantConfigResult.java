package com.quadpay.quadpay.Network;

import com.google.gson.annotations.SerializedName;

public class MerchantConfigResult {

    @SerializedName("MerchantId")
    private String MerchantId;
    @SerializedName("MerchantName")
    private String MerchantName;
    @SerializedName("MinimumOrderAmount")
    private String MinimumOrderAmount;
    @SerializedName("MaximumOrderAmount")
    private String MaximumOrderAmount;
    @SerializedName("IsLongDurationLendingEnabled")
    private String IsLongDurationLendingEnabled;
    @SerializedName("LongDurationLendingMinimumAmount")
    private String LongDurationLendingMinimumAmount;
    @SerializedName("LongDurationLendingMaximumAmount")
    private String LongDurationLendingMaximumAmount;
    @SerializedName("ApplyGrayLabel")
    private String ApplyGrayLabel;
    @SerializedName("ModalGrayLabelName")
    private String ModalGrayLabelName;
    @SerializedName("LogoUrl")
    private String LogoUrl;
    @SerializedName("ModalTopColor")
    private String ModalTopColor;
    @SerializedName("LinkTextColor")
    private String LinkTextColor;
    @SerializedName("ModalHeadersFontStyle")
    private String ModalHeadersFontStyle;
    @SerializedName("ModalBodyFontStyle")
    private String ModalBodyFontStyle;
    @SerializedName("ZipWidgetFontStyle")
    private String ZipWidgetFontStyle;
    @SerializedName("TimelineColor")
    private String TimelineColor;
    @SerializedName("PaymentWidgetHeaderFontStyle")
    private String PaymentWidgetHeaderFontStyle;
    @SerializedName("PaymentWidgetBodyFontStyle")
    private String PaymentWidgetBodyFontStyle;

    public MerchantConfigResult(String name) {
        this.MerchantId = name;
    }

    public String getName() {
        return MerchantId;
    }

    public String getLogoUrl(){
        return LogoUrl;
    }

}
