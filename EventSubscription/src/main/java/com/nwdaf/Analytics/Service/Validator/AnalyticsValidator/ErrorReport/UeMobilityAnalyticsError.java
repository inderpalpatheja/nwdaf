package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import org.springframework.http.HttpStatus;

public class UeMobilityAnalyticsError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = "Analytics " + EventID.UE_MOBILITY.toString()  + " error";

    String supi;

    public UeMobilityAnalyticsError(String supi)
    { this.supi = supi; }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getSupi() {
        return supi;
    }

    public void setSupi(String supi) {
        this.supi = supi;
    }
}
