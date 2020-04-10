package com.ruilebre.pandemicqueue.data.models;

import com.auth0.android.jwt.JWT;

import java.util.Date;

public class SessionToken {
    private String token;
    private JWT jwtToken;

    public SessionToken(String token) {
        this.token = token;
        this.jwtToken = new JWT(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        this.jwtToken = new JWT(token);
    }

    public JWT getJwtToken() {
        return jwtToken;
    }

    public boolean isValid() {
        Date currentDate = new Date();
        Date expirationDate = jwtToken.getExpiresAt();

        return expirationDate.after(currentDate);
    }

    public boolean isAuthenticated() {
        return (token != null && this.isValid() ? true : false);
    }

    @Override
    public String toString() {
        return "SessionToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
