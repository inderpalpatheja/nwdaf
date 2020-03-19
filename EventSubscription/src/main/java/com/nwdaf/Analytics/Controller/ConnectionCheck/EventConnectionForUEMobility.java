package com.nwdaf.Analytics.Controller.ConnectionCheck;

import java.util.Date;

public class EventConnectionForUEMobility {

    private Boolean DataStatus;
    private String Message;
    private String supi;
    private Date ts;
    private Integer DurationSec;
    private String locationInfo;

    public EventConnectionForUEMobility() {
    }

    public EventConnectionForUEMobility(Boolean dataStatus, String message, String supi, Date ts, Integer durationSec, String locationInfo) {
        DataStatus = dataStatus;
        Message = message;
        this.supi = supi;
        this.ts = ts;
        DurationSec = durationSec;
        this.locationInfo = locationInfo;
    }

    @Override
    public String toString() {
        return "EventConnectionForUEMobility{" +
                "DataStatus=" + DataStatus +
                ", Message='" + Message + '\'' +
                ", supi='" + supi + '\'' +
                ", ts=" + ts +
                ", DurationSec=" + DurationSec +
                ", locationInfo='" + locationInfo + '\'' +
                '}';
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

    public Integer getDurationSec() {
        return DurationSec;
    }

    public void setDurationSec(Integer durationSec) {
        DurationSec = durationSec;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }
}