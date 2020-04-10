package com.ruilebre.pandemicqueue.ui.store;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.ruilebre.pandemicqueue.data.NormalizedError;
import com.ruilebre.pandemicqueue.data.models.Store;
import com.ruilebre.pandemicqueue.services.StoreService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class StoreListViewModel extends ViewModel {
    private StoreService storeService;
    private MutableLiveData<List<Store>> storeListResult = new MutableLiveData<>();

    StoreListViewModel(StoreService storeService) {
        this.storeService = storeService;
    }

    LiveData<List<Store>> getStoreListResult() {
        return storeListResult;
    }

    public void getStoreList() {
        Call call = storeService.getStoreList();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                if (response.isSuccessful()) {
                    List<Store> storeList = (List<Store>) response.body();

                    storeListResult.setValue(storeList);
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        jsonError = jsonError.getJSONArray("errors").getJSONObject(0);
                        NormalizedError normalizedError = new Gson().fromJson(jsonError.toString(), NormalizedError.class);
                        Log.e("FETCH", normalizedError.toString());
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
}
