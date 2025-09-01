package com.enterpriseshop.user.service;

import com.enterpriseshop.user.dto.UserDto;
import com.enterpriseshop.user.entity.User;
import com.enterpriseshop.user.repository.UserRepository;
import com.enterpriseshop.user.exception.ResourceNotFoundException;
import com.enterpriseshop.user.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for User entity business logic
 * 
 * Provides:
 * - User CRUD operations
 * - User search and filtering
 * - Business validation
 * - Transaction management
 */
@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private UserPreferenceService preferenceService;
    
    /**
     * Create a new user profile
     */
    public UserDto createUser(UserDto userDto) {
        // Validate that auth user ID is not already used
        if (userRepository.existsByAuthUserId(userDto.getAuthUserId())) {
            throw new BadRequestException("User profile already exists for this authentication user ID");
        }
        
        // Validate email uniqueness
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestException("Email address is already in use");
        }
        
        // Validate phone uniqueness if provided
        if (userDto.getPhone() != null && userRepository.existsByPhone(userDto.getPhone())) {
            throw new BadRequestException("Phone number is already in use");
        }
        
        // Create user entity
        User user = new User();
        user.setAuthUserId(userDto.getAuthUserId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAvatarUrl(userDto.getAvatarUrl());
        user.setBio(userDto.getBio());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setGender(userDto.getGender());
        user.setActive(true);
        user.setVerified(false);
        
        // Save user
        User savedUser = userRepository.save(user);
        
        // Convert to DTO and return
        return convertToDto(savedUser);
    }
    
    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        return convertToDto(user);
    }
    
    /**
     * Get user by authentication user ID
     */
    @Transactional(readOnly = true)
    public UserDto getUserByAuthUserId(UUID authUserId) {
        User user = userRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with auth user ID: " + authUserId));
        
        return convertToDto(user);
    }
    
    /**
     * Get user by email
     */
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        return convertToDto(user);
    }
    
    /**
     * Update user profile
     */
    public UserDto updateUser(UUID userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        // Check email uniqueness if changed
        if (!user.getEmail().equals(userDto.getEmail()) && 
            userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestException("Email address is already in use");
        }
        
        // Check phone uniqueness if changed
        if (userDto.getPhone() != null && !userDto.getPhone().equals(user.getPhone()) && 
            userRepository.existsByPhone(userDto.getPhone())) {
            throw new BadRequestException("Phone number is already in use");
        }
        
        // Update fields
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAvatarUrl(userDto.getAvatarUrl());
        user.setBio(userDto.getBio());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setGender(userDto.getGender());
        
        // Save and return
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    /**
     * Update user verification status
     */
    public UserDto updateVerificationStatus(UUID userId, boolean isVerified) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        user.setVerified(isVerified);
        User savedUser = userRepository.save(user);
        
        return convertToDto(savedUser);
    }
    
    /**
     * Update user active status
     */
    public UserDto updateActiveStatus(UUID userId, boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        user.setActive(isActive);
        User savedUser = userRepository.save(user);
        
        return convertToDto(savedUser);
    }
    
    /**
     * Update last login timestamp
     */
    public void updateLastLogin(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    /**
     * Delete user (soft delete by setting inactive)
     */
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        user.setActive(false);
        userRepository.save(user);
    }
    
    /**
     * Get all users with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToDto);
    }
    
    /**
     * Get active users with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserDto> getActiveUsers(Pageable pageable) {
        Page<User> users = userRepository.findByIsActiveTrue(pageable);
        return users.map(this::convertToDto);
    }
    
    /**
     * Search users by name
     */
    @Transactional(readOnly = true)
    public List<UserDto> searchUsersByName(String name) {
        List<User> users = userRepository.findByFullNameContainingIgnoreCase(name);
        return users.stream().map(this::convertToDto).toList();
    }
    
    /**
     * Search users by city
     */
    @Transactional(readOnly = true)
    public List<UserDto> searchUsersByCity(String city) {
        List<User> users = userRepository.findByCityContainingIgnoreCase(city);
        return users.stream().map(this::convertToDto).toList();
    }
    
    /**
     * Search users by country
     */
    @Transactional(readOnly = true)
    public List<UserDto> searchUsersByCountry(String country) {
        List<User> users = userRepository.findByCountryContainingIgnoreCase(country);
        return users.stream().map(this::convertToDto).toList();
    }
    
    /**
     * Get user statistics
     */
    @Transactional(readOnly = true)
    public UserStatistics getUserStatistics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByIsActive(true);
        long verifiedUsers = userRepository.countByIsVerified(true);
        long unverifiedUsers = userRepository.countByIsVerified(false);
        
        return new UserStatistics(totalUsers, activeUsers, verifiedUsers, unverifiedUsers);
    }
    
    /**
     * Convert User entity to UserDto
     */
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setAuthUserId(user.getAuthUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setBio(user.getBio());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender());
        dto.setActive(user.isActive());
        dto.setVerified(user.isVerified());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        // Convert addresses and preferences if needed
        // Note: This could be made configurable to avoid N+1 queries
        // For now, we'll leave them null and let the controller decide when to load them
        
        return dto;
    }
    
    /**
     * Inner class for user statistics
     */
    public static class UserStatistics {
        private final long totalUsers;
        private final long activeUsers;
        private final long verifiedUsers;
        private final long unverifiedUsers;
        
        public UserStatistics(long totalUsers, long activeUsers, long verifiedUsers, long unverifiedUsers) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.verifiedUsers = verifiedUsers;
            this.unverifiedUsers = unverifiedUsers;
        }
        
        // Getters
        public long getTotalUsers() { return totalUsers; }
        public long getActiveUsers() { return activeUsers; }
        public long getVerifiedUsers() { return verifiedUsers; }
        public long getUnverifiedUsers() { return unverifiedUsers; }
    }
}
