package com.enterpriseshop.auth.event;

import com.enterpriseshop.messaging.event.BaseEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event published when a user successfully logs in
 * 
 * This event is consumed by:
 * - Analytics Service: To track user engagement
 * - Security Service: To detect suspicious login patterns
 * - Notification Service: To send login notifications
 */
public class UserLoginEvent extends BaseEvent {
    
    private final String username;
    private final String email;
    private final String ipAddress;
    private final String userAgent;
    private final String loginMethod; // PASSWORD, OAUTH, SSO
    private final boolean isSuccessful;
    private final String failureReason; // null if successful
    
    public UserLoginEvent(String sourceService, UUID correlationId, UUID userId,
                         String username, String email, String ipAddress, 
                         String userAgent, String loginMethod, boolean isSuccessful, 
                         String failureReason) {
        super(sourceService, correlationId, userId);
        this.username = username;
        this.email = email;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.loginMethod = loginMethod;
        this.isSuccessful = isSuccessful;
        this.failureReason = failureReason;
    }
    
    // Constructor for deserialization
    public UserLoginEvent(UUID eventId, LocalDateTime timestamp, String sourceService,
                         String eventVersion, UUID correlationId, UUID userId,
                         String username, String email, String ipAddress, 
                         String userAgent, String loginMethod, boolean isSuccessful, 
                         String failureReason) {
        super(eventId, timestamp, sourceService, eventVersion, correlationId, userId);
        this.username = username;
        this.email = email;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.loginMethod = loginMethod;
        this.isSuccessful = isSuccessful;
        this.failureReason = failureReason;
    }
    
    @Override
    public String getEventType() {
        return "USER_LOGIN";
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
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public String getLoginMethod() {
        return loginMethod;
    }
    
    public boolean isSuccessful() {
        return isSuccessful;
    }
    
    public String getFailureReason() {
        return failureReason;
    }
    
    @Override
    public String toString() {
        return String.format("UserLoginEvent{eventId=%s, username='%s', email='%s', " +
                           "successful=%s, method='%s', userId=%s}",
                getEventId(), username, email, isSuccessful, loginMethod, getUserId());
    }
}
