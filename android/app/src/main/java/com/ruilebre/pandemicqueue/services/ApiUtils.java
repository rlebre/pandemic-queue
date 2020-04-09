package com.ruilebre.pandemicqueue.services;

public class ApiUtils {

    public static final String BASE_URL = "http://192.168.1.93:3000";

    public static UserService getUserService() {
        return RetrofitClient.getRetrofit(BASE_URL).create(UserService.class);
    }

    public static StoreService getStoreService() {
        return RetrofitClient.getRetrofit(BASE_URL).create(StoreService.class);
    }

    public static TicketService getTicketService() {
        return RetrofitClient.getRetrofit(BASE_URL).create(TicketService.class);
    }

    public static SubscriptionService getSubscriptionService() {
        return RetrofitClient.getRetrofit(BASE_URL).create(SubscriptionService.class);
    }
}