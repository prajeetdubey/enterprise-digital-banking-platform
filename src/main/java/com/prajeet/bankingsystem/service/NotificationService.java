package com.prajeet.bankingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // ⚠️ Abhi ye sirf console pe print karta hai (mock).
    // Baad mein isme JavaMailSender (real email) ya Twilio (real SMS) laga sakte ho.

    public void sendEmail(String toEmail, String subject, String message) {

        logger.info("📧 [MOCK EMAIL] To: {} | Subject: {} | Message: {}",
                toEmail, subject, message);
    }

    public void sendSms(String phoneNumber, String message) {

        logger.info("📱 [MOCK SMS] To: {} | Message: {}",
                phoneNumber, message);
    }
}