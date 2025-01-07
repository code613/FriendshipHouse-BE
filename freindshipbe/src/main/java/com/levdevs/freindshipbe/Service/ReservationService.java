package com.levdevs.freindshipbe.Service;

import com.levdevs.freindshipbe.DAO.ReservationRepository;
import com.levdevs.freindshipbe.DTO.ApiRequestDto;
import com.levdevs.freindshipbe.DTO.GuestDto;
import com.levdevs.freindshipbe.DTO.PatientDto;
import com.levdevs.freindshipbe.Entity.Guest;
import com.levdevs.freindshipbe.Entity.Location;
import com.levdevs.freindshipbe.Entity.Patient;
import com.levdevs.freindshipbe.Entity.Reservation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final LocationService locationService;

    public ReservationService(ReservationRepository reservationRepository, LocationService locationService) {
        this.reservationRepository = reservationRepository;
        this.locationService = locationService;
    }

    public Reservation saveReservation(ApiRequestDto request) {

        Location locationId = locationService.getLocationIdByName(request.friendshipHouseLocation());

        Reservation reservation = mapToEntity(request,locationId);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    // Assuming you have Reservation, PatientEntity, GuestEntity classes
    private Reservation mapToEntity(ApiRequestDto request, Location location) {
        Reservation reservation = new Reservation();

        // Mapping simple fields
        reservation.setLocation(location);

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
        guestEntity.setEntrance(guestDto.entrance());
        guestEntity.setCity(guestDto.city());
        guestEntity.setState(guestDto.state());
        guestEntity.setZip(guestDto.zip());
        guestEntity.setCheckInDate(guestDto.checkInDate());
        guestEntity.setCheckOutDate(guestDto.checkOutDate());

        return guestEntity;
    }

    // Method to map VisitTypeDto to VisitTypeEntity (if needed)
//    private VisitType mapVisitTypeToEntity(VisitTypeDto visitTypeDto) {
//        VisitType visitType = new VisitType();
//        // Populate fields of visitTypeEntity based on visitTypeDto
//        return visitType;
//    }


}
