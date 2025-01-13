package com.levdevs.freindshipbe.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String relationship;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String cell;

    @Column(nullable = false)
    private String email;

//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    private Address address;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String houseNumber;

//    private String entrance;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private String country;

//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    private CheckInDetails checkInDetails;

    @Column(nullable = false)
    private String checkInDate;

    @Column(nullable = false)
    private String checkOutDate;
}
