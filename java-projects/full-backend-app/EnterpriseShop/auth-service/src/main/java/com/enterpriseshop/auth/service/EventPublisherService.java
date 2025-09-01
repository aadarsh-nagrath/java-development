package com.enterpriseshop.auth.service;

import com.enterpriseshop.auth.event.UserLoginEvent;
import com.enterpriseshop.auth.event.UserRegisteredEvent;
import com.enterpriseshop.messaging.event.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service responsible for publishing events to Kafka topics
 * 
 * This service:
 * - Publishes user registration events
 * - Publishes user login events
 * - Handles event publishing failures
 * - Provides async publishing capabilities
 */
@Service
public class EventPublisherService {
    
    private static final Logger logger = LoggerFactory.getLogger(EventPublisherService.class);
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    // Topic names
    private static final String USER_EVENTS_TOPIC = "user-events";
    private static final String AUTH_EVENTS_TOPIC = "auth-events";
    
    @Autowired
    public EventPublisherService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    /**
     * Publish user registration event
     */
    public CompletableFuture<SendResult<String, Object>> publishUserRegistered(
            UUID userId, String username, String email, String firstName, 
            String lastName, String phoneNumber, UUID correlationId) {
        
        UserRegisteredEvent event = new UserRegisteredEvent(
                "auth-service", correlationId, userId, username, email, 
                firstName, lastName, phoneNumber
        );
        
        logger.info("Publishing user registration event: {}", event);
        
        return publishEvent(USER_EVENTS_TOPIC, userId.toString(), event)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        logger.error("Failed to publish user registration event for user {}: {}", 
                                   userId, throwable.getMessage());
                    } else {
                        logger.info("Successfully published user registration event for user {} to partition {} offset {}", 
                                  userId, result.getRecordMetadata().partition(), 
                                  result.getRecordMetadata().offset());
                    }
                });
    }
    
    /**
     * Publish user login event
     */
    public CompletableFuture<SendResult<String, Object>> publishUserLogin(
            UUID userId, String username, String email, String ipAddress, 
            String userAgent, String loginMethod, boolean isSuccessful, 
            String failureReason, UUID correlationId) {
        
        UserLoginEvent event = new UserLoginEvent(
                "auth-service", correlationId, userId, username, email, 
                ipAddress, userAgent, loginMethod, isSuccessful, failureReason
        );
        
        logger.info("Publishing user login event: {}", event);
        
        return publishEvent(AUTH_EVENTS_TOPIC, userId.toString(), event)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        logger.error("Failed to publish user login event for user {}: {}", 
                                   userId, throwable.getMessage());
                    } else {
                        logger.info("Successfully published user login event for user {} to partition {} offset {}", 
                                  userId, result.getRecordMetadata().partition(), 
                                  result.getRecordMetadata().offset());
                    }
                });
    }
    
    /**
     * Generic method to publish any event to a topic
     */
    private CompletableFuture<SendResult<String, Object>> publishEvent(
            String topic, String key, BaseEvent event) {
        
        try {
            return kafkaTemplate.send(topic, key, event);
        } catch (Exception e) {
            logger.error("Error publishing event to topic {}: {}", topic, e.getMessage());
            CompletableFuture<SendResult<String, Object>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }
    
    /**
     * Publish event synchronously (for critical events)
     */
    public void publishEventSync(String topic, String key, BaseEvent event) {
        try {
            kafkaTemplate.send(topic, key, event).get();
            logger.info("Successfully published event to topic {}: {}", topic, event.getEventType());
        } catch (Exception e) {
            logger.error("Failed to publish event to topic {}: {}", topic, e.getMessage());
            throw new RuntimeException("Event publishing failed", e);
        }
    }
}
