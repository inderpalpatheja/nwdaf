package com.nwdaf.Analytics.Model;


import java.util.UUID;

public class NotificationData {

    UUID subscriptionID;
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

    public Integer getLoadLevelThreshold() {
        return loadLevelThreshold;
    }

    public void setLoadLevelThreshold(Integer loadLevelThreshold) {
        this.loadLevelThreshold = loadLevelThreshold;
    }
}
