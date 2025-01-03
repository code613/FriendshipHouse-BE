package com.levdevs.freindshipbe.DTO;

import com.levdevs.freindshipbe.enums.VisitType;
import com.levdevs.freindshipbe.validation.EnumValue;
import com.levdevs.freindshipbe.validation.RoomRequiredForInpatient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public  record VisitTypeDto(
        @NotBlank(message = "Visit type is required")
   //     @EnumValue(enumClass = VisitType.class, message = "Invalid visit type")
        String type,

//        @Pattern(regexp = "\\d+", message = "Room must be a valid numeric value")
        @RoomRequiredForInpatient(message = "Room is required for inpatient visits")
        String room
) {}
