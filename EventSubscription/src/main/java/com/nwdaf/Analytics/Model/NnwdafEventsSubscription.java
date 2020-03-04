package com.nwdaf.Analytics.Model;

import java.util.UUID;
import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.CustomData.NotificationMethod;

public class NnwdafEventsSubscription {

    String subscriptionID;
    Integer eventID;
    String notificationURI;
    String snssais;
    Integer notifMethod;
    Integer repetitionPeriod;
    Integer loadLevelThreshold;
    Boolean anySlice;


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

    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
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

    public Integer getLoadLevelThreshold() {
        return loadLevelThreshold;
    }

    public void setLoadLevelThreshold(Integer loadLevelThreshold) {
        this.loadLevelThreshold = loadLevelThreshold;
    }

    public Boolean getAnySlice() {
        return anySlice;
    }

    public void setAnySlice(Boolean anySlice) {
        this.anySlice = anySlice;
    }
}