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
import jakarta.validation.Valid;
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

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


//    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> createReservation(
//            @RequestPart("patientFile") MultipartFile patientFile) {
//
//        System.out.println("Received patient file: " + patientFile.getOriginalFilename());
//        System.out.println("reservation saved: " );
//        return ResponseEntity.ok("responce");
//    }



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createReservation(
            @RequestPart("patientFile") MultipartFile patientFile,
         //   @RequestPart("guestFiles") List<MultipartFile> guestFiles,
            @RequestPart("request") @Valid ApiRequestDtoTest request) {

        System.out.println("Received request: " + request);
        System.out.println("Received patient file: " + patientFile.getOriginalFilename());
//        System.out.println("Received guest files: " + guestFiles.stream()
//                .map(MultipartFile::getOriginalFilename)
//                .toList());

        // Map DTO to Entity
//Reservation reservation = mapToEntity(request);
        System.out.println("Received request: " + request);
       // Reservation responce = reservationService.saveReservation(request);
      //  System.out.println("reservation saved: " + responce);
      //  return ResponseEntity.ok(responce);
        return ResponseEntity.ok("responce");

    }

    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok("Reservation deleted successfully.");
    }
}



