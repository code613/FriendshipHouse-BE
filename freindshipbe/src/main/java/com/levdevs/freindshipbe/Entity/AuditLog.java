package com.levdevs.freindshipbe.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; //session id or user id
    private String actionType;
    private String description;
    private String createdBy;
    private LocalDateTime timestamp;

    // Getters and setters
}
