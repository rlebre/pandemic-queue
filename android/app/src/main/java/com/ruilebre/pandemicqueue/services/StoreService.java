package com.ruilebre.pandemicqueue.services;

import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.data.models.Ticket;
import com.ruilebre.pandemicqueue.utils.backendendpoints.StoreEndpoint;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StoreService {
    @POST(StoreEndpoint.POST_STORE)
    Call createStore(@Body Store body);

    @GET(StoreEndpoint.GET_NUMBER_TICKETS)
    Call<HashMap<String, String>> getNumberTicketsWaiting(@Query("store") String store);

    @GET(StoreEndpoint.GET_WAITING_TICKETS)
    Call<List<Ticket>> getWaitingTickets(@Query("store") String store);

    @GET(StoreEndpoint.GET_STORE_DETAILS)
    Call<Store> getStoreDetails(@Query("store") String store);
}
