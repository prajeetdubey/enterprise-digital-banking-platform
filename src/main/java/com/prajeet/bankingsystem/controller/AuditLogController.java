package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.entity.AuditLog;
import com.prajeet.bankingsystem.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public Page<AuditLog> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return auditLogService.getAllLogs(pageable);
    }

    @GetMapping("/search")
    public Page<AuditLog> searchLogs(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String endpoint,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (username != null) {
            return auditLogService.searchByUsername(username, pageable);
        }

        if (endpoint != null) {
            return auditLogService.searchByEndpoint(endpoint, pageable);
        }

        return auditLogService.getAllLogs(pageable);
    }
}