package com.levdevs.freindshipbe.controller;

import com.levdevs.freindshipbe.DTO.ApiRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api"})
public class ApiController {
    public ApiController() {
    }

    @PostMapping({"/checkin"})
    public ResponseEntity<String> handleCheckIn(@RequestBody @Valid ApiRequestDto request) {
        System.out.println("Received request: " + request);
        return ResponseEntity.ok("Check-in details processed successfully.");
    }
}