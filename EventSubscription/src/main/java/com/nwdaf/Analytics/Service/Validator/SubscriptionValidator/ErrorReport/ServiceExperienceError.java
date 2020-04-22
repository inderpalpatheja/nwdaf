package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
public class ServiceExperienceError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = EventID.LOAD_LEVEL_INFORMATION.toString() + " error";

    Object supi;
    Object snssais;

    public ServiceExperienceError(SubscriptionRawData rawData)
    {
        this.supi = rawData.getSupi();
        this.snssais = rawData.getSnssais();
    }
}
