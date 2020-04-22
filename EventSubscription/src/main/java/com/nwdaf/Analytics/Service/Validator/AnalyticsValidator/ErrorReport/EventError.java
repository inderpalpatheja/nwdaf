package com.nwdaf.Analytics.Service.Validator.AnalyticsValidator.ErrorReport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter @Setter
@AllArgsConstructor
public class EventError {

    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;
    final String message = "Analytics EventID error";

    String eventID;
}
