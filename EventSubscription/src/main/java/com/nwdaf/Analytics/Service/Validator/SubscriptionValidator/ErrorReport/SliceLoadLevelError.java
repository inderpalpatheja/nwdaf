package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import org.springframework.http.HttpStatus;

public class SliceLoadLevelError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = EventID.LOAD_LEVEL_INFORMATION.toString() + " error";

    Object snssais;
    Object anySlice;
    Object notifMethod;
    Object repetitionPeriod;
    Object loadLevelThreshold;

    public SliceLoadLevelError(SubscriptionRawData rawData)
    {
        this.snssais = rawData.getSnssais();
        this.anySlice = rawData.getAnySlice();
        this.notifMethod = rawData.getNotifMethod();
        this.repetitionPeriod = rawData.getRepetitionPeriod();
        this.loadLevelThreshold = rawData.getLoadLevelThreshold();
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getSnssais() {
        return snssais;
    }

    public void setSnssais(Object snssais) {
        this.snssais = snssais;
    }

    public Object getAnySlice() {
        return anySlice;
    }

    public void setAnySlice(Object anySlice) {
        this.anySlice = anySlice;
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
}
