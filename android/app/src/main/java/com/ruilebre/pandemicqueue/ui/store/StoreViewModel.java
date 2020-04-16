package com.ruilebre.pandemicqueue.ui.store;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.ruilebre.pandemicqueue.data.NormalizedError;
import com.ruilebre.pandemicqueue.data.Status;
import com.ruilebre.pandemicqueue.data.StatusCheckTicket;
import com.ruilebre.pandemicqueue.data.models.LoggedInUser;
import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.services.StoreService;
import com.ruilebre.pandemicqueue.services.TicketService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;


public class StoreViewModel extends ViewModel {
    private static final String TAG = "StoreViewModel";

    private TicketService ticketService;
    private StoreService storeService;
    private Store store;

    private MutableLiveData<Store> getStoreDetailsResult = new MutableLiveData<>();
    private MutableLiveData<Status> createTicketResult = new MutableLiveData<>();
    private MutableLiveData<StatusCheckTicket> checkTicketResult = new MutableLiveData<>();
    private MutableLiveData<Status> cancelTicketResult = new MutableLiveData<>();

    StoreViewModel(TicketService ticketService, StoreService storeService, Store store) {
        this.storeService = storeService;
        this.ticketService = ticketService;
        this.store = store;
    }

    LiveData<Store> getStoreDetailsResult() {
        return getStoreDetailsResult;
    }

    LiveData<StatusCheckTicket> getCheckTicketResult() {
        return checkTicketResult;
    }

    LiveData<Status> getCreateTicketResult() {
        return createTicketResult;
    }

    LiveData<Status> getCancelTicketResult() {
        return cancelTicketResult;
    }

    public void getStoreDetails() {
        String userToken = LoggedInUser.getInstance().getToken().getToken();

        Call call = storeService.getStoreDetails(store.getName());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                if (response.isSuccessful()) {
                    Store store = (Store) response.body();

                    getStoreDetailsResult.setValue(store);
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        jsonError = jsonError.getJSONArray("errors").getJSONObject(0);
                        NormalizedError normalizedError = new Gson().fromJson(jsonError.toString(), NormalizedError.class);
                        Log.e(TAG, normalizedError.toString());
                    } catch (JSONException | IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void checkTicket() {
        String userToken = LoggedInUser.getInstance().getToken().getToken();

        Call call = ticketService.checkTicket("Bearer " + userToken, store.getName());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                if (response.isSuccessful()) {
                    StatusCheckTicket status = (StatusCheckTicket) response.body();
                    checkTicketResult.setValue(status);
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        jsonError = jsonError.getJSONArray("errors").getJSONObject(0);
                        NormalizedError normalizedError = new Gson().fromJson(jsonError.toString(), NormalizedError.class);
                        Log.e(TAG, normalizedError.toString());
                    } catch (JSONException | IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void cancelTicket() {
        String userToken = LoggedInUser.getInstance().getToken().getToken();

        Call call = ticketService.cancelTicket("Bearer " + userToken, store.get_id());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                if (response.isSuccessful()) {
                    Status status = (Status) response.body();
                    cancelTicketResult.setValue(status);
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        jsonError = jsonError.getJSONArray("errors").getJSONObject(0);
                        NormalizedError normalizedError = new Gson().fromJson(jsonError.toString(), NormalizedError.class);
                        Log.e(TAG, normalizedError.toString());
                    } catch (JSONException | IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, t.getMessage());
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
                        Log.e(TAG, normalizedError.toString());
                    } catch (JSONException | IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private HashMap<String, Store> getStoreBody() {
        HashMap storeBody = new HashMap<String, Store>();
        storeBody.put("store", store);

        return storeBody;
    }
}
