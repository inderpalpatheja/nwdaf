package com.nwdaf.Analytics.Model.TableType.QosSustainability;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class QosSustainabilitySubscriptionData {

    @Getter @Setter
    private Integer ID;

    private UUID subscriptionID;

    @Getter @Setter
    private String snssais;

    @Getter @Setter
    private Integer ranUeThroughputThreshold;

    @Getter @Setter
    private Integer qosFlowRetainThreshold;


    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

}
