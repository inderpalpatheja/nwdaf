package com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


public class UEmobilitySubscriptionTable {

    @Getter @Setter
    private Integer ID;

    @Getter @Setter
    private String supi;

    private UUID subscriptionID;

    private UUID correlationID;

    @Getter @Setter
    private Integer referenceCount;


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
