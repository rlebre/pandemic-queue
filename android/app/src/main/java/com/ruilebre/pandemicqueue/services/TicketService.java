package com.ruilebre.pandemicqueue.services;

import com.ruilebre.pandemicqueue.data.Status;
import com.ruilebre.pandemicqueue.data.StatusCheckTicket;
import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.utils.endpoints.TicketEndpoint;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TicketService {
    @POST(TicketEndpoint.CALL_TICKET)
    Call<Status> callTicket(@Header("Authorization") String authorization, @Body HashMap<String, Store> store);

    @POST(TicketEndpoint.CREATE_TICKET)
    Call<Status> createTicket(@Header("Authorization") String authorization, @Body HashMap<String, Store> store);

    @GET(TicketEndpoint.CHECK_TICKET)
    Call<StatusCheckTicket> checkTicket(@Header("Authorization") String authorization, @Query("store") String store);

    @DELETE(TicketEndpoint.CANCEL_TICKET)
    Call<Status> cancelTicket(@Header("Authorization") String authorization, @Path("store") String store);
}
