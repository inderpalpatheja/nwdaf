package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
@AllArgsConstructor
public class UeMobilityAnalyticsError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = "Analytics " + EventID.UE_MOBILITY.toString()  + " error";

    String supi;
}
