package com.enterpriseshop.auth.event;

import com.enterpriseshop.messaging.event.BaseEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when a user successfully registers
 * 
 * This event is consumed by:
 * - User Service: To create user profile
 * - Notification Service: To send welcome email
 * - Analytics Service: To track user acquisition
 */
public class UserRegisteredEvent extends BaseEvent {
    
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    
    public UserRegisteredEvent(String sourceService, UUID correlationId, UUID userId,
                             String username, String email, String firstName, 
                             String lastName, String phoneNumber) {
        super(sourceService, correlationId, userId);
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
    
    // Constructor for deserialization
    public UserRegisteredEvent(UUID eventId, LocalDateTime timestamp, String sourceService,
                             String eventVersion, UUID correlationId, UUID userId,
                             String username, String email, String firstName, 
                             String lastName, String phoneNumber) {
        super(eventId, timestamp, sourceService, eventVersion, correlationId, userId);
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
    
    @Override
    public String getEventType() {
        return "USER_REGISTERED";
    }
    
    @Override
    protected String getEventVersion() {
        return "1.0";
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    @Override
    public String toString() {
        return String.format("UserRegisteredEvent{eventId=%s, username='%s', email='%s', userId=%s}",
                getEventId(), username, email, getUserId());
    }
}
