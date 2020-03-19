package com.nwdaf.Analytics.Model.TableType.UEMobility;
/*
 * @created 16/03/2020 - 1:39 PM
 * @project EventSubscription
 * @author  sheetalkumar
 */

public class UE_MobilitySubscriptionModel {

    String subscriptionID;
    Integer eventID;
    String notificationURI;
    Integer notifMethod;
    Integer repetitionPeriod;
    String supi;


    public UE_MobilitySubscriptionModel() {
    }

    public String getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public String getNotificationURI() {
        return notificationURI;
    }

    public void setNotificationURI(String notificationURI) {
        this.notificationURI = notificationURI;
    }

    public Integer getNotifMethod() {
        return notifMethod;
    }

    public void setNotifMethod(Integer notifMethod) {
        this.notifMethod = notifMethod;
    }

    public Integer getRepetitionPeriod() {
        return repetitionPeriod;
    }

    public void setRepetitionPeriod(Integer repetitionPeriod) {
        this.repetitionPeriod = repetitionPeriod;
    }

    public String getSupi() {
        return supi;
    }

    public void setSupi(String supi) {
        this.supi = supi;
    }

    public UE_MobilitySubscriptionModel(String subscriptionID, Integer eventID, String notificationURI, Integer notifMethod, Integer repetitionPeriod, String supi) {
        this.subscriptionID = subscriptionID;
        this.eventID = eventID;
        this.notificationURI = notificationURI;
        this.notifMethod = notifMethod;
        this.repetitionPeriod = repetitionPeriod;
        this.supi = supi;
    }

    @Override
    public String toString() {
        return "UE_MobilitySubscriptionModel{" +
                "subscriptionID='" + subscriptionID + '\'' +
                ", eventID=" + eventID +
                ", notificationURI='" + notificationURI + '\'' +
                ", notifMethod=" + notifMethod +
                ", repetitionPeriod=" + repetitionPeriod +
                ", supi='" + supi + '\'' +
                '}';
    }
}
