package com.nwdaf.Analytics.Model.RawData;

public class AnalyticsRawData {

    String eventID;
    String snssais;
    String anySlice;
    String supi;


    public AnalyticsRawData(){}

    public AnalyticsRawData(String eventID, String snssais, String anySlice)
    {
        this.eventID = eventID;
        this.snssais = snssais;
        this.anySlice = anySlice;
    }

    public AnalyticsRawData(String eventID, String supi)
    {
        this.eventID = eventID;
        this.supi = supi;
    }


    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getSnssais() {
        return snssais;
    }

    public void setSnssais(String snssais) {
        this.snssais = snssais;
    }

    public String getAnySlice() {
        return anySlice;
    }

    public void setAnySlice(String anySlice) {
        this.anySlice = anySlice;
    }

    public String getSupi() {
        return supi;
    }

    public void setSupi(String supi) {
        this.supi = supi;
    }
}
