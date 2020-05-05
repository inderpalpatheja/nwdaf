package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter @Setter
public class NetworkPerfAnalyticsError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = "Analytics " + EventID.NETWORK_PERFORMANCE.toString()  + " error";

    String supi;
    String nwPerfType;
    String anyUE;


    public NetworkPerfAnalyticsError(AnalyticsRawData rawData)
    {
        this.supi = rawData.getSupi();
        this.nwPerfType = rawData.getNwPerfType();
        this.anyUE = rawData.getAnyUE();
    }

}
