package com.enterpriseshop.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * UserPreference entity representing user preferences and settings
 * 
 * This entity stores:
 * - Preference key-value pairs
 * - Preference categories (notifications, privacy, display)
 * - Timestamps for auditing
 */
@Entity
@Table(name = "user_preferences", indexes = {
    @Index(name = "idx_preference_user_id", columnList = "user_id"),
    @Index(name = "idx_preference_category", columnList = "category"),
    @Index(name = "idx_preference_key", columnList = "preference_key")
})
public class UserPreference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank(message = "Preference category is required")
    @Size(max = 50, message = "Category cannot exceed 50 characters")
    @Column(name = "category", nullable = false)
    private String category; // e.g., "notifications", "privacy", "display", "shopping"
    
    @NotBlank(message = "Preference key is required")
    @Size(max = 100, message = "Preference key cannot exceed 100 characters")
    @Column(name = "preference_key", nullable = false)
    private String preferenceKey; // e.g., "email_notifications", "dark_mode", "language"
    
    @Column(name = "preference_value", columnDefinition = "TEXT")
    private String preferenceValue; // The actual preference value
    
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    @Column(name = "description")
    private String description; // Human-readable description of the preference
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserPreference() {}
    
    public UserPreference(String category, String preferenceKey, String preferenceValue) {
        this.category = category;
        this.preferenceKey = preferenceKey;
        this.preferenceValue = preferenceValue;
    }
    
    public UserPreference(String category, String preferenceKey, String preferenceValue, String description) {
        this.category = category;
        this.preferenceKey = preferenceKey;
        this.preferenceValue = preferenceValue;
        this.description = description;
    }
    
    // Helper methods
    public String getFullKey() {
        return category + "." + preferenceKey;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getPreferenceKey() {
        return preferenceKey;
    }
    
    public void setPreferenceKey(String preferenceKey) {
        this.preferenceKey = preferenceKey;
    }
    
    public String getPreferenceValue() {
        return preferenceValue;
    }
    
    public void setPreferenceValue(String preferenceValue) {
        this.preferenceValue = preferenceValue;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "UserPreference{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", preferenceKey='" + preferenceKey + '\'' +
                ", preferenceValue='" + preferenceValue + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPreference that = (UserPreference) o;
        return id != null && id.equals(that.getId());
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
