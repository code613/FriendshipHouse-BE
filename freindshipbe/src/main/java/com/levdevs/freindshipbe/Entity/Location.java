package com.levdevs.freindshipbe.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Location {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(unique = true) // This makes the 'name' column unique in the database
        private String name = "Default"; // Default value for the 'name' field

//        @ManyToMany(cascade = CascadeType.PERSIST) // Cascade persist ensures SubLocation is saved when Location is saved
//        @JoinTable(
//                name = "location_sublocation", // Name of the intermediate table
//                joinColumns = @JoinColumn(name = "location_id"), // Foreign key to Location
//                inverseJoinColumns = @JoinColumn(name = "sublocation_id") // Foreign key to SubLocation
//        )
//        private List<SubLocation> subLocations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "location_id") // Foreign key in SubLocation table
    private List<SubLocation> subLocations = new ArrayList<>();

    public Location() {}

    public Location(String name) {
        this.name = name;
    }


}
