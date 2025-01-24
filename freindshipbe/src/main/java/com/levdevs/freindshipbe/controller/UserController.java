package com.levdevs.freindshipbe.controller;


import com.levdevs.freindshipbe.DTO.CreateUserDTO;
import com.levdevs.freindshipbe.Entity.*;
import com.levdevs.freindshipbe.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CreateUserDTO createUserDTO) {
        userService.createUser(createUserDTO);
        return ResponseEntity.ok("User created successfully.");
    }

    // Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Get all guests for a user
    @GetMapping("/{userId}/guests")
    public ResponseEntity<List<UserGuest>> getAllGuests(@PathVariable Long userId) {
        List<UserGuest> guests = userService.getAllGuests(userId);
        return ResponseEntity.ok(guests);
    }

    // Add a reservation to a user
    @PostMapping("/{userId}/reservations")
    public ResponseEntity<String> addReservationToUser(
            @PathVariable Long userId,
            @RequestBody Reservation reservation) { // @RequestBody @Valid ApiRequestDto requestDto
        userService.addReservationToUser(userId, reservation);
        return ResponseEntity.ok("Reservation added successfully.");
    }

    // Get all reservations for a user
    @GetMapping("/{userId}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
        List<Reservation> reservations = userService.getReservationsByUser(userId);
        return ResponseEntity.ok(reservations);
    }

    // Get all patients for a user
    @GetMapping("/{userId}/patients")
    public ResponseEntity<List<Patient>> getAllPatients(@PathVariable Long userId) {
        List<Patient> patients = userService.getAllPatients(userId);
        return ResponseEntity.ok(patients);
    }

    // Deactivate a user
    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable Long userId) {
        userService.deactivateUser(userId);
        return ResponseEntity.ok("User deactivated successfully.");
    }

    // Activate a user
    @PutMapping("/{userId}/activate")
    public ResponseEntity<String> activateUser(@PathVariable Long userId) {
        userService.activateUser(userId);
        return ResponseEntity.ok("User activated successfully.");
    }


    //get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
