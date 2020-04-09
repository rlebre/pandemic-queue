package com.ruilebre.pandemicqueue.services;

import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.utils.backendendpoints.SubscriptionEndpoint;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SubscriptionService {
    @POST(SubscriptionEndpoint.SUBSCRIBE)
    Call<HashMap<String, String>> subscribe(@Body Store body);

    @POST(SubscriptionEndpoint.UNSUBSCRIBE)
    Call<HashMap<String, String>> unsubscribe(@Body Store store);
}
