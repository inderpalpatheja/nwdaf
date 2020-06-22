package com.nwdaf.Analytics.Validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventValidation.class)
public @interface EventValidator {

    String message() default "Invalid Event Type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
