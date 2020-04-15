package com.ruilebre.pandemicqueue.ui.store;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.ruilebre.pandemicqueue.data.NormalizedError;
import com.ruilebre.pandemicqueue.data.Status;
import com.ruilebre.pandemicqueue.data.models.LoggedInUser;
import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.services.TicketService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;


public class StoreViewModel extends ViewModel {
    private TicketService ticketService;
    private Store store;
    private MutableLiveData<Status> callTicketResult = new MutableLiveData<>();
    private MutableLiveData<Status> createTicketResult = new MutableLiveData<>();

    StoreViewModel(TicketService ticketService, Store store) {
        this.ticketService = ticketService;
        this.store = store;
    }

    LiveData<Status> getCallTicketResult() {
        return callTicketResult;
    }

    LiveData<Status> getCreateTicketResult() {
        return createTicketResult;
    }

    public void callTicket() {
        String userToken = LoggedInUser.getInstance().getToken().getToken();

        Call call = ticketService.callTicket("Bearer " + userToken, this.getStoreBody());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                if (response.isSuccessful()) {
                    Status status = (Status) response.body();

                    callTicketResult.setValue(status);
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        jsonError = jsonError.getJSONArray("errors").getJSONObject(0);
                        NormalizedError normalizedError = new Gson().fromJson(jsonError.toString(), NormalizedError.class);
                        Log.e("TICKET", normalizedError.toString());
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void createTicket() {
        String userToken = LoggedInUser.getInstance().getToken().getToken();

        Call call = ticketService.createTicket("Bearer " + userToken, this.getStoreBody());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                if (response.isSuccessful()) {
                    Status status = (Status) response.body();

                    createTicketResult.setValue(status);
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        jsonError = jsonError.getJSONArray("errors").getJSONObject(0);
                        NormalizedError normalizedError = new Gson().fromJson(jsonError.toString(), NormalizedError.class);
                        Log.e("TICKET", normalizedError.toString());
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private HashMap<String, Store> getStoreBody() {
        HashMap storeBody = new HashMap<String, Store>();
        storeBody.put("store", store);

        return storeBody;
    }
}
