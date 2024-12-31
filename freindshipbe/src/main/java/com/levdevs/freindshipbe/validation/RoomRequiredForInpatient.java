package com.levdevs.freindshipbe.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {RoomValidator.class}
)
public @interface RoomRequiredForInpatient {
    String message() default "Room is required for inpatient visits";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
