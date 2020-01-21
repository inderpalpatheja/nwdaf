package com.example.Collector.model.Namf_EventExposure;

import com.example.Collector.model.nestedModel.NotificationEventIdModel;

public class Namf_EventExposure_Notify {

    private int AMF_ID;
    private String notificaionCorrelationInformation;
    private NotificationEventIdModel eventId;
    private String timeStamp;

    public Namf_EventExposure_Notify() {
    }

    public int getAMF_ID() {
        return AMF_ID;
    }

    public void setAMF_ID(int AMF_ID) {
        this.AMF_ID = AMF_ID;
    }

    public String getNotificaionCorrelationInformation() {
        return notificaionCorrelationInformation;
    }

    public void setNotificaionCorrelationInformation(String notificaionCorrelationInformation) {
        this.notificaionCorrelationInformation = notificaionCorrelationInformation;
    }

    public NotificationEventIdModel getEventId() {
        return eventId;
    }

    public void setEventId(NotificationEventIdModel eventId) {
        this.eventId = eventId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Namf_EventExposure_Notify(int AMF_ID, String notificaionCorrelationInformation, NotificationEventIdModel eventId, String timeStamp) {
        this.AMF_ID = AMF_ID;
        this.notificaionCorrelationInformation = notificaionCorrelationInformation;
        this.eventId = eventId;
        this.timeStamp = timeStamp;
    }
}
