package com.nwdaf.Analytics.Service.Validator.UpdateValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public class SliceLoadLevelUpdateError {

    @Getter
    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;

    @Getter
    final String message = EventID.LOAD_LEVEL_INFORMATION.toString() + " Update Error";

    @Getter @Setter
    Object notifMethod;

    @Getter @Setter
    Object repetitionPeriod;

    @Getter @Setter
    Object loadLevelThreshold;

}
