package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import org.springframework.http.HttpStatus;

public class UeMobilityError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = EventID.UE_MOBILITY.toString() + " error";

    Object notifMethod;
    Object repetitionPeriod;
    Object loadLevelThreshold;
    Object supi;


    public UeMobilityError(SubscriptionRawData rawData)
    {
        this.notifMethod = rawData.getNotifMethod();
        this.repetitionPeriod = rawData.getRepetitionPeriod();
        this.loadLevelThreshold = rawData.getLoadLevelThreshold();
        this.supi = rawData.getSupi();
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getNotifMethod() {
        return notifMethod;
    }

    public void setNotifMethod(Object notifMethod) {
        this.notifMethod = notifMethod;
    }

    public Object getRepetitionPeriod() {
        return repetitionPeriod;
    }

    public void setRepetitionPeriod(Object repetitionPeriod) {
        this.repetitionPeriod = repetitionPeriod;
    }

    public Object getLoadLevelThreshold() {
        return loadLevelThreshold;
    }

    public void setLoadLevelThreshold(Object loadLevelThreshold) {
        this.loadLevelThreshold = loadLevelThreshold;
    }

    public Object getSupi() {
        return supi;
    }

    public void setSupi(Object supi) {
        this.supi = supi;
    }
}
