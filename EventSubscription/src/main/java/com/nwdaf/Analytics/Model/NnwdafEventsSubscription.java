package com.nwdaf.Analytics.Model;

import java.util.UUID;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.NotificationMethod;

public class NnwdafEventsSubscription {

    UUID subscriptionID;
    EventID eventID;
    String notificationURI;
    String snssais;
    NotificationMethod notifMethod;
    Integer repetitionPeriod;
    Integer loadLevelThreshold;
    Boolean anySlice;



    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

    public Integer getEventID() {
        return this.eventID != null ? this.eventID.ordinal() : null;
    }

    public void setEventID(Integer ID) {
        this.eventID = (ID != null) ? EventID.values()[ID] : null;
    }

    public String getNotificationURI() {
        return notificationURI;
    }

    public void setNotificationURI(String notificationURI) {
        this.notificationURI = notificationURI;
    }

    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }

    public Integer getNotifMethod() {
        return notifMethod != null ? notifMethod.ordinal() : null;
    }

    public void setNotifMethod(Integer method_no) {
        this.notifMethod = (method_no != null) ? NotificationMethod.values()[method_no] : null;
    }

    public Integer getRepetitionPeriod() {
        return repetitionPeriod;
    }

    public void setRepetitionPeriod(Integer repetitionPeriod) {
        this.repetitionPeriod = repetitionPeriod;
    }

    public Integer getLoadLevelThreshold() {
        return loadLevelThreshold;
    }

    public void setLoadLevelThreshold(Integer loadLevelThreshold) {
        this.loadLevelThreshold = loadLevelThreshold;
    }

    public Boolean isAnySlice() {
        return anySlice;
    }

    public void setAnySlice(Boolean anySlice) {
        this.anySlice = anySlice;
    }

}