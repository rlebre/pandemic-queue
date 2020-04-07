package com.ruilebre.pandemicqueue.data.models;

import java.util.List;

public class PandemicUser {
    private String username;
    private String email;
    private String password;
    private List<Store> storeSubscriptions;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Store> getStoreSubscriptions() {
        return storeSubscriptions;
    }

    public void setStoreSubscriptions(List<Store> storeSubscriptions) {
        this.storeSubscriptions = storeSubscriptions;
    }

    @Override
    public String toString() {
        return "PandemicUser{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", storeSubscriptions=" + storeSubscriptions +
                '}';
    }
}
