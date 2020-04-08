package com.ruilebre.pandemicqueue.services;

import com.ruilebre.pandemicqueue.data.models.PandemicUser;
import com.ruilebre.pandemicqueue.data.models.SessionToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StoreService {
    @POST("users/auth")
    Call<SessionToken> login(@Body SessionToken body);

    @POST("users/register")
    Call register(@Body SessionToken body);

    @GET("users/get-user-details")
    Call<PandemicUser> getUserDetails(@Query("store") String store);
}
