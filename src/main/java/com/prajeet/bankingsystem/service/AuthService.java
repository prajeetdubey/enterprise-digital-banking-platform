package com.prajeet.bankingsystem.service;

import com.prajeet.bankingsystem.dto.LoginRequestDTO;
import com.prajeet.bankingsystem.dto.LoginResponseDTO;
import com.prajeet.bankingsystem.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;   // ⚠️ NAYA import
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final LoginHistoryService loginHistoryService;   // ⚠️ NAYA

    public AuthService(AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       JwtService jwtService,
                       LoginHistoryService loginHistoryService) {   // ⚠️ NAYA parameter

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.loginHistoryService = loginHistoryService;   // ⚠️ NAYA
    }

    // ⚠️ SIGNATURE CHANGE — ab ek extra "ipAddress" parameter leta hai
    public LoginResponseDTO login(LoginRequestDTO request, String ipAddress) {

        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Load user details
            UserDetails user =
                    userDetailsService.loadUserByUsername(request.getEmail());

            // Generate Access Token
            String accessToken = jwtService.generateToken(user);

            // Generate Refresh Token
            String refreshToken = jwtService.generateRefreshToken(user);

            // ⚠️ NAYA — login success record karo
            loginHistoryService.recordSuccess(request.getEmail(), ipAddress);

            // Return both tokens
            return new LoginResponseDTO(
                    accessToken,
                    refreshToken
            );

        } catch (AuthenticationException ex) {
            // ⚠️ NAYA — login fail hua (galat password/email), record karo aur rethrow karo
            loginHistoryService.recordFailure(
                    request.getEmail(),
                    ex.getMessage(),
                    ipAddress
            );
            throw ex;   // taaki tumhara existing error-handling (401 etc.) waisa hi kaam kare
        }
    }

    public LoginResponseDTO refreshToken(String refreshToken) {

        String email = jwtService.extractUsername(refreshToken);

        UserDetails user =
                userDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        // Generate NEW access token
        String newAccessToken = jwtService.generateToken(user);

        // OPTIONAL: Generate NEW refresh token as well
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponseDTO(
                newAccessToken,
                newRefreshToken
        );
    }
}