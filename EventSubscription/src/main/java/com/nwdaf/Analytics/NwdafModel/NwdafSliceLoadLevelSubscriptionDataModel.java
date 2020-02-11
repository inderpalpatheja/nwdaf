package com.nwdaf.Analytics.NwdafModel;

import java.util.UUID;

public class NwdafSliceLoadLevelSubscriptionDataModel {

    private int ID;
    private UUID subscriptionID;
    private String snssais;
    private int loadLevelThreshold;


    public NwdafSliceLoadLevelSubscriptionDataModel() {
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
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

    public int getLoadLevelThreshold() {
        return loadLevelThreshold;
    }

    public void setLoadLevelThreshold(int loadLevelThreshold) {
        this.loadLevelThreshold = loadLevelThreshold;
    }
}
