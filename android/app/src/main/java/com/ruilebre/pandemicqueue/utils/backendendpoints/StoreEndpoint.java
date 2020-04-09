package com.ruilebre.pandemicqueue.utils.backendendpoints;

import com.ruilebre.pandemicqueue.utils.Endpoint;

public class StoreEndpoint {
    public static final String POST_STORE = Endpoint.API_BASEPATH + "/stores/store";
    public static final String GET_NUMBER_TICKETS = Endpoint.API_BASEPATH + "/stores/number-waiting-tickets";
    public static final String GET_WAITING_TICKETS = Endpoint.API_BASEPATH + "/stores/waiting-tickets";
    public static final String GET_STORE_DETAILS = Endpoint.API_BASEPATH + "/stores/get-store-details";
}
