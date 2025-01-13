package com.levdevs.freindshipbe.DTO;

import lombok.Data;

import java.util.List;

@Data
public class  ReservationAPIResponseDto{
private String location; // Location as a string
private PatientDto patient; // Patient with only necessary fields
private List<GuestDto> guests; // Guests with only necessary fields
}
