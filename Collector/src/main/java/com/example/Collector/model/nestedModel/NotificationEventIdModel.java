package com.example.Collector.model.nestedModel;

public class NotificationEventIdModel {

    private int eventId;
    private String UEInfo;


    public NotificationEventIdModel() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getUEInfo() {
        return UEInfo;
    }

    public void setUEInfo(String UEInfo) {
        this.UEInfo = UEInfo;
    }

    public NotificationEventIdModel(int eventId, String UEInfo) {
        this.eventId = eventId;
        this.UEInfo = UEInfo;
    }
}
