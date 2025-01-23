package com.levdevs.freindshipbe.DTO;

import com.levdevs.freindshipbe.enums.ReservationStatus;
import lombok.Data;

import java.util.List;

@Data
public class  ReservationAPIResponseDto{
    private Long id; // Reservation ID
    private ReservationStatus status; // Reservation status
    private String location; // Location as a string
    private PatientDto patient; // Patient with only necessary fields
    private List<GuestDto> guests; // Guests with only necessary fields
    private String createdAt; // Reservation creation date
}
