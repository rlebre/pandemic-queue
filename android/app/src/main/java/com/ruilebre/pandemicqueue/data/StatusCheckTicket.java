package com.ruilebre.pandemicqueue.data;

public class StatusCheckTicket extends Status {
    public boolean ticketExists;

    public boolean getTicketExists() {
        return ticketExists;
    }

    public void setTicketExists(boolean ticketExists) {
        this.ticketExists = ticketExists;
    }
}
