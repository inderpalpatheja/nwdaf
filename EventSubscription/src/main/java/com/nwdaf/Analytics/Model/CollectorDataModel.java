package com.nwdaf.Analytics.Model;

public class CollectorDataModel {


    private String SnSaai;
    private int LOAD_LEVEL_INFORMATION;
    private int eventId;

    public CollectorDataModel() {
    }

    public CollectorDataModel(String snSaai, int LOAD_LEVEL_INFORMATION, int eventId) {
        SnSaai = snSaai;
        this.LOAD_LEVEL_INFORMATION = LOAD_LEVEL_INFORMATION;
        this.eventId = eventId;
    }

    public String getSnSaai() {
        return SnSaai;
    }

    public void setSnSaai(String snSaai) {
        SnSaai = snSaai;
    }

    public int getLOAD_LEVEL_INFORMATION() {
        return LOAD_LEVEL_INFORMATION;
    }

    public void setLOAD_LEVEL_INFORMATION(int LOAD_LEVEL_INFORMATION) {
        this.LOAD_LEVEL_INFORMATION = LOAD_LEVEL_INFORMATION;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
