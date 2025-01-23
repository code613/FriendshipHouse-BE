package com.levdevs.freindshipbe.Entity;

import com.levdevs.freindshipbe.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String friendshipHouseLocation;
//    @ManyToOne(cascade = CascadeType.PERSIST) // Cascade persist ensures Location is saved when Reservation is saved
//    @JoinColumn(name = "location_id" ) //, nullable = false)
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;   // Link to Location entity
 //   private String locationName;  // Store only the location ID

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Patient patient;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guest> guests;

    @Enumerated(EnumType.STRING) // Store the enum as a string
    private ReservationStatus status = ReservationStatus.NEW; // Default status is NEW

    private LocalDateTime createdAt = LocalDateTime.now(); // Automatically set when the reservation is created

}



