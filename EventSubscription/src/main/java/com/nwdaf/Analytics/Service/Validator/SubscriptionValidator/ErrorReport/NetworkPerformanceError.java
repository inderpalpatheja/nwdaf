package com.nwdaf.Analytics.Service.Validator.SubscriptionValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import com.nwdaf.Analytics.Model.RawData.SubscriptionRawData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter @Setter
public class NetworkPerformanceError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = EventID.NETWORK_PERFORMANCE.toString() + " error";

    Object supi;
    Object nwPerfType;
    Object relativeRatioThreshold;
    Object absoluteNumThreshold;

    public NetworkPerformanceError(SubscriptionRawData rawData)
    {
        this.supi = rawData.getSupi();
        this.nwPerfType = rawData.getNwPerfType();
        this.relativeRatioThreshold = rawData.getRelativeRatioThreshold();
        this.absoluteNumThreshold = rawData.getAbsoluteNumThreshold();
    }

}
