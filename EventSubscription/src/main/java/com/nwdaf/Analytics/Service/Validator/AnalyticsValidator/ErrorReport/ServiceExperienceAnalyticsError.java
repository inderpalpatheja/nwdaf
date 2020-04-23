package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter @Setter
public class ServiceExperienceAnalyticsError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = "Analytics " + EventID.UE_MOBILITY.toString()  + " error";

    String supi;
    String snssais;
    String anyUE;

    public ServiceExperienceAnalyticsError(AnalyticsRawData rawData)
    {
        this.supi = rawData.getSupi();
        this.snssais = rawData.getSnssais();
        this.anyUE = rawData.getAnyUE();
    }
}
