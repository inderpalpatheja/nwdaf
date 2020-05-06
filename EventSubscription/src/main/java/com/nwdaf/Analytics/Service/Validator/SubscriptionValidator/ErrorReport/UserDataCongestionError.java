package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter @Setter
public class UserDataCongestionError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = EventID.USER_DATA_CONGESTION.toString() + " error";

    Object supi;
    Object congType;
    Object congLevelThreshold;

    public UserDataCongestionError(SubscriptionRawData rawData)
    {
        this.supi = rawData.getSupi();
        this.congType = rawData.getCongType();
        this.congLevelThreshold = rawData.getCongLevelThreshold();
    }

}
