package com.example.service;

import com.example.dto.UserRegisteredEvent;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void sendWelcomeEmail(UserRegisteredEvent event) {
        logger.info("Sending welcome email to: {} at {}", event.getUsername(), event.getEmail());
        // Business logic for sending welcome email
        // In a real application, this would integrate with an email service
    }
}
