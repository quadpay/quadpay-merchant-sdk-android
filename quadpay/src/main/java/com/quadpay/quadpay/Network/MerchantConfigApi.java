package com.quadpay.quadpay.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface MerchantConfigApi {

    @GET("{merchantId}.json")
    Call<MerchantConfigResult> getMerchantAssets(@Path("merchantId") String merchantId);
}

