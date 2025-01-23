package com.levdevs.freindshipbe.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER}) // Apply at field or method parameter level
@Retention(RetentionPolicy.RUNTIME) // Annotation should be available at runtime
@Constraint(validatedBy = ValidDateValidator.class) // Link to the validator class
public @interface ValidDate {
    String message() default "Invalid date format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
