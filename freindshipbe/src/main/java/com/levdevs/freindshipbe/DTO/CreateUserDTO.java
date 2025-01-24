package com.levdevs.freindshipbe.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateUserDTO (

    @NotNull
    @Email(message = "Email must be a valid format")
    String email,

    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName,

    @Pattern(regexp = "Male|Female", message = "Gender must be Male or Female")
    String gender,

    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}", message = "Cell number must match the format xxx-xxx-xxxx")
    String cell,

    @NotBlank(message = "Street is required")
    String street,

    @NotBlank(message = "House number is required")
    String houseNumber,

    @NotBlank(message = "City is required")
    String city,

    String state,

    @NotBlank(message = "Zip code is required")
    String zip,

    @NotBlank(message = "Country is required")
    String country

    // Getters and setters (or use Lombok for auto-generation)
){}

