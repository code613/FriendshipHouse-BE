package com.levdevs.freindshipbe.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; // User identifier, can be a string
    private String action; // e.g., "CREATED_RESERVATION", "CHANGED_STATUS"
    private LocalDateTime timestamp; // Action timestamp
    @Column(length = 1000) // You can set a length limit here if needed
    private String details; // Additional information about the action

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt; // Automatically populated when created
    // Getters and setters
}
