package com.ruilebre.pandemicqueue.ui.login;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ruilebre.pandemicqueue.R;
import com.ruilebre.pandemicqueue.data.NormalizedError;
import com.ruilebre.pandemicqueue.data.models.LoggedInUser;
import com.ruilebre.pandemicqueue.data.models.SessionToken;
import com.ruilebre.pandemicqueue.utils.Endpoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private Context context;

    LoginViewModel(Context context) {
        this.context = context;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(Endpoint authEndpoint, String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, authEndpoint.toString(), jsonBody, new Response.Listener() {
            @Override
            public void onResponse(Object res) {
                JSONObject response = (JSONObject) res;
                Log.i("VOLLEY", response.toString());
                SessionToken token = new Gson().fromJson(response.toString(), SessionToken.class);

                LoggedInUser data = new LoggedInUser("", "");
                loginResult.setValue(new LoginResult(true));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                try {
                    JSONObject jsonError = new JSONObject(new String(error.networkResponse.data, "UTF-8"));
                    jsonError = jsonError.getJSONArray("errors").getJSONObject(0);

                    NormalizedError normalizedError = new Gson().fromJson(jsonError.toString(), NormalizedError.class);

                    System.out.println(normalizedError);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 3;
    }
}
