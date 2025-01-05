//package com.levdevs.freindshipbe.Entity;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name="patient")
//public class Patient {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private int id;
//    @Column(name = "first_name")
//    private String firstName;
//    @Column(name = "last_name")
//    private String lastName;
//
//    public Patient() {
//    }
//
//    public Patient(String firstName, String lastName) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
//
//    @Override
//    public String toString() {
//        return "Patient (%d) %s %s %n".formatted(id,firstName,lastName);
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//}
package com.levdevs.freindshipbe.Entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String facility;

    @Column(nullable = false)
    private String patientCondition;

    @Column(nullable = false)
    private String type; // e.g., inpatient, outpatient

    private String room;
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    private VisitType visitType;
}