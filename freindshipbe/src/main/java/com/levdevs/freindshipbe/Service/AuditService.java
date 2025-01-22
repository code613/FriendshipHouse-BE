package com.levdevs.freindshipbe.Service;

import com.levdevs.freindshipbe.DAO.AuditLogRepository;
import com.levdevs.freindshipbe.Entity.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String userId, String action) {
        logAction(userId, action, null);
    }

    public void logAction(String userId, String action, String details) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    // Retrieve audit logs within a time frame
    public List<AuditLog> getLogsByTimeFrame(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTimestampBetween(start, end);
    }

    // Retrieve audit logs of specific action types
    public List<AuditLog> getLogsByActions(List<String> actions) {
        return auditLogRepository.findByActionIn(actions);
    }

    // Retrieve audit logs by time frame and action types
    public List<AuditLog> getLogsByTimeFrameAndActions(LocalDateTime start, LocalDateTime end, List<String> actions) {
        return auditLogRepository.findByTimestampBetweenAndActionIn(start, end, actions);
    }

    // Get all logs with pagination
    public Page<AuditLog> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    // Get all logs (without pagination)
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

}

