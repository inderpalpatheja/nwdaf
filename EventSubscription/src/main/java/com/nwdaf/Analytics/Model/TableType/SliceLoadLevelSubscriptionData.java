package com.nwdaf.Analytics.Model.TableType;

import java.util.UUID;

public class SliceLoadLevelSubscriptionData {

    private int ID;
    private UUID subscriptionID;
    private String snssais;
    private int loadLevelThreshold;


    public SliceLoadLevelSubscriptionData() {
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
