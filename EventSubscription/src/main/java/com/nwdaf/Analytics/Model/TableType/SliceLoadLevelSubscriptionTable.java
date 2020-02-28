package com.nwdaf.Analytics.Model.TableType;

import java.util.UUID;

public class SliceLoadLevelSubscriptionTable {


    private int ID;
    private String snssais;
    private UUID subscriptionID;
    private UUID correlationID;
    private int referenceCount;

    public int getRefrenceCount() {
        return referenceCount;
    }

    public void setRefrenceCount(int refrenceCount) {
        this.referenceCount = refrenceCount;
    }

    public SliceLoadLevelSubscriptionTable() {
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
