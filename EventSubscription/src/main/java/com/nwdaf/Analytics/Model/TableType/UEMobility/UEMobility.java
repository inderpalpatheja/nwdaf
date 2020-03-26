package com.nwdaf.Analytics.Model.TableType.UEMobility;
/*
 * @created 16/03/2020 - 5:21 PM
 * @project EventSubscription
 * @author  sheetalkumar
 */

import java.util.Date;

public class UEMobility {

    String supi;
    Date ts;
    Integer DurationSec;
    String locationInfo;

    public UEMobility() {
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

    public UEMobility(String supi, Date ts, Integer durationSec, String locationInfo) {
        this.supi = supi;
        this.ts = ts;
        DurationSec = durationSec;
        this.locationInfo = locationInfo;
    }
}
