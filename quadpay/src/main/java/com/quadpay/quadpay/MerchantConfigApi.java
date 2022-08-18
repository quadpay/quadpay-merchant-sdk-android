package com.quadpay.quadpay;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MerchantConfigApi {

    String BASE_URL = "https://qp-merchant-configs-dev.azureedge.net/";
    @GET("{merchantId}.json")
    Call<MerchantConfigResult> getMerchantAssets(@Path("merchantId") String merchantId);
}

