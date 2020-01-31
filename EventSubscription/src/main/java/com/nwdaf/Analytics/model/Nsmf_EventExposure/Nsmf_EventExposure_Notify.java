package com.nwdaf.Analytics.model.Nsmf_EventExposure;

public class Nsmf_EventExposure_Notify {

    private int eventId;
    private String notificaitonCorrelationInformation;
    private String UE_ID;
    private String PDPSessionId;
    private String TimeStamp;

    public Nsmf_EventExposure_Notify() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getNotificaitonCorrelationInformation() {
        return notificaitonCorrelationInformation;
    }

    public void setNotificaitonCorrelationInformation(String notificaitonCorrelationInformation) {
        this.notificaitonCorrelationInformation = notificaitonCorrelationInformation;
    }

    public String getUE_ID() {
        return UE_ID;
    }

    public void setUE_ID(String UE_ID) {
        this.UE_ID = UE_ID;
    }

    public String getPDPSessionId() {
        return PDPSessionId;
    }

    public void setPDPSessionId(String PDPSessionId) {
        this.PDPSessionId = PDPSessionId;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public Nsmf_EventExposure_Notify(int eventId, String notificaitonCorrelationInformation, String UE_ID, String PDPSessionId, String timeStamp) {
        this.eventId = eventId;
        this.notificaitonCorrelationInformation = notificaitonCorrelationInformation;
        this.UE_ID = UE_ID;
        this.PDPSessionId = PDPSessionId;
        TimeStamp = timeStamp;
    }
}
