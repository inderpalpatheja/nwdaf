package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import org.springframework.http.HttpStatus;

public class AnalyticsError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    String message;
    String snssais;
    String anySlice;


    public AnalyticsError(AnalyticsRawData rawData, EventID eventID)
    {
        this.message = "Analytics " + eventID.toString() + " error";
        this.snssais = rawData.getSnssais();
        this.anySlice = rawData.getAnySlice();
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
