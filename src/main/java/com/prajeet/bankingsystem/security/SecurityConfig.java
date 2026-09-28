package com.prajeet.bankingsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuditLoggingFilter auditLoggingFilter;   // ⚠️ NAYA

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          AuditLoggingFilter auditLoggingFilter) {   // ⚠️ NAYA parameter
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.auditLoggingFilter = auditLoggingFilter;   // ⚠️ NAYA
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Public APIs
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        // TEMPORARY (Development)
                        .requestMatchers("/api/branches/**").permitAll()
                        .requestMatchers("/api/accounts/**").permitAll()

                        .requestMatchers("/api/transactions/**").permitAll()

                        // ⚠️ NAYA — Audit & Login history sirf ADMIN dekh sakta hai
                        .requestMatchers("/api/audit-logs/**").hasRole("ADMIN")
                        .requestMatchers("/api/login-history/**").hasRole("ADMIN")

                        // Only ADMIN can see all users
                        .requestMatchers(HttpMethod.GET, "/users")
                        .hasRole("ADMIN")

                        // USER or ADMIN can view a user by id
                        .requestMatchers(HttpMethod.GET, "/users/*")
                        .hasAnyRole("USER", "ADMIN")

                        // Only ADMIN can update users
                        .requestMatchers(HttpMethod.PUT, "/users/*")
                        .hasRole("ADMIN")

                        // Only ADMIN can delete users
                        .requestMatchers(HttpMethod.DELETE, "/users/*")
                        .hasRole("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // ⚠️ NAYA — audit filter, JWT filter ke turant baad chalega
                .addFilterAfter(
                        auditLoggingFilter,
                        JwtAuthenticationFilter.class
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}