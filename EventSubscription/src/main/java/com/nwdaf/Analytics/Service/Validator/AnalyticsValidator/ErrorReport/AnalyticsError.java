package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter @Setter
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
}
