package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.entity.LoginHistory;
import com.prajeet.bankingsystem.service.LoginHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login-history")
public class LoginHistoryController {

    private final LoginHistoryService loginHistoryService;

    public LoginHistoryController(LoginHistoryService loginHistoryService) {
        this.loginHistoryService = loginHistoryService;
    }

    @GetMapping
    public Page<LoginHistory> getAllLoginHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return loginHistoryService.getAllLoginHistory(pageable);
    }

    @GetMapping("/failed")
    public Page<LoginHistory> getFailedLogins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return loginHistoryService.getFailedLogins(pageable);
    }

    @GetMapping("/search")
    public Page<LoginHistory> searchByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return loginHistoryService.searchByEmail(email, pageable);
    }
}