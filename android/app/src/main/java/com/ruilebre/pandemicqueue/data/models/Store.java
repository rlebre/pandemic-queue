package com.ruilebre.pandemicqueue.data.models;

import java.io.Serializable;
import java.util.Date;

public class Store implements Serializable {
    private String _id;
    private String name;
    private String city;
    private String address;
    private int nWaiting;
    private Date lastOnQueue;
    private Date lastEnteredStore;
    private int capacity;
    private Ticket[] waitingTickets;
    private PandemicUser[] usersSubscribed;
    private String parentStore;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getnWaiting() {
        return nWaiting;
    }

    public void setnWaiting(int nWaiting) {
        this.nWaiting = nWaiting;
    }

    public Date getLastOnQueue() {
        return lastOnQueue;
    }

    public void setLastOnQueue(Date lastOnQueue) {
        this.lastOnQueue = lastOnQueue;
    }

    public Date getLastEnteredStore() {
        return lastEnteredStore;
    }

    public void setLastEnteredStore(Date lastEnteredStore) {
        this.lastEnteredStore = lastEnteredStore;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Ticket[] getWaitingTickets() {
        return waitingTickets;
    }

    public void setWaitingTickets(Ticket[] waitingTickets) {
        this.waitingTickets = waitingTickets;
    }

    public PandemicUser[] getUsersSubscribed() {
        return usersSubscribed;
    }

    public void setUsersSubscribed(PandemicUser[] usersSubscribed) {
        this.usersSubscribed = usersSubscribed;
    }

    public String getParentStore() {
        return parentStore;
    }

    public void setParentStore(String parentStore) {
        this.parentStore = parentStore;
    }

    @Override
    public String toString() {
        return "Store{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", nWating=" + nWaiting +
                ", lastOnQueue=" + lastOnQueue +
                ", lastEnteredStire=" + lastEnteredStore +
                ", capacity=" + capacity +
                ", waitingTickets=" + waitingTickets +
                ", usersSubscribed=" + usersSubscribed +
                '}';
    }
}
