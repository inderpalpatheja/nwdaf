package com.nwdaf.Analytics.Model.TableType.UEmobility;

import java.util.UUID;

public class UEmobilitySubscriptionTable {

    private Integer ID;
    private String supi;
    private UUID subscriptionID;
    private UUID correlationID;
    private Integer referenceCount;

    public Integer getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(Integer refrenceCount) {
        this.referenceCount = refrenceCount;
    }

    public UEmobilitySubscriptionTable() {
    }


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getSupi() {
        return supi;
    }

    public void setSupi(String snssais) {
        this.supi = snssais;
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
