package com.ruilebre.pandemicqueue.services.user;

import com.google.gson.Gson;
import com.ruilebre.pandemicqueue.data.models.SessionToken;
import com.ruilebre.pandemicqueue.services.HTTPService;

import java.util.concurrent.ExecutionException;

public class Auth extends HTTPService {
    public Auth(String url, String method, String body) {
        super(url, method, body);
    }

    public SessionToken getToken() throws ExecutionException, InterruptedException {
        return new Gson().fromJson(super.execute().get(), SessionToken.class);
    }
}
