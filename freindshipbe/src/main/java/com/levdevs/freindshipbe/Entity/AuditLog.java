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

    private String userId; // User identifier, can be a string
    private String action; // e.g., "CREATED_RESERVATION", "CHANGED_STATUS"
    private LocalDateTime timestamp; // Action timestamp
    private String details; // Additional information about the action (optional)

    // Getters and setters
}
