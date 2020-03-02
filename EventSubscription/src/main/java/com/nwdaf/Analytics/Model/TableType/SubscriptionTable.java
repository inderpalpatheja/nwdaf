package com.nwdaf.Analytics.Model.TableType;

import java.util.UUID;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.NotificationMethod;


public class SubscriptionTable {


    private UUID subscriptionID;
    private EventID eventID;
    private String notificationURI;
    private NotificationMethod notifMethod;
    private Integer repetitionPeriod;


    public SubscriptionTable() {
    }


    public String getSubscriptionID() {
        return String.valueOf(subscriptionID);
    }

    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = UUID.fromString(subscriptionID);
    }

    public Integer getEventID() {
        return eventID != null ? eventID.ordinal() : null;
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

    public Integer getNotifMethod() {
        return notifMethod != null ? notifMethod.ordinal() : null;
    }

    public void setNotifMethod(Integer method_no) {
        this.notifMethod = (method_no != null) ? notifMethod.values()[method_no] : null;
    }

    public Integer getRepetitionPeriod() {
        return repetitionPeriod;
    }

    public void setRepetitionPeriod(Integer repetitionPeriod) {
        this.repetitionPeriod = repetitionPeriod;
    }

    public SubscriptionTable(UUID subscriptionID, EventID eventID, String notificationURI, NotificationMethod notifMethod, Integer repetitionPeriod) {
        this.subscriptionID = subscriptionID;
        this.eventID = eventID;
        this.notificationURI = notificationURI;
        this.notifMethod = notifMethod;
        this.repetitionPeriod = repetitionPeriod;
    }
}
