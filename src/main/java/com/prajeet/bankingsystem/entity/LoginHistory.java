package com.prajeet.bankingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean success;

    // Agar login fail hua, to reason yahan store hoga (e.g. "Bad credentials")
    private String failureReason;

    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime loginTime;
}