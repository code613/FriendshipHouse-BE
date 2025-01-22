package com.levdevs.freindshipbe.controller;

import com.levdevs.freindshipbe.Entity.AuditLog;
import com.levdevs.freindshipbe.Service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    private final AuditService auditService;

    @Autowired
    public AuditLogController(AuditService auditService) {
        this.auditService = auditService;
    }

    // Endpoint to get logs within a specific time frame
    @GetMapping("/time-frame")
    public ResponseEntity<List<AuditLog>> getLogsByTimeFrame(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AuditLog> logs = auditService.getLogsByTimeFrame(start, end);
        return ResponseEntity.ok(logs);
    }

    // Endpoint to get logs by specific actions
    @GetMapping("/actions")
    public ResponseEntity<List<AuditLog>> getLogsByActions(@RequestParam List<String> actions) {
        List<AuditLog> logs = auditService.getLogsByActions(actions);
        return ResponseEntity.ok(logs);
    }

    // Endpoint to get logs by time frame and actions
    @GetMapping("/time-frame-and-actions")
    public ResponseEntity<List<AuditLog>> getLogsByTimeFrameAndActions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam List<String> actions) {
        List<AuditLog> logs = auditService.getLogsByTimeFrameAndActions(start, end, actions);
        return ResponseEntity.ok(logs);
    }

    // Endpoint to get all logs with pagination
    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        // Define the sort direction
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Create a Pageable object
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Fetch paginated logs
        Page<AuditLog> logs = auditService.getAllLogs(pageable);

        return ResponseEntity.ok(logs);
    }

    // Endpoint to get all logs without pagination
    @GetMapping("/all")
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        List<AuditLog> logs = auditService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}

