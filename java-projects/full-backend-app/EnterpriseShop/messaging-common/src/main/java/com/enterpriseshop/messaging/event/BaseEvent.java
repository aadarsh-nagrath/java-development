package com.enterpriseshop.messaging.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base Event class for all domain events
 * 
 * Provides common fields and functionality for:
 * - Event identification
 * - Timestamp tracking
 * - Source service identification
 * - Event correlation
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseEvent {
    
    /**
     * Unique identifier for this event
     */
    private final UUID eventId;
    
    /**
     * When this event occurred
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private final LocalDateTime timestamp;
    
    /**
     * Which service generated this event
     */
    private final String sourceService;
    
    /**
     * Version of the event schema
     */
    private final String eventVersion;
    
    /**
     * Correlation ID for tracing related events
     */
    private final UUID correlationId;
    
    /**
     * User ID who triggered this event (if applicable)
     */
    private final UUID userId;
    
    /**
     * Constructor for creating new events
     */
    protected BaseEvent(String sourceService, UUID correlationId, UUID userId) {
        this.eventId = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.sourceService = sourceService;
        this.eventVersion = getEventVersion();
        this.correlationId = correlationId;
        this.userId = userId;
    }
    
    /**
     * Constructor for deserialization
     */
    protected BaseEvent(UUID eventId, LocalDateTime timestamp, String sourceService, 
                       String eventVersion, UUID correlationId, UUID userId) {
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.sourceService = sourceService;
        this.eventVersion = eventVersion;
        this.correlationId = correlationId;
        this.userId = userId;
    }
    
    /**
     * Get the event type for routing and processing
     */
    public abstract String getEventType();
    
    /**
     * Get the event version for schema evolution
     */
    protected abstract String getEventVersion();
    
    /**
     * Get the event ID
     */
    public UUID getEventId() {
        return eventId;
    }
    
    /**
     * Get the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Get the source service
     */
    public String getSourceService() {
        return sourceService;
    }
    
    /**
     * Get the event version
     */
    public String getEventVersion() {
        return eventVersion;
    }
    
    /**
     * Get the correlation ID
     */
    public UUID getCorrelationId() {
        return correlationId;
    }
    
    /**
     * Get the user ID
     */
    public UUID getUserId() {
        return userId;
    }
    
    @Override
    public String toString() {
        return String.format("%s{eventId=%s, timestamp=%s, sourceService='%s', eventType='%s'}",
                getClass().getSimpleName(), eventId, timestamp, sourceService, getEventType());
    }
}
