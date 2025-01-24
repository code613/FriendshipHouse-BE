package com.levdevs.freindshipbe.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic user information for audit logging
    @Column(unique = true) // Ensures the email is unique at the database level
    private String email;
    private String fullName;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private UserGuest mainGuest;

    private boolean active = true; // Soft delete flag



    // Relationships
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGuest> userGuests = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patient> patients = new ArrayList<>();
}
