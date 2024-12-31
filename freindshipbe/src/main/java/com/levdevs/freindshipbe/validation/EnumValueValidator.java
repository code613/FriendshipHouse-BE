package com.levdevs.freindshipbe.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {
    private Enum<?>[] enumValues;

    public EnumValueValidator() {
    }

    public void initialize(EnumValue annotation) {
        this.enumValues = (Enum[])annotation.enumClass().getEnumConstants();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null && !value.isEmpty()) {
            for(Enum<?> enumValue : this.enumValues) {
                if (enumValue.name().equalsIgnoreCase(value)) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }
}

