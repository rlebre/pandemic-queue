package com.ruilebre.pandemicqueue.ui.login;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.ruilebre.pandemicqueue.R;
import com.ruilebre.pandemicqueue.data.NormalizedError;
import com.ruilebre.pandemicqueue.data.models.LoggedInUser;
import com.ruilebre.pandemicqueue.data.models.SessionToken;
import com.ruilebre.pandemicqueue.services.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;


public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private UserService userService;

    LoginViewModel(UserService userService) {
        this.userService = userService;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void offlineLogin(SessionToken token) {
        if (token.isValid()) {
            DecodedJWT decodedJWT = token.decode();
            Claim userId = decodedJWT.getClaim("userId");
            Claim username = decodedJWT.getClaim("username");
            LoggedInUser data = LoggedInUser.getInstance(userId.asString(), username.asString(), token);
            loginResult.setValue(new LoginResult(false, data.getDisplayName()));
        } else {
            loginResult.setValue(new LoginResult(true, "Token expired."));
        }

    }

    public void login(String email, String password) {
        HashMap<String, String> loginuser = new HashMap<>();
        loginuser.put("email", email);
        loginuser.put("password", password);
        Call call = userService.login(loginuser);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, retrofit2.Response response) {
                if (response.isSuccessful()) {
                    SessionToken token = (SessionToken) response.body();

                    DecodedJWT decodedJWT = token.decode();
                    Claim userId = decodedJWT.getClaim("userId");
                    Claim username = decodedJWT.getClaim("username");
                    LoggedInUser data = LoggedInUser.getInstance(userId.asString(), username.asString(), token);
                    loginResult.setValue(new LoginResult(false, data.getDisplayName()));
                } else {
                    try {
                        JSONObject jsonError = new JSONObject(response.errorBody().string());
                        jsonError = jsonError.getJSONArray("errors").getJSONObject(0);
                        NormalizedError normalizedError = new Gson().fromJson(jsonError.toString(), NormalizedError.class);
                        loginResult.setValue(new LoginResult(true, normalizedError.getTitle()));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        loginResult.setValue(new LoginResult(true, "Unknown error"));
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("DEBUG", t.getMessage());
            }
        });
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
