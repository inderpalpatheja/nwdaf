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
    int repetitionPeriod;
    int loadLevelThreshold;
    boolean anySlice;


    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

    public int getEventID() {
        return eventID.ordinal();
    }

    public void setEventID(int ID) {
        this.eventID = EventID.values()[ID];
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

    public int getNotifMethod() {
        return notifMethod.ordinal();
    }

    public void setNotifMethod(int method_no) {
        this.notifMethod = NotificationMethod.values()[method_no];
    }

    public int getRepetitionPeriod() {
        return repetitionPeriod;
    }

    public void setRepetitionPeriod(int repetitionPeriod) {
        this.repetitionPeriod = repetitionPeriod;
    }

    public int getLoadLevelThreshold() {
        return loadLevelThreshold;
    }

    public void setLoadLevelThreshold(int loadLevelThreshold) {
        this.loadLevelThreshold = loadLevelThreshold;
    }

    public boolean isAnySlice() {
        return anySlice;
    }

    public void setAnySlice(boolean anySlice) {
        this.anySlice = anySlice;
    }
}