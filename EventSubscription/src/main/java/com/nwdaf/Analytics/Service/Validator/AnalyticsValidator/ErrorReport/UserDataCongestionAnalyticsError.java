package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.AnalyticsRawData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter @Setter
public class UserDataCongestionAnalyticsError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = "Analytics " + EventID.USER_DATA_CONGESTION.toString()  + " error";

    String supi;
    String congType;

    public UserDataCongestionAnalyticsError(AnalyticsRawData rawData)
    {
        this.supi = rawData.getSupi();
        this.congType = rawData.getCongType();
    }

}
