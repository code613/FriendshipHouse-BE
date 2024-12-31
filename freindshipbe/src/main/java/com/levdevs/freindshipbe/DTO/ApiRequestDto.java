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

record PatientDto(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Hospital name is required")
        String hospital,

        @NotBlank(message = "Patient condition is required")
        String patientCondition,

        @NotNull(message = "Visit type details are required")
        VisitTypeDto visitType
) {}

record GuestDto(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Relationship is required")
        String relationship,

        @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
        String gender,

        @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Cell number must match the format xxx-xxx-xxxx")
        String cell,

        @Email(message = "Email must be a valid format")
        String email,

        @NotNull(message = "Address details are required")
        AddressDto address,

        @NotNull(message = "Check-in details are required")
        CheckInDetailsDto checkInDetails
) {}

record AddressDto(
        @NotBlank(message = "Street is required")
        String street,

        @NotBlank(message = "House number is required")
        String houseNumber,

        String entrance, // Optional field

        @NotBlank(message = "City is required")
        String city,

        @Pattern(regexp = "[A-Z]{2}", message = "State must be a valid 2-letter code")
        String state,

        @Pattern(regexp = "\\d{5}", message = "ZIP code must be a 5-digit number")
        String zip
) {}

record CheckInDetailsDto(
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Check-in date must be in the format yyyy-MM-dd")
        String checkInDate,

        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Check-out date must be in the format yyyy-MM-dd")
        String checkOutDate
) {}
