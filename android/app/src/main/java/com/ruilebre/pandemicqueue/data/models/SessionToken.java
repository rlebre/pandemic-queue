package com.ruilebre.pandemicqueue.data.models;

public class SessionToken {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "SessionToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
