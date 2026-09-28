package com.prajeet.bankingsystem.controller;

import com.prajeet.bankingsystem.dto.LoginRequestDTO;
import com.prajeet.bankingsystem.dto.LoginResponseDTO;
import com.prajeet.bankingsystem.dto.UserRequestDTO;
import com.prajeet.bankingsystem.dto.UserResponseDTO;
import com.prajeet.bankingsystem.service.AuthService;
import com.prajeet.bankingsystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;   // ⚠️ NAYA import
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.prajeet.bankingsystem.dto.RefreshTokenRequestDTO;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService,
                          UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponseDTO register(
            @Valid @RequestBody UserRequestDTO request) {

        return userService.createUser(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request,
            HttpServletRequest httpRequest) {   // ⚠️ NAYA parameter — IP nikalne ke liye

        return authService.login(request, httpRequest.getRemoteAddr());   // ⚠️ NAYA — IP pass ho raha hai
    }
    @PostMapping("/refresh")
    public LoginResponseDTO refreshToken(
            @Valid @RequestBody RefreshTokenRequestDTO request) {

        return authService.refreshToken(
                request.getRefreshToken());
    }
}