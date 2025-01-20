package com.levdevs.freindshipbe.controller;

import com.levdevs.freindshipbe.DTO.*;
import com.levdevs.freindshipbe.Entity.*;
import com.levdevs.freindshipbe.Service.LocationService;
import com.levdevs.freindshipbe.Service.ReservationService;
import com.levdevs.freindshipbe.enums.VisitType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping(path = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(HttpSession session,@RequestPart("path") String path, @RequestBody MultipartFile file) {
        logger.info("Received file: {}", file.getOriginalFilename());
        logger.info("Session: {}", session.getId());
        logger.info("File size: {}", file.getSize());
        logger.info("File type: {}", file.getContentType());
        logger.info("Path: {}", path);

        FileUploadResponseDto response = reservationService.uploadFile(session, path, file);

        logger.info("File uploaded successfully: {}", response);
        return ResponseEntity.ok("File uploaded successfully.");
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReservationAPIResponseDto> createReservation(
          //  @RequestPart("patientFile") MultipartFile patientFile,
          HttpSession session,
            @RequestPart("request")  @Valid ApiRequestDto request) {
        logger.info("Received request: {}", request);
        logger.info("Session: {}", session.getId());

        ReservationAPIResponseDto response = reservationService.saveReservation(session,request);
        System.out.println("reservation saved: " + response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReservationAPIResponseDto>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
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



