package com.ruilebre.pandemicqueue.data.models;

import java.util.Date;

public class Store {
    private String _id;
    private String name;
    private String city;
    private String address;
    private int nWating;
    private Date lastOnQueue;
    private Date lastEnteredStore;
    private int capacity;
    private Ticket[] waitingTickets;
    private PandemicUser[] usersSubscribed;

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

    public int getnWating() {
        return nWating;
    }

    public void setnWating(int nWating) {
        this.nWating = nWating;
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

    @Override
    public String toString() {
        return "Store{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", nWating=" + nWating +
                ", lastOnQueue=" + lastOnQueue +
                ", lastEnteredStire=" + lastEnteredStore +
                ", capacity=" + capacity +
                ", waitingTickets=" + waitingTickets +
                ", usersSubscribed=" + usersSubscribed +
                '}';
    }
}
