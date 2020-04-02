package com.nwdaf.Analytics.Service.Validator.UpdateValidator.ErrorReport;

import com.nwdaf.Analytics.Model.CustomData.EventID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public class QosSustainabilityUpdateError {

    @Getter
    final String code = String.valueOf(HttpStatus.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));;

    @Getter
    final String message = EventID.QOS_SUSTAINABILITY.toString() + " Update Error";

    @Getter @Setter
    Object ranUeThroughputThreshold;

    @Getter @Setter
    Object qosFlowRetainThreshold;

}
