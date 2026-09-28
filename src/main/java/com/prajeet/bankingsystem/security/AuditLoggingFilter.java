package com.prajeet.bankingsystem.security;

import com.prajeet.bankingsystem.service.AuditLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuditLoggingFilter extends OncePerRequestFilter {

    private final AuditLogService auditLogService;

    public AuditLoggingFilter(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Pehle request ko aage jaane do — controller tak, response ban ne do
        filterChain.doFilter(request, response);

        // Response ban jaane ke baad hi log likhenge (taaki status code pata chale)
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String username = (auth != null && auth.isAuthenticated())
                    ? auth.getName()
                    : "anonymous";

            auditLogService.record(
                    username,
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    request.getRemoteAddr()
            );
        } catch (Exception ex) {
            // Audit logging kabhi bhi actual request ko fail nahi karni chahiye
            // isliye yahan exception ko silently ignore/log kar rahe hain
            logger.warn("Audit logging failed: " + ex.getMessage());
        }
    }
}