package com.levdevs.freindshipbe.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record ApiRequestDto(
        @NotBlank(message = "Friendship House Location is required")
        String friendshipHouseLocation,

        @NotNull(message = "Patient details are required")
        PatientDto patient,

        @NotEmpty(message = "At least one guest is required")
        List<@Valid GuestDto> guests // Validate each guest in the list
) {}

//record AddressDto(
//        @NotBlank(message = "Street is required")
//        String street,
//
//        @NotBlank(message = "House number is required")
//        String houseNumber,
//
//        String entrance, // Optional field
//
//        @NotBlank(message = "City is required")
//        String city,
//
//        @Pattern(regexp = "[A-Z]{2}", message = "State must be a valid 2-letter code")
//        String state,
//
//        @Pattern(regexp = "\\d{5}", message = "ZIP code must be a 5-digit number")
//        String zip
//) {}
//
//record CheckInDetailsDto(
//        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Check-in date must be in the format yyyy-MM-dd")
//        String checkInDate,
//
//        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Check-out date must be in the format yyyy-MM-dd")
//        String checkOutDate
//) {}
