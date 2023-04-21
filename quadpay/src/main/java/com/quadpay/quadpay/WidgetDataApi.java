package com.quadpay.quadpay;

import java.sql.Array;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface WidgetDataApi {

    @GET("virtual/widget-data")
    Call<WidgetData> getWidgetData(
        @Query("merchantId") String merchantId,
        @Query("websiteUrl") String websiteUrl,
        @Query("environmentName") String environmentName,
        @Query("userId") String userId
    );

    @GET("virtual/widget-data")
    Call<WidgetData> getWidgetData(@QueryMap Map<String, String> parameters);

}
