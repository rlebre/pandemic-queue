package com.ruilebre.pandemicqueue.utils.endpoints;

public class TicketEndpoint {
    public static final String CREATE_TICKET = Base.API_BASEPATH + "/tickets/create";
    public static final String CALL_TICKET = Base.API_BASEPATH + "/tickets/call";
    public static final String CHECK_TICKET = Base.API_BASEPATH + "/tickets/exist";
    public static final String CANCEL_TICKET = Base.API_BASEPATH + "/tickets/{store}";
}
