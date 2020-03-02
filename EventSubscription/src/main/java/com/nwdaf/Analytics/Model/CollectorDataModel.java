package com.nwdaf.Analytics.Model;

public class CollectorDataModel {


    private String SnSaai;
    private Integer LOAD_LEVEL_INFORMATION;
    private Integer eventId;

    public CollectorDataModel() {
    }

    public CollectorDataModel(String snSaai, Integer LOAD_LEVEL_INFORMATION, Integer eventId) {
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

    public Integer getLOAD_LEVEL_INFORMATION() {
        return LOAD_LEVEL_INFORMATION;
    }

    public void setLOAD_LEVEL_INFORMATION(Integer LOAD_LEVEL_INFORMATION) {
        this.LOAD_LEVEL_INFORMATION = LOAD_LEVEL_INFORMATION;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}
