package com.enterpriseshop.auth.controller;

import com.enterpriseshop.auth.dto.AuthResponse;
import com.enterpriseshop.auth.dto.LoginRequest;
import com.enterpriseshop.auth.dto.RegisterRequest;
import com.enterpriseshop.auth.entity.User;
import com.enterpriseshop.auth.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * Register a new user.
     *
     * @param registerRequest the registration request
     * @return authentication response
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Registering new user: {}", registerRequest.getUsername());
        
        AuthResponse response = authService.registerUser(registerRequest);
        
        logger.info("User registered successfully: {}", registerRequest.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticate user and return JWT tokens.
     *
     * @param loginRequest the login request
     * @return authentication response
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Authenticating user: {}", loginRequest.getUsernameOrEmail());
        
        AuthResponse response = authService.authenticateUser(loginRequest);
        
        logger.info("User authenticated successfully: {}", loginRequest.getUsernameOrEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh access token using refresh token.
     *
     * @param refreshToken the refresh token
     * @return new authentication response
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        logger.info("Refreshing token");
        
        AuthResponse response = authService.refreshToken(refreshToken);
        
        logger.info("Token refreshed successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Logout user by invalidating refresh token.
     *
     * @param refreshToken the refresh token to invalidate
     * @return success message
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestParam String refreshToken) {
        logger.info("Logging out user");
        
        authService.logout(refreshToken);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User logged out successfully");
        
        logger.info("User logged out successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Get current authenticated user.
     *
     * @return current user information
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getCurrentUser() {
        logger.info("Getting current user information");
        
        User currentUser = authService.getCurrentUser();
        
        logger.info("Retrieved current user: {}", currentUser.getUsername());
        return ResponseEntity.ok(currentUser);
    }

    /**
     * Health check endpoint.
     *
     * @return health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "auth-service");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }
}
