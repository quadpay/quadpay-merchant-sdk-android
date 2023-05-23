package com.quadpay.quadpay.Network;

import android.content.Context;
import com.quadpay.quadpay.BuildConfig;
import java.io.File;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GatewayClient {
    private static GatewayClient instance = null;
    private final GatewayApi gatewayApi;


    private GatewayClient(Context context){
        Cache cache = this.httpCache(context);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.GatewayUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp(cache))
                .build();
        gatewayApi = retrofit.create(GatewayApi.class);
    }

    private OkHttpClient okHttp(Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new CacheInterceptor())
                .build();
    }

    private Cache httpCache(Context applicationContext) {
        long CACHE_SIZE = 5 * 1024 * 1024L;
        File httpCacheDirectory = new File(applicationContext.getCacheDir(), "zip-http-cache");
        return new Cache(httpCacheDirectory, CACHE_SIZE);
    }


    public static synchronized GatewayClient getInstance(Context context){
        if (instance == null) {
            instance = new GatewayClient(context);
        }
        return instance;
    }

    public GatewayApi getWidgetDataApi() {
        return gatewayApi;
    }
}

