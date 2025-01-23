package com.levdevs.freindshipbe.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null validation
        }

        try {
            DATE_FORMAT.setLenient(false); // Ensure strict date parsing
            Date parsedDate = DATE_FORMAT.parse(value);

            // Check if the date is in the past
            return !parsedDate.before(new Date()); // Date is in the past
        } catch (ParseException e) {
            return false; // Invalid date format
        }
    }
}

