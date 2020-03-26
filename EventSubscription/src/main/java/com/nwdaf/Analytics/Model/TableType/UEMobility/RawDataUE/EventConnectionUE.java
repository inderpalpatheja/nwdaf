package com.nwdaf.Analytics.Model.TableType.UEMobility.RawDataUE;
/*
 * @created 23/03/2020 - 8:14 PM
 * @project Analytics
 * @author  sheetalkumar
 */


import java.util.Date;

public class EventConnectionUE {

    private Boolean DataStatus;
    private String Message;
    private String supi;
    private Date ts;
    private int DurationSec;
    private String location;


    public EventConnectionUE() {
    }

    public EventConnectionUE(Boolean dataStatus, String message, String supi, Date ts, int durationSec, String location) {
        DataStatus = dataStatus;
        Message = message;
        this.supi = supi;
        this.ts = ts;
        DurationSec = durationSec;
        this.location = location;
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

    public String getSupi() {
        return supi;
    }

    public void setSupi(String supi) {
        this.supi = supi;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public int getDurationSec() {
        return DurationSec;
    }

    public void setDurationSec(int durationSec) {
        DurationSec = durationSec;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "EventConnectionUE{" +
                "DataStatus=" + DataStatus +
                ", Message='" + Message + '\'' +
                ", supi='" + supi + '\'' +
                ", ts=" + ts +
                ", DurationSec=" + DurationSec +
                ", location='" + location + '\'' +
                '}';
    }
}