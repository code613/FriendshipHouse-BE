package com.levdevs.freindshipbe.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Temporal(TemporalType.DATE) // Using date instead of string for check-in date
    @Column(nullable = false)
    private Date checkInDate;

    @Temporal(TemporalType.DATE) // Using date instead of string for check-out date
    @Column(nullable = false)
    private Date checkOutDate;

    // Method to convert String to Date
    static private Date convertToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            // Handle exception, maybe throw a custom exception or log the error
            e.printStackTrace();
            return null; // or return a default date
        }
    }
}
