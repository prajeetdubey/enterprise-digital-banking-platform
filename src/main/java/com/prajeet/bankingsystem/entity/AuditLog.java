package com.prajeet.bankingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kisne request bheji (agar login nahi kiya to "anonymous")
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String httpMethod;   // GET, POST, PUT, DELETE

    @Column(nullable = false)
    private String endpoint;     // /api/transactions/search

    @Column(nullable = false)
    private int statusCode;      // 200, 401, 500 etc.

    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}