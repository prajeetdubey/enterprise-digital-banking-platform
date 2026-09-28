package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.entity.LoginHistory;
import com.prajeet.bankingsystem.repository.LoginHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;

    public LoginHistoryService(LoginHistoryRepository loginHistoryRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
    }

    // Successful login record karne ke liye
    public void recordSuccess(String email, String ipAddress) {

        LoginHistory record = LoginHistory.builder()
                .email(email)
                .success(true)
                .ipAddress(ipAddress)
                .loginTime(LocalDateTime.now())
                .build();

        loginHistoryRepository.save(record);
    }

    // Failed login record karne ke liye (galat password, user na milna, etc.)
    public void recordFailure(String email, String reason, String ipAddress) {

        LoginHistory record = LoginHistory.builder()
                .email(email)
                .success(false)
                .failureReason(reason)
                .ipAddress(ipAddress)
                .loginTime(LocalDateTime.now())
                .build();

        loginHistoryRepository.save(record);
    }

    public Page<LoginHistory> getAllLoginHistory(Pageable pageable) {
        return loginHistoryRepository.findAll(pageable);
    }

    public Page<LoginHistory> searchByEmail(String email, Pageable pageable) {
        return loginHistoryRepository.findByEmailContainingIgnoreCase(email, pageable);
    }

    public Page<LoginHistory> getFailedLogins(Pageable pageable) {
        return loginHistoryRepository.findBySuccess(false, pageable);
    }
}