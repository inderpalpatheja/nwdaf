package com.nwdaf.Analytics.Validation;

import com.nwdaf.Analytics.Model.NwdafEvent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;


public class EventValidation implements ConstraintValidator<EventValidator, NwdafEvent> {

    @Override
    public boolean isValid(NwdafEvent nwdafEvent, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.asList(NwdafEvent.values()).contains(nwdafEvent);
    }
}
