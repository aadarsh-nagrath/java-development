package com.enterpriseshop.user.repository;

import com.enterpriseshop.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity
 * 
 * Provides methods for:
 * - Basic CRUD operations (inherited from JpaRepository)
 * - Custom queries for finding users
 * - Pagination support
 * - Search functionality
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    /**
     * Find user by authentication user ID (from auth-service)
     */
    Optional<User> findByAuthUserId(UUID authUserId);
    
    /**
     * Find user by email address
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by phone number
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if user exists by phone
     */
    boolean existsByPhone(String phone);
    
    /**
     * Check if user exists by auth user ID
     */
    boolean existsByAuthUserId(UUID authUserId);
    
    /**
     * Find active users
     */
    List<User> findByIsActiveTrue();
    
    /**
     * Find verified users
     */
    List<User> findByIsVerifiedTrue();
    
    /**
     * Find users by first name (case-insensitive)
     */
    List<User> findByFirstNameIgnoreCaseContaining(String firstName);
    
    /**
     * Find users by last name (case-insensitive)
     */
    List<User> findByLastNameIgnoreCaseContaining(String lastName);
    
    /**
     * Find users by full name (case-insensitive)
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByFullNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Find users by city
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.addresses a WHERE LOWER(a.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<User> findByCityContainingIgnoreCase(@Param("city") String city);
    
    /**
     * Find users by country
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.addresses a WHERE LOWER(a.country) LIKE LOWER(CONCAT('%', :country, '%'))")
    List<User> findByCountryContainingIgnoreCase(@Param("country") String country);
    
    /**
     * Find users with pagination
     */
    Page<User> findAll(Pageable pageable);
    
    /**
     * Find active users with pagination
     */
    Page<User> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find users by creation date range
     */
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                     @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find users who haven't logged in recently
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt IS NULL OR u.lastLoginAt < :cutoffDate")
    List<User> findInactiveUsers(@Param("cutoffDate") java.time.LocalDateTime cutoffDate);
    
    /**
     * Count users by verification status
     */
    long countByIsVerified(boolean isVerified);
    
    /**
     * Count users by active status
     */
    long countByIsActive(boolean isActive);
}
