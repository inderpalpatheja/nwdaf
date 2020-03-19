package com.nwdaf.Analytics.Model.TableType.UEMobility;
/*
 * @created 16/03/2020 - 5:48 PM
 * @project EventSubscription
 * @author  sheetalkumar
 */

import java.util.UUID;

public class nwdafUEmobilitySubscriptionTable {

    private Integer ID;
    private String supi;
    private UUID subscriptionID;
    private UUID correlationID;
    private Integer referenceCount;


    public nwdafUEmobilitySubscriptionTable() {
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

    public void setSupi(String supi) {
        this.supi = supi;
    }

    public UUID getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(UUID subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public UUID getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(UUID correlationID) {
        this.correlationID = correlationID;
    }

    public Integer getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(Integer referenceCount) {
        this.referenceCount = referenceCount;
    }

    public nwdafUEmobilitySubscriptionTable(Integer ID, String supi, UUID subscriptionID, UUID correlationID, Integer referenceCount) {
        this.ID = ID;
        this.supi = supi;
        this.subscriptionID = subscriptionID;
        this.correlationID = correlationID;
        this.referenceCount = referenceCount;
    }
}
