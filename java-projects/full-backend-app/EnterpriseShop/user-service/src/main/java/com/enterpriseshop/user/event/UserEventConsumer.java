package com.enterpriseshop.user.event;

import com.enterpriseshop.auth.event.UserRegisteredEvent;
import com.enterpriseshop.user.entity.User;
import com.enterpriseshop.user.entity.Address;
import com.enterpriseshop.user.entity.UserPreference;
import com.enterpriseshop.user.repository.UserRepository;
import com.enterpriseshop.user.repository.AddressRepository;
import com.enterpriseshop.user.repository.UserPreferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Consumer for user-related events from Kafka
 * 
 * This consumer:
 * - Listens to user-events topic
 * - Creates user profiles when users register
 * - Handles user updates and deletions
 * - Maintains data consistency between services
 */
@Service
public class UserEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);
    
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    
    @Autowired
    public UserEventConsumer(UserRepository userRepository, 
                           AddressRepository addressRepository,
                           UserPreferenceRepository userPreferenceRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.userPreferenceRepository = userPreferenceRepository;
    }
    
    /**
     * Handle user registration events
     * 
     * When a user registers in the auth service, this consumer:
     * 1. Creates a user profile in the user service
     * 2. Sets up default preferences
     * 3. Creates a default address
     */
    @KafkaListener(
        topics = "user-events",
        groupId = "user-service-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleUserEvent(@Payload UserRegisteredEvent event,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                               @Header(KafkaHeaders.OFFSET) long offset) {
        
        logger.info("Received user event from topic {} partition {} offset {}: {}", 
                   topic, partition, offset, event);
        
        try {
            if ("USER_REGISTERED".equals(event.getEventType())) {
                handleUserRegistered(event);
            } else {
                logger.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            logger.error("Error processing user event {}: {}", event.getEventId(), e.getMessage(), e);
            // In a real application, you might want to send to dead letter queue
            throw e; // Re-throw to trigger retry mechanism
        }
    }
    
    /**
     * Handle user registration by creating user profile
     */
    private void handleUserRegistered(UserRegisteredEvent event) {
        logger.info("Creating user profile for registered user: {}", event.getUserId());
        
        // Check if user profile already exists
        if (userRepository.existsById(event.getUserId())) {
            logger.warn("User profile already exists for user: {}", event.getUserId());
            return;
        }
        
        // Create user profile
        User user = new User();
        user.setId(event.getUserId());
        user.setUsername(event.getUsername());
        user.setEmail(event.getEmail());
        user.setFirstName(event.getFirstName());
        user.setLastName(event.getLastName());
        user.setPhoneNumber(event.getPhoneNumber());
        user.setActive(true);
        user.setVerified(true);
        user.setCreatedAt(event.getTimestamp());
        user.setUpdatedAt(event.getTimestamp());
        
        // Save user
        User savedUser = userRepository.save(user);
        logger.info("Created user profile: {}", savedUser.getId());
        
        // Create default address
        Address defaultAddress = new Address();
        defaultAddress.setUser(savedUser);
        defaultAddress.setAddressType(com.enterpriseshop.user.entity.AddressType.BOTH);
        defaultAddress.setStreetAddress("Default Address");
        defaultAddress.setCity("Default City");
        defaultAddress.setState("Default State");
        defaultAddress.setPostalCode("00000");
        defaultAddress.setCountry("Default Country");
        defaultAddress.setIsDefault(true);
        defaultAddress.setCreatedAt(event.getTimestamp());
        defaultAddress.setUpdatedAt(event.getTimestamp());
        
        addressRepository.save(defaultAddress);
        logger.info("Created default address for user: {}", savedUser.getId());
        
        // Create default preferences
        UserPreference defaultPreferences = new UserPreference();
        defaultPreferences.setUser(savedUser);
        defaultPreferences.setPreferenceKey("language");
        defaultPreferences.setPreferenceValue("en");
        defaultPreferences.setCreatedAt(event.getTimestamp());
        defaultPreferences.setUpdatedAt(event.getTimestamp());
        
        userPreferenceRepository.save(defaultPreferences);
        logger.info("Created default preferences for user: {}", savedUser.getId());
        
        logger.info("Successfully completed user profile setup for user: {}", savedUser.getId());
    }
}
