package com.quadpay.quadpay.Network;

import com.quadpay.quadpay.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MerchantConfigClient {
    private static MerchantConfigClient instance = null;
    private MerchantConfigApi merchantConfigApi;

    private MerchantConfigClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.CdnUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        merchantConfigApi = retrofit.create(MerchantConfigApi.class);
    }

    public static synchronized MerchantConfigClient getInstance() {
        if (instance == null) {
            instance = new MerchantConfigClient();
        }
        return instance;
    }

    public MerchantConfigApi getMerchantConfigApi() {
        return merchantConfigApi;
    }
}
