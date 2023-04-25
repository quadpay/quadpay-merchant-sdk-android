package com.quadpay.quadpay.Network;

import com.quadpay.quadpay.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GatewayClient {
    private static GatewayClient instance = null;
    private GatewayApi gatewayApi;

    private GatewayClient(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.GatewayUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gatewayApi = retrofit.create(GatewayApi.class);
    }

    public static synchronized GatewayClient getInstance(){
        if (instance == null) {
            instance = new GatewayClient();
        }
        return instance;
    }

    public GatewayApi getWidgetDataApi() {
        return gatewayApi;
    }
}
