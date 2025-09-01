package com.enterpriseshop.user.repository;

import com.enterpriseshop.user.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for UserPreference entity
 * 
 * Provides methods for:
 * - Basic CRUD operations (inherited from JpaRepository)
 * - Finding preferences by user and category
 * - Finding specific preference values
 * - Preference management queries
 */
@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, UUID> {
    
    /**
     * Find all preferences for a specific user
     */
    List<UserPreference> findByUserId(UUID userId);
    
    /**
     * Find preferences by user ID and category
     */
    List<UserPreference> findByUserIdAndCategory(UUID userId, String category);
    
    /**
     * Find a specific preference by user ID, category, and key
     */
    Optional<UserPreference> findByUserIdAndCategoryAndPreferenceKey(UUID userId, String category, String preferenceKey);
    
    /**
     * Find preferences by user ID and active status
     */
    List<UserPreference> findByUserIdAndIsActiveTrue(UUID userId);
    
    /**
     * Find preferences by category across all users
     */
    List<UserPreference> findByCategory(String category);
    
    /**
     * Find preferences by category and key across all users
     */
    List<UserPreference> findByCategoryAndPreferenceKey(String category, String preferenceKey);
    
    /**
     * Find preferences by value (useful for finding users with specific settings)
     */
    List<UserPreference> findByPreferenceValue(String preferenceValue);
    
    /**
     * Find preferences by user ID and preference key (across all categories)
     */
    List<UserPreference> findByUserIdAndPreferenceKey(UUID userId, String preferenceKey);
    
    /**
     * Check if a specific preference exists for a user
     */
    boolean existsByUserIdAndCategoryAndPreferenceKey(UUID userId, String category, String preferenceKey);
    
    /**
     * Count preferences by user
     */
    long countByUserId(UUID userId);
    
    /**
     * Count preferences by user and category
     */
    long countByUserIdAndCategory(UUID userId, String category);
    
    /**
     * Find preferences by partial key match
     */
    @Query("SELECT p FROM UserPreference p WHERE p.userId = :userId AND p.preferenceKey LIKE %:keyPattern%")
    List<UserPreference> findByUserIdAndPreferenceKeyPattern(@Param("userId") UUID userId, 
                                                           @Param("keyPattern") String keyPattern);
    
    /**
     * Find preferences by partial value match
     */
    @Query("SELECT p FROM UserPreference p WHERE p.userId = :userId AND p.preferenceValue LIKE %:valuePattern%")
    List<UserPreference> findByUserIdAndPreferenceValuePattern(@Param("userId") UUID userId, 
                                                             @Param("valuePattern") String valuePattern);
    
    /**
     * Find users with specific preference value
     */
    @Query("SELECT p FROM UserPreference p WHERE p.category = :category AND p.preferenceKey = :key AND p.preferenceValue = :value")
    List<UserPreference> findByCategoryAndKeyAndValue(@Param("category") String category, 
                                                     @Param("key") String key, 
                                                     @Param("value") String value);
    
    /**
     * Delete all preferences for a specific user
     */
    void deleteByUserId(UUID userId);
    
    /**
     * Delete preferences by user ID and category
     */
    void deleteByUserIdAndCategory(UUID userId, String category);
    
    /**
     * Delete a specific preference
     */
    void deleteByUserIdAndCategoryAndPreferenceKey(UUID userId, String category, String preferenceKey);
}
