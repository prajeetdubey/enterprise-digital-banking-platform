package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.entity.AuditLog;
import com.prajeet.bankingsystem.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void record(String username, String httpMethod, String endpoint,
                       int statusCode, String ipAddress) {

        AuditLog log = AuditLog.builder()
                .username(username)
                .httpMethod(httpMethod)
                .endpoint(endpoint)
                .statusCode(statusCode)
                .ipAddress(ipAddress)
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }

    public Page<AuditLog> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    public Page<AuditLog> searchByUsername(String username, Pageable pageable) {
        return auditLogRepository.findByUsernameContainingIgnoreCase(username, pageable);
    }

    public Page<AuditLog> searchByEndpoint(String endpoint, Pageable pageable) {
        return auditLogRepository.findByEndpointContainingIgnoreCase(endpoint, pageable);
    }
}