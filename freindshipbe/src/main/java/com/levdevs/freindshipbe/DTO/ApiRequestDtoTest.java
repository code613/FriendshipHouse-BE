package com.levdevs.freindshipbe.DTO;

import jakarta.validation.constraints.NotBlank;

public record ApiRequestDtoTest(
        @NotBlank(message = "Friendship House Location is required")
        String friendshipHouseLocation
) {}
