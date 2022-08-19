package com.quadpay.quadpay;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private MerchantConfigApi merchantConfigApi;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(MerchantConfigApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        merchantConfigApi = retrofit.create(MerchantConfigApi.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public MerchantConfigApi getMerchantConfigApi() {
        return merchantConfigApi;
    }
}
