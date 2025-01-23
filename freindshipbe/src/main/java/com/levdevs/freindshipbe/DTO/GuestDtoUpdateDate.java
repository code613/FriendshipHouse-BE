package com.levdevs.freindshipbe.DTO;

import com.levdevs.freindshipbe.validation.ValidDate;
import jakarta.validation.constraints.NotBlank;

public record GuestDtoUpdateDate (
    @ValidDate(message = "Check-out date must be in the format yyyy-MM-dd, valid, and not in the past")
    String checkOutDate
) {}
