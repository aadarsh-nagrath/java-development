package com.enterpriseshop.auth.service;

import com.enterpriseshop.auth.dto.AuthResponse;
import com.enterpriseshop.auth.dto.LoginRequest;
import com.enterpriseshop.auth.dto.RegisterRequest;
import com.enterpriseshop.auth.entity.RefreshToken;
import com.enterpriseshop.auth.entity.Role;
import com.enterpriseshop.auth.entity.User;
import com.enterpriseshop.auth.exception.BadRequestException;
import com.enterpriseshop.auth.exception.ResourceNotFoundException;
import com.enterpriseshop.auth.repository.RefreshTokenRepository;
import com.enterpriseshop.auth.repository.RoleRepository;
import com.enterpriseshop.auth.repository.UserRepository;
import com.enterpriseshop.auth.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Service for handling authentication operations.
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * Authenticate user and return JWT tokens.
     *
     * @param loginRequest the login request
     * @return authentication response with tokens
     */
    @Transactional
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        logger.debug("Authenticating user: {}", loginRequest.getUsernameOrEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateAccessToken(authentication);
        
        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String refreshToken = tokenProvider.generateRefreshToken(user);
        
        // Save refresh token to database
        saveRefreshToken(user, refreshToken);

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());

        return new AuthResponse(
                jwt,
                refreshToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roles,
                tokenProvider.getTokenExpirationAsLocalDateTime(jwt)
        );
    }

    /**
     * Register a new user.
     *
     * @param registerRequest the registration request
     * @return authentication response with tokens
     */
    @Transactional
    public AuthResponse registerUser(RegisterRequest registerRequest) {
        logger.debug("Registering new user: {}", registerRequest.getUsername());

        // Validate passwords match
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Check if username exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setActive(true);
        user.setVerified(true); // For demo purposes, set as verified

        // Assign default role
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        // Generate tokens
        String jwt = tokenProvider.generateAccessToken(savedUser);
        String refreshToken = tokenProvider.generateRefreshToken(savedUser);
        
        // Save refresh token
        saveRefreshToken(savedUser, refreshToken);

        Set<String> roleNames = savedUser.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());

        return new AuthResponse(
                jwt,
                refreshToken,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                roleNames,
                tokenProvider.getTokenExpirationAsLocalDateTime(jwt)
        );
    }

    /**
     * Refresh access token using refresh token.
     *
     * @param refreshToken the refresh token
     * @return new authentication response
     */
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        logger.debug("Refreshing token");

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }

        String tokenType = tokenProvider.getTokenType(refreshToken);
        if (!"refresh".equals(tokenType)) {
            throw new BadRequestException("Token is not a refresh token");
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify refresh token exists in database
        RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new BadRequestException("Refresh token not found"));

        if (storedRefreshToken.isExpired()) {
            refreshTokenRepository.delete(storedRefreshToken);
            throw new BadRequestException("Refresh token has expired");
        }

        // Generate new tokens
        String newJwt = tokenProvider.generateAccessToken(user);
        String newRefreshToken = tokenProvider.generateRefreshToken(user);

        // Update refresh token in database
        refreshTokenRepository.delete(storedRefreshToken);
        saveRefreshToken(user, newRefreshToken);

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());

        return new AuthResponse(
                newJwt,
                newRefreshToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roles,
                tokenProvider.getTokenExpirationAsLocalDateTime(newJwt)
        );
    }

    /**
     * Logout user by invalidating refresh token.
     *
     * @param refreshToken the refresh token to invalidate
     */
    @Transactional
    public void logout(String refreshToken) {
        logger.debug("Logging out user");

        RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElse(null);

        if (storedRefreshToken != null) {
            refreshTokenRepository.delete(storedRefreshToken);
        }
    }

    /**
     * Save refresh token to database.
     *
     * @param user the user
     * @param refreshToken the refresh token
     */
    private void saveRefreshToken(User user, String refreshToken) {
        LocalDateTime expiresAt = tokenProvider.getTokenExpirationAsLocalDateTime(refreshToken);
        
        // Delete existing refresh token for user
        refreshTokenRepository.deleteByUser(user);
        
        // Save new refresh token
        RefreshToken newRefreshToken = new RefreshToken(user, refreshToken, expiresAt);
        refreshTokenRepository.save(newRefreshToken);
    }

    /**
     * Get current authenticated user.
     *
     * @return the current user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    /**
     * Get user by ID.
     *
     * @param userId the user ID
     * @return the user
     */
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}
