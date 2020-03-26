package com.nwdaf.Analytics.Model.TableType.UEMobility;
/*
 * @created 20/03/2020 - 2:19 PM
 * @project Analytics
 * @author  sheetalkumar
 */

public class UEMobilitySubscriptionData {

    Integer ID;
    String subscriptionID;
    String supi;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public UEMobilitySubscriptionData() {
    }

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public String getSupi() {
        return supi;
    }

    public void setSupi(String supi) {
        this.supi = supi;
    }

    public UEMobilitySubscriptionData(Integer ID, String subscriptionID, String supi) {
        this.ID = ID;
        this.subscriptionID = subscriptionID;
        this.supi = supi;
    }
}
