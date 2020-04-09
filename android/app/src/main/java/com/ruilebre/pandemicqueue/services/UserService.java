package com.ruilebre.pandemicqueue.services;

import com.ruilebre.pandemicqueue.data.models.PandemicUser;
import com.ruilebre.pandemicqueue.data.models.SessionToken;
import com.ruilebre.pandemicqueue.utils.endpoints.UserEndpoint;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST(UserEndpoint.AUTH)
    Call<SessionToken> login(@Body HashMap<String, String> body);

    @POST(UserEndpoint.REGISTER)
    Call register(@Body SessionToken body);

    @GET(UserEndpoint.GET_USER_DETAILS)
    Call<PandemicUser> getUserDetails(@Query("store") String store);
}
