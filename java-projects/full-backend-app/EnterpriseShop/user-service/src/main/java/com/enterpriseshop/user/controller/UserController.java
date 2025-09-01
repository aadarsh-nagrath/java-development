package com.enterpriseshop.user.controller;

import com.enterpriseshop.user.dto.UserDto;
import com.enterpriseshop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * REST Controller for User management
 * 
 * Provides endpoints for:
 * - User CRUD operations
 * - User search and filtering
 * - User statistics
 * - Pagination support
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Create a new user profile
     * POST /api/users
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    /**
     * Get user by ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Get user by authentication user ID
     * GET /api/users/auth/{authUserId}
     */
    @GetMapping("/auth/{authUserId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserByAuthUserId(@PathVariable UUID authUserId) {
        UserDto user = userService.getUserByAuthUserId(authUserId);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Get user by email
     * GET /api/users/email/{email}
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Update user profile
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Update user verification status
     * PATCH /api/users/{id}/verification
     */
    @PatchMapping("/{id}/verification")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateVerificationStatus(@PathVariable UUID id, @RequestParam boolean isVerified) {
        UserDto updatedUser = userService.updateVerificationStatus(id, isVerified);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Update user active status
     * PATCH /api/users/{id}/status
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateActiveStatus(@PathVariable UUID id, @RequestParam boolean isActive) {
        UserDto updatedUser = userService.updateActiveStatus(id, isActive);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Update last login timestamp
     * PATCH /api/users/{id}/last-login
     */
    @PatchMapping("/{id}/last-login")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> updateLastLogin(@PathVariable UUID id) {
        userService.updateLastLogin(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Delete user (soft delete)
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get all users with pagination
     * GET /api/users?page=0&size=20&sort=firstName,asc
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDto> users = userService.getAllUsers(pageable);
        
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get active users with pagination
     * GET /api/users/active?page=0&size=20
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDto>> getActiveUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<UserDto> users = userService.getActiveUsers(pageable);
        
        return ResponseEntity.ok(users);
    }
    
    /**
     * Search users by name
     * GET /api/users/search/name?q=john
     */
    @GetMapping("/search/name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> searchUsersByName(@RequestParam String q) {
        List<UserDto> users = userService.searchUsersByName(q);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Search users by city
     * GET /api/users/search/city?q=newyork
     */
    @GetMapping("/search/city")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> searchUsersByCity(@RequestParam String q) {
        List<UserDto> users = userService.searchUsersByCity(q);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Search users by country
     * GET /api/users/search/country?q=usa
     */
    @GetMapping("/search/country")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> searchUsersByCountry(@RequestParam String q) {
        List<UserDto> users = userService.searchUsersByCountry(q);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get user statistics
     * GET /api/users/statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserService.UserStatistics> getUserStatistics() {
        UserService.UserStatistics stats = userService.getUserStatistics();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Health check endpoint
     * GET /api/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "User Service");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
