package com.nwdaf.Analytics.Model;

public class NnwdafEventsSubscriptionUEmobility {


    String subscriptionID;
    Integer eventID;
    String notificationURI;
    String supi;
    Integer notifMethod;
    Integer repetitionPeriod;


    public NnwdafEventsSubscriptionUEmobility(String subscriptionID, Integer eventID, String notificationURI, String supi, Integer notifMethod, Integer repetitionPeriod) {
        this.subscriptionID = subscriptionID;
        this.eventID = eventID;
        this.notificationURI = notificationURI;
        this.supi = supi;
        this.notifMethod = notifMethod;
        this.repetitionPeriod = repetitionPeriod;
    }

    public NnwdafEventsSubscriptionUEmobility() {
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

    public String getSupi() {
        return supi;
    }

    public void setSupi(String supi) {
        this.supi = supi;
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
}
