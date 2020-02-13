package com.nwdaf.Analytics;

import java.util.UUID;

public class EventConnection {

    private Boolean DataStatus;
    private String Message;
    private String snssais;
    private int currentLoadLevelInfo;


    public EventConnection() {
    }

    public EventConnection(Boolean dataStatus, String message, String snssais, int currentLoadLevelInfo) {
        DataStatus = dataStatus;
        Message = message;
        this.snssais = snssais;
        this.currentLoadLevelInfo = currentLoadLevelInfo;
    }

    public Boolean getDataStatus() {
        return DataStatus;
    }

    public void setDataStatus(Boolean dataStatus) {
        DataStatus = dataStatus;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }

    public int getCurrentLoadLevelInfo() {
        return currentLoadLevelInfo;
    }

    public void setCurrentLoadLevelInfo(int currentLoadLevelInfo) {
        this.currentLoadLevelInfo = currentLoadLevelInfo;
    }

    @Override
    public String toString() {
        return "Event Connections [ snssais=" + snssais + ", currentLoadLevelInfo="
                + currentLoadLevelInfo + "]";
    }


}