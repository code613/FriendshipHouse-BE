package com.levdevs.freindshipbe.Entity;

import com.levdevs.freindshipbe.enums.AdminSettingKey;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)  // This is important for auditing
public class AdminSettings {

    @Id
    @Enumerated(EnumType.STRING) // Persist enum as a string in the database
    @Column(nullable = false, unique = true)
    private AdminSettingKey settingKey;

    @Column(nullable = false)
    private String settingValue; // Store values as Strings to accommodate various types

    @Version
    private Long version; // For optimistic locking

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    // Default constructor (required by JPA)
    public AdminSettings() {
    }

    // Parameterized constructor for convenience
    public AdminSettings(AdminSettingKey settingKey, String settingValue) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }

}