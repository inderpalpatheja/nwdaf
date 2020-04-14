package com.nwdaf.Analytics.Model;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


public class NotificationData {

    UUID subscriptionID;

    @Getter @Setter
    Integer loadLevelThreshold;


    public NotificationData(String subscriptionID, Integer loadLevelThreshold)
    {
        this.subscriptionID = UUID.fromString(subscriptionID);
        this.loadLevelThreshold = loadLevelThreshold;
    }

    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }
}
