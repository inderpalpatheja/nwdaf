package com.nwdaf.Analytics;

import java.util.UUID;

public class events_connection {

    //LOAD_LEVEL_INFORMATION
    //private int event_id;
    private String snssais;

    private String subscriptionID;

    private int currentLoadLevelInfo;


    private int id;


    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }



    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public int getCurrentLoadLevelInfo() {
        return currentLoadLevelInfo;
    }

    public void setCurrentLoadLevelInfo(int currentLoadLevelInfo) {
        this.currentLoadLevelInfo = currentLoadLevelInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public events_connection() {
    }


    public events_connection(String snssais, boolean anySlice, int currentLoadLevelInfo) {
        this.snssais = snssais;
        this.currentLoadLevelInfo = currentLoadLevelInfo;
    }

    @Override
    public String toString() {
        return "events_connection [snssais=" + snssais + ", currentLoadLevelInfo="
                + currentLoadLevelInfo + "]";
    }


}