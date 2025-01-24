package com.levdevs.freindshipbe.Service;


import com.levdevs.freindshipbe.DAO.UserRepository;
import com.levdevs.freindshipbe.DTO.CreateUserDTO;
import com.levdevs.freindshipbe.Entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserDTO createUserDTO) {
        // Check if email already exists
        if (userRepository.existsByEmail(createUserDTO.email())) {
            throw new RuntimeException("Email is already in use");
        }

        // Create the user entity from the DTO
        User newUser = new User();
        newUser.setEmail(createUserDTO.email());
        newUser.setFullName(createUserDTO.firstName() + " " + createUserDTO.lastName());

        UserGuest mainGuest = new UserGuest();
        mainGuest.setFirstName(createUserDTO.firstName());
        mainGuest.setLastName(createUserDTO.lastName());
        mainGuest.setGender(createUserDTO.gender());
        mainGuest.setCell(createUserDTO.cell());
        mainGuest.setEmail(createUserDTO.email());
        mainGuest.setStreet(createUserDTO.street());
        mainGuest.setHouseNumber(createUserDTO.houseNumber());
        mainGuest.setCity(createUserDTO.city());
        mainGuest.setState(createUserDTO.state());
        mainGuest.setZip(createUserDTO.zip());
        mainGuest.setCountry(createUserDTO.country());

        // Assign the main guest to the user
        newUser.setMainGuest(mainGuest);

        return userRepository.save(newUser);
    }

    public List<UserGuest> getAllGuests(Long userId) {
        User user = getUserById(userId);

        // Add the main guest to the list of guests
        List<UserGuest> allGuests = new ArrayList<>(user.getUserGuests());
        if (user.getMainGuest() != null) {
            allGuests.add(user.getMainGuest());
        }
        return allGuests;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void addReservationToUser(Long userId, Reservation reservation) {
        User user = getUserById(userId);
        user.getReservations().add(reservation);
        reservation.setUser(user); // Set bidirectional relationship
        userRepository.save(user);
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        User user = getUserById(userId);
        return user.getReservations();
    }

    public List<Patient> getAllPatients(Long userId) {
        User user = getUserById(userId);
        return user.getPatients();
    }
    public void addPatientToUser(Long userId, Patient patient) {
        User user = getUserById(userId);
        user.getPatients().add(patient);
        patient.setUser(user); // Set bidirectional relationship
        userRepository.save(user);
    }

    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false); // Mark the user as inactive
        userRepository.save(user); // Retain reservations for auditing
    }

    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true); // Mark the user as active
        userRepository.save(user); // Retain reservations for auditing
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
