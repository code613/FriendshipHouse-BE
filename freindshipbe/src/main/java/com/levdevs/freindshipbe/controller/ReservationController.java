package com.levdevs.freindshipbe.controller;

import com.levdevs.freindshipbe.DTO.*;
import com.levdevs.freindshipbe.Entity.*;
import com.levdevs.freindshipbe.Service.LocationService;
import com.levdevs.freindshipbe.Service.ReservationService;
import com.levdevs.freindshipbe.enums.ReservationStatus;
import com.levdevs.freindshipbe.enums.VisitType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping({"/api"})
//public class ApiController {
//    public ApiController() {
//    }
//
//    @PostMapping({"/reservations"})
//    public ResponseEntity<String> handleCheckIn(@RequestBody @Valid ApiRequestDto request) {
//        System.out.println("Received request: " + request);
//        return ResponseEntity.ok("Check-in details processed successfully.");
//    }
//}

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Create a new reservation")
    @PostMapping
    public ResponseEntity<ReservationAPIResponseDto> createReservation(
            @RequestBody @Valid ApiRequestDto request, HttpSession session) {
        logger.info("Received request: {}", request);
        logger.info("Session: {}", session.getId());

        ReservationAPIResponseDto response = reservationService.saveReservation(session,request);
        System.out.println("reservation saved: " + response);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a reservation")
    @PutMapping("/{reservationId}")
    public ResponseEntity<ReservationAPIResponseDto> updateReservation(
            @PathVariable Long reservationId,
            @RequestBody @Valid ApiRequestDto request) {
        logger.info("Received request: {}", request);
        ReservationAPIResponseDto response = reservationService.updateReservation(reservationId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{reservationId}/{guestId}")
    public ResponseEntity<ReservationAPIResponseDto> updateGuest(
            @PathVariable Long reservationId,
            @PathVariable Long guestId,
            @RequestBody @Valid GuestDtoUpdateDate guest) {
        logger.info("Received request to update guest: {}", guest);
        ReservationAPIResponseDto response = reservationService.updateGuest(reservationId, guestId, guest);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/statuses")
    public ResponseEntity<List<String>> getStatuses() {
        List<String> statuses = Arrays.stream(ReservationStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
    }

    @PatchMapping("/{reservationId}/status")
    public ResponseEntity<ReservationAPIResponseDto> updateReservationStatus(
            @PathVariable Long reservationId,
            @RequestBody Map<String, String> updates) {

        ReservationAPIResponseDto reservationNew;
        String newStatus = updates.get("status");
        try {
            ReservationStatus status = ReservationStatus.valueOf(newStatus);
            reservationNew = reservationService.updateReservationStatus(reservationId, status);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value: " + newStatus + " error message" + e.getMessage());
        }
        return ResponseEntity.ok(reservationNew);
    }


    @GetMapping("/all")
    public ResponseEntity<List<ReservationAPIResponseDto>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/all/filtered")
    public ResponseEntity<List<ReservationAPIResponseDto>> getFilteredReservations(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {

        logger.info("Received request to filter reservations by status: {}, start time: {}, end time: {}", status, startTime, endTime);
        List<ReservationAPIResponseDto> reservations = reservationService.getFilteredReservations(status, startTime, endTime);

        return ResponseEntity.ok(reservations);
    }



    @GetMapping("/{id}")
    public ResponseEntity<ReservationAPIResponseDto> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok("Reservation deleted successfully.");
    }
}



