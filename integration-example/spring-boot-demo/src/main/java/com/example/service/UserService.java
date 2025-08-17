package com.example.service;

import com.example.dto.UserRegisteredEvent;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void welcomeNewUser(UserRegisteredEvent event) {
        logger.info("Welcoming new user: {} ({})", event.getUsername(), event.getUserId());
        // Business logic for welcoming new user
    }

    public void setupUserProfile(UserRegisteredEvent event) {
        logger.info("Setting up profile for user: {} with email: {}", event.getUsername(), event.getEmail());
        // Business logic for setting up user profile
    }
}
