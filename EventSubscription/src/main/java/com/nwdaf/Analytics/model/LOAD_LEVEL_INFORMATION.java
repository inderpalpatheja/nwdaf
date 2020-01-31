package com.nwdaf.Analytics.model;

public class LOAD_LEVEL_INFORMATION {

    private int eventId;
    private String snssai;
    private boolean anySlice;
    private int id;
    private int load_level_info;
    private String subscriptionId;

    public LOAD_LEVEL_INFORMATION() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getSnssai() {
        return snssai;
    }

    public void setSnssai(String snssai) {
        this.snssai = snssai;
    }

    public boolean isAnySlice() {
        return anySlice;
    }

    public void setAnySlice(boolean anySlice) {
        this.anySlice = anySlice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoad_level_info() {
        return load_level_info;
    }

    public void setLoad_level_info(int load_level_info) {
        this.load_level_info = load_level_info;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LOAD_LEVEL_INFORMATION(int eventId, String snssai, boolean anySlice, int id, int load_level_info, String subscriptionId) {
        this.eventId = eventId;
        this.snssai = snssai;
        this.anySlice = anySlice;
        this.id = id;
        this.load_level_info = load_level_info;
        this.subscriptionId = subscriptionId;
    }
}
