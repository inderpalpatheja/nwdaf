package com.nwdaf.Analytics.Model.TableType;

import java.util.UUID;

public class SliceLoadLevelSubscriptionData {

    private Integer ID;
    private UUID subscriptionID;
    private String snssais;
    private Integer loadLevelThreshold;


    public SliceLoadLevelSubscriptionData() {
    }


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }

    public Integer getLoadLevelThreshold() {
        return loadLevelThreshold;
    }

    public void setLoadLevelThreshold(Integer loadLevelThreshold) {
        this.loadLevelThreshold = loadLevelThreshold;
    }
}
