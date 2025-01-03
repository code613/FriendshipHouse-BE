package com.levdevs.freindshipbe.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String friendshipHouseLocation;
    @ManyToOne(cascade = CascadeType.PERSIST) // Cascade persist ensures Location is saved when Reservation is saved
    @JoinColumn(name = "location_id" ) //, nullable = false)
    private Location location;  // Link to Location entity

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Patient patient;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guest> guests;
}



