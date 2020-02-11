package com.nwdaf.Analytics.NwdafModel;

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


public class NwdafSubscriptionTableModel {


    private UUID subscriptionID;
    private EventID eventID;
    private String notificationURI;
    private NotificationMethod notifMethod;
    private int repetitionPeriod;


    public NwdafSubscriptionTableModel() {
    }


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
        this.notifMethod = notifMethod.values()[method_no];
    }

    public int getRepetitionPeriod() {
        return repetitionPeriod;
    }

    public void setRepetitionPeriod(int repetitionPeriod) {
        this.repetitionPeriod = repetitionPeriod;
    }

    public NwdafSubscriptionTableModel(UUID subscriptionID, EventID eventID, String notificationURI, NotificationMethod notifMethod, int repetitionPeriod) {
        this.subscriptionID = subscriptionID;
        this.eventID = eventID;
        this.notificationURI = notificationURI;
        this.notifMethod = notifMethod;
        this.repetitionPeriod = repetitionPeriod;
    }
}
