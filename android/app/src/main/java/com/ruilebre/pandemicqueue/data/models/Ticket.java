package com.ruilebre.pandemicqueue.data.models;

import java.util.Date;
import java.util.List;

public class Ticket {
    private Date enteredQueueTimestamp;
    private Date enteredStoreTimestamp;
    private List<PandemicUser> users;
    private List<Store> stores;

    public Date getEnteredQueueTimestamp() {
        return enteredQueueTimestamp;
    }

    public void setEnteredQueueTimestamp(Date enteredQueueTimestamp) {
        this.enteredQueueTimestamp = enteredQueueTimestamp;
    }

    public Date getEnteredStoreTimestamp() {
        return enteredStoreTimestamp;
    }

    public void setEnteredStoreTimestamp(Date enteredStoreTimestamp) {
        this.enteredStoreTimestamp = enteredStoreTimestamp;
    }

    public List<PandemicUser> getUsers() {
        return users;
    }

    public void setUsers(List<PandemicUser> users) {
        this.users = users;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "enteredQueueTimestamp=" + enteredQueueTimestamp +
                ", enteredStoreTimestamp=" + enteredStoreTimestamp +
                ", users=" + users +
                ", stores=" + stores +
                '}';
    }
}
