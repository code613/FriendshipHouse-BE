package com.levdevs.freindshipbe.validation;

import com.levdevs.freindshipbe.DTO.VisitTypeDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoomValidator implements ConstraintValidator<RoomRequiredForInpatient, String> {
    public RoomValidator() {
    }

    public boolean isValid(String room, ConstraintValidatorContext context) {
        VisitTypeDto visitTypeDto = (VisitTypeDto)context.unwrap(VisitTypeDto.class);
        if (visitTypeDto != null && "hospital inpatient".equals(visitTypeDto.type())) {
            return room != null && room.matches("\\d+");
        } else {
            return true;
        }
    }
}