package com.levdevs.freindshipbe.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String name;
}
