package com.levdevs.freindshipbe.DAO;

import com.levdevs.freindshipbe.Entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Find all audit logs within a specific time frame
    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    // Find all audit logs of specific action types
    List<AuditLog> findByActionIn(List<String> actions);

    // Find all audit logs within a time frame and matching specific action types
    List<AuditLog> findByTimestampBetweenAndActionIn(LocalDateTime start, LocalDateTime end, List<String> actions);


    // Fetch all logs with pagination
    Page<AuditLog> findAll(Pageable pageable);

}

