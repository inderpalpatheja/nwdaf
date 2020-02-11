package com.nwdaf.Analytics;

import java.util.UUID;

public class events_connection {

    private String snssais;

    private int currentLoadLevelInfo;




    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }




    public void setCurrentLoadLevelInfo(int currentLoadLevelInfo) {
        this.currentLoadLevelInfo = currentLoadLevelInfo;
    }


    public int getCurrentLoadLevelInfo() {
        return currentLoadLevelInfo;
    }

    public events_connection() {
    }


    public events_connection(String snssais,int currentLoadLevelInfo) {
        this.snssais = snssais;
        this.currentLoadLevelInfo = currentLoadLevelInfo;
    }

    @Override
    public String toString() {
        return "events_connection [ snssais=" + snssais + ", currentLoadLevelInfo="
                + currentLoadLevelInfo + "]";
    }


}