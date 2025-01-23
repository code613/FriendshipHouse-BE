package com.levdevs.freindshipbe.validation;

import com.levdevs.freindshipbe.DTO.GuestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckInCheckOutValidator implements ConstraintValidator<ValidCheckInCheckOut, Object> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    static {
        DATE_FORMAT.setLenient(false); // Ensure strict validation
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Skip validation if the object is null
        }

        try {
            // Assume the object is your DTO class
            GuestDto guestDto = (GuestDto) value;
            String checkInDateStr = guestDto.checkInDate();
            String checkOutDateStr = guestDto.checkOutDate();

            if (checkInDateStr == null || checkOutDateStr == null) {
                return true; // Let @NotNull handle null values
            }

            Date checkInDate = DATE_FORMAT.parse(checkInDateStr);
            Date checkOutDate = DATE_FORMAT.parse(checkOutDateStr);

            // Calculate the difference in milliseconds
            long diffInMillis = checkOutDate.getTime() - checkInDate.getTime();
            // Convert the difference to days
            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24); // 1000 ms/sec * 60 sec/min * 60 min/hr * 24 hrs/day

            // Ensure the difference is at least 1 day
            return diffInDays >= 1;
        } catch (Exception e) {
            // Invalid date format or other errors
            return false;
        }
    }
}

