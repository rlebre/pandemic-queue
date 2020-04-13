package com.ruilebre.pandemicqueue.data;

public class Status {
    private String registered;

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "Status{" +
                "registered='" + registered + '\'' +
                '}';
    }
}
