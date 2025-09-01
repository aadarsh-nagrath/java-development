package com.enterpriseshop.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for UserPreference entity
 * 
 * Used for:
 * - API requests and responses
 * - Preference creation and updates
 * - Preference validation
 */
public class UserPreferenceDto {
    
    private UUID id;
    
    private UUID userId;
    
    @NotBlank(message = "Preference category is required")
    @Size(max = 50, message = "Category cannot exceed 50 characters")
    private String category;
    
    @NotBlank(message = "Preference key is required")
    @Size(max = 100, message = "Preference key cannot exceed 100 characters")
    private String preferenceKey;
    
    private String preferenceValue;
    
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
    
    private boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserPreferenceDto() {}
    
    public UserPreferenceDto(String category, String preferenceKey, String preferenceValue) {
        this.category = category;
        this.preferenceKey = preferenceKey;
        this.preferenceValue = preferenceValue;
    }
    
    public UserPreferenceDto(String category, String preferenceKey, String preferenceValue, String description) {
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
    
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
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
        return "UserPreferenceDto{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", preferenceKey='" + preferenceKey + '\'' +
                ", preferenceValue='" + preferenceValue + '\'' +
                '}';
    }
}
