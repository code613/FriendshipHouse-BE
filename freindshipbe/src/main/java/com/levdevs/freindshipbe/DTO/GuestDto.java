package com.levdevs.freindshipbe.DTO;

import com.levdevs.freindshipbe.validation.ValidCheckInCheckOut;
import com.levdevs.freindshipbe.validation.ValidDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@ValidCheckInCheckOut(message = "Check-in date must be before check-out date")
public record GuestDto(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Relationship is required")
        String relationship,

        @Pattern(regexp = "Male|Female", message = "Gender must be Male or Female")
        String gender,

        @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Cell number must match the format xxx-xxx-xxxx")
        String cell,

        @Email(message = "Email must be a valid format")
        String email,

//        @NotNull(message = "Address details are required")
//        AddressDto address,

        @NotBlank(message = "Street is required")
        String street,

        @NotBlank(message = "House number is required")
        String houseNumber,
//
//        String entrance, // Optional field

        @NotBlank(message = "City is required")
        String city,

    //    @Pattern(regexp = "[A-Z]{2}", message = "State must be a valid 2-letter code")
        String state,

//        @Pattern(regexp = "\\d{5}", message = "ZIP code must be a 5-digit number")
        @NotBlank(message = "Zip code is required")
        String zip,

        @NotBlank(message = "Country is required")
        String country,

//        @NotNull(message = "Check-in details are required")
//        CheckInDetailsDto checkInDetails

//        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Check-in date must be in the format yyyy-MM-dd")
//        String checkInDate,
//
//        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Check-out date must be in the format yyyy-MM-dd")
//        String checkOutDate
        @NotBlank(message = "Check-in date cannot be null")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Check-out date must be in the format yyyy-MM-dd")
        @ValidDate(message = "Check-in date must be in the format yyyy-MM-dd, valid, and not in the past")
        String checkInDate,

        @NotBlank(message = "Check-out date cannot be null")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Check-out date must be in the format yyyy-MM-dd")
        @ValidDate(message = "Check-out date must be in the format yyyy-MM-dd, valid, and not in the past")
        String checkOutDate

) {}
