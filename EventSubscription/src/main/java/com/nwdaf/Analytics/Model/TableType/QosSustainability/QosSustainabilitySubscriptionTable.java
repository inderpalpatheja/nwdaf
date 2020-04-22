package com.nwdaf.Analytics.Model.TableType.QosSustainability;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@NoArgsConstructor
public class QosSustainabilitySubscriptionTable {

    @Getter @Setter
    private Integer ID;

    @Getter @Setter
    private String plmnID;

    @Getter @Setter
    private String snssais;

    private UUID subscriptionID;

    private UUID correlationID;

    @Getter @Setter
    private Integer refCount;


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


    public QosSustainabilitySubscriptionTable(String subscriptionID, String correlationID)
    {
        this.subscriptionID = UUID.fromString(subscriptionID);
        this.correlationID = UUID.fromString(correlationID);
    }


}
