package com.nwdaf.Analytics.Service.Validator.UpdateValidator.ErrorReport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public class NullValuesUpdateError {

    @Getter
    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));

    @Getter @Setter
    String eventID;

    @Getter
    final String message = "All Null values passed";
}
