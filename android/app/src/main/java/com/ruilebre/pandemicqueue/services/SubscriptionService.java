package com.ruilebre.pandemicqueue.services;

import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.utils.endpoints.SubscriptionEndpoint;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SubscriptionService {
    @POST(SubscriptionEndpoint.SUBSCRIBE)
    Call<HashMap<String, String>> subscribe(@Header("Authorization") String authorization, @Body Store body);

    @POST(SubscriptionEndpoint.UNSUBSCRIBE)
    Call<HashMap<String, String>> unsubscribe(@Header("Authorization") String authorization, @Body Store store);
}
