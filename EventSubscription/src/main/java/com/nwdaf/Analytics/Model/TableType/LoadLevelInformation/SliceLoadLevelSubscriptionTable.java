package com.nwdaf.Analytics.Model.TableType.LoadLevelInformation;

import java.util.UUID;

public class SliceLoadLevelSubscriptionTable {


    private Integer ID;
    private String snssais;
    private UUID subscriptionID;
    private UUID correlationID;
    private Integer referenceCount;

    public Integer getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(Integer refrenceCount) {
        this.referenceCount = refrenceCount;
    }

    public SliceLoadLevelSubscriptionTable() {
    }


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
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
