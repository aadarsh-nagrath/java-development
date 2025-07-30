package com.example.restapi.controller;

import com.example.restapi.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for User operations
 * This class handles HTTP requests for user-related operations
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // Simulating a database with a list of users
    private final List<User> users = new ArrayList<>();

    // Constructor to initialize some sample data
    public UserController() {
        users.add(new User(1L, "John Doe", "john@example.com", 25));
        users.add(new User(2L, "Jane Smith", "jane@example.com", 30));
        users.add(new User(3L, "Bob Johnson", "bob@example.com", 35));
    }

    /**
     * GET /api/users
     * Returns all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/{id}
     * Returns a specific user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/users/search?name={name}
     * Returns users by name (case-insensitive search)
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String name) {
        List<User> foundUsers = users.stream()
                .filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        
        return ResponseEntity.ok(foundUsers);
    }

    /**
     * GET /api/users/count
     * Returns the total number of users
     */
    @GetMapping("/count")
    public ResponseEntity<String> getUserCount() {
        return ResponseEntity.ok("Total users: " + users.size());
    }

    /**
     * GET /api/health
     * Simple health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("API is running!");
    }
} 