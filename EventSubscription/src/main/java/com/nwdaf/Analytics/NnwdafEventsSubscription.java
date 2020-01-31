package com.nwdaf.Analytics;

import java.util.UUID;

enum EventID
{
    LOAD_LEVEL_INFORMATION,
    NETWORK_PERFORMANCE,
    NF_LOAD,
    QOS_SUSTAINABILITY,
    SERVICE_EXPERIENCE,
    UE_MOBILITY,
    UE_COMM,
    USER_DATA_CONGESTION,
    ABNORMAL_BEHAVIOUR;
}


enum NotificationMethod
{ PERIODIC, THRESHOLD; }


public class NnwdafEventsSubscription {

    UUID subscriptionID;
    EventID eventID;
    String notificationURI;
    NotificationMethod notifMethod;
    int repetitionPeriod;
    int loadLevelThreshold;
    int eventDataID;

    String snssais;
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

    public int getEventDataID() {
        return eventDataID;
    }

    public void setEventDataID(int eventDataID) {
        this.eventDataID = eventDataID;
    }

    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }

    public boolean isAnySlice() {
        return anySlice;
    }

    public void setAnySlice(boolean anySlice) {
        this.anySlice = anySlice;
    }
}