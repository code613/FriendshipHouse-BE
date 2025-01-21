package com.levdevs.freindshipbe.controller;

import com.levdevs.freindshipbe.DTO.ApiRequestDto;
import com.levdevs.freindshipbe.DTO.GuestDto;
import com.levdevs.freindshipbe.DTO.PatientDto;
import com.levdevs.freindshipbe.DTO.ReservationAPIResponseDto;
import com.levdevs.freindshipbe.Entity.Guest;
import com.levdevs.freindshipbe.Entity.Location;
import com.levdevs.freindshipbe.Entity.Patient;
import com.levdevs.freindshipbe.Entity.Reservation;
import com.levdevs.freindshipbe.Service.EmailService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.levdevs.freindshipbe.enums.ReservationStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/email")
public class emailTest {

    private final Logger logger = LoggerFactory.getLogger(emailTest.class);
    private final EmailService emailService;

    public emailTest(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> createReservation(
            @RequestBody @Valid ApiRequestDto request, HttpSession session) {
        logger.info("Received request: {}", request);
        logger.info("Session: {}", session.getId());

        // Map the request to the entity
        Reservation reservation = mapToEntity(request);
        emailService.sendReservationConfirmationEmail(reservation);
        emailService.sendStatusUpdateEmail(reservation, ReservationStatus.CANCELLED);
        emailService.sendReservationConfirmationEmailTymleaf(reservation);

        return ResponseEntity.ok("sent email successfully");
    }





    // Assuming you have Reservation, PatientEntity, GuestEntity classes
    private Reservation mapToEntity(ApiRequestDto request) {
        Reservation reservation = new Reservation();

        // Mapping simple fields
        reservation.setLocation(new Location(request.friendshipHouseLocation()));

        // Mapping the PatientDto to PatientEntity
        Patient patient = mapPatientToEntity(request.patient());
        reservation.setPatient(patient);

        // Mapping the list of GuestDto to a list of GuestEntity
        List<Guest> guests = request.guests().stream()
                .map(this::mapGuestToEntity)  // Assuming you have a method for mapping individual guests
                .collect(Collectors.toList());
        reservation.setGuests(guests);

        return reservation;
    }

    // Method to map PatientDto to PatientEntity
    private Patient mapPatientToEntity(PatientDto patientDto) {
        Patient patient = new Patient();
        patient.setFirstName(patientDto.firstName());
        patient.setLastName(patientDto.lastName());
        patient.setFacility(patientDto.facility());
        patient.setPatientCondition(patientDto.condition());

        // Assuming VisitTypeDto maps to some VisitTypeEntity
//        VisitType visitType = mapVisitTypeToEntity(patientDto.visitType());
//        patient.setVisitType(visitType);
        patient.setType(patientDto.visitType());
        patient.setRoom(patientDto.roomNumber());

        return patient;
    }

    // Method to map GuestDto to GuestEntity
    private Guest mapGuestToEntity(GuestDto guestDto) {
        Guest guestEntity = new Guest();
        guestEntity.setFirstName(guestDto.firstName());
        guestEntity.setLastName(guestDto.lastName());
        guestEntity.setRelationship(guestDto.relationship());
        guestEntity.setGender(guestDto.gender());
        guestEntity.setCell(guestDto.cell());
        guestEntity.setEmail(guestDto.email());
        guestEntity.setStreet(guestDto.street());
        guestEntity.setHouseNumber(guestDto.houseNumber());
        guestEntity.setCity(guestDto.city());
        guestEntity.setState(guestDto.state());
        guestEntity.setZip(guestDto.zip());
        guestEntity.setCheckInDate(guestDto.checkInDate());
        guestEntity.setCheckOutDate(guestDto.checkOutDate());
        guestEntity.setCountry(guestDto.country());

        return guestEntity;
    }

}
