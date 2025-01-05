package com.levdevs.freindshipbe.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Location() {
        this.name = "Friendship House";
    }

    public Location(String name) {
        this.name = name;
    }

    @Column(unique = true) // This makes the 'name' column unique in the database
    private String name;
}
