package com.levdevs.freindshipbe.DTO;

import com.levdevs.freindshipbe.validation.RoomRequiredForInpatient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientDto(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Hospital name is required")
        String hospital,

        @NotBlank(message = "Patient condition is required")
        String patientCondition,

        @NotBlank(message = "Visit type is required")
        //     @EnumValue(enumClass = VisitType.class, message = "Invalid visit type")
        String type,

//        @Pattern(regexp = "\\d+", message = "Room must be a valid numeric value")
        @RoomRequiredForInpatient(message = "Room is required for inpatient visits")
        String room

//        @NotNull(message = "Visit type details are required")
//        VisitTypeDto visitType
) {}
