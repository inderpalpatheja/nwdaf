package com.nwdaf.Analytics.NwdafModel;

import java.util.UUID;

public class NwdafSliceLoadLevelSubscriptionTableModel {


    private int ID;
    private String snssais;
    private UUID subscriptionID;
    private UUID correlationID;
    private int refrenceCount;

    public int getRefrenceCount() {
        return refrenceCount;
    }

    public void setRefrenceCount(int refrenceCount) {
        this.refrenceCount = refrenceCount;
    }

    public NwdafSliceLoadLevelSubscriptionTableModel() {
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }

    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

    public String getCorrelationID() {
        return String.valueOf(correlationID);
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = UUID.fromString(correlationID);
    }
}
