package com.quadpay.quadpay.Network;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface GatewayApi {

    @GET("virtual/widget-data")
    Call<WidgetData> getWidgetData(@QueryMap Map<String, String> parameters);

}
