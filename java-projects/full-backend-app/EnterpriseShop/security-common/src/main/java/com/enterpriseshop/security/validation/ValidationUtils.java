package com.enterpriseshop.security.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Validation Utilities for EnterpriseShop services
 * 
 * Provides:
 * - Common validation patterns
 * - Custom validation methods
 * - Validation result formatting
 * - Input sanitization
 */
@Component
public class ValidationUtils {
    
    private static final Validator validator;
    
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    // Common regex patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\+?[1-9]\\d{1,14}$"
    );
    
    private static final Pattern UUID_PATTERN = Pattern.compile(
        "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._-]{3,20}$"
    );
    
    /**
     * Validate an object using Bean Validation annotations
     */
    public static <T> ValidationResult<T> validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        
        if (violations.isEmpty()) {
            return new ValidationResult<>(true, null, object);
        }
        
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<T> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        
        return new ValidationResult<>(false, errors, object);
    }
    
    /**
     * Validate an object with a specific validation group
     */
    public static <T> ValidationResult<T> validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        
        if (violations.isEmpty()) {
            return new ValidationResult<>(true, null, object);
        }
        
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<T> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        
        return new ValidationResult<>(false, errors, object);
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validate UUID format
     */
    public static boolean isValidUuid(String uuid) {
        return uuid != null && UUID_PATTERN.matcher(uuid).matches();
    }
    
    /**
     * Validate password strength
     */
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Validate username format
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * Sanitize input string (remove potentially dangerous characters)
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        return input
            .replaceAll("<script[^>]*>.*?</script>", "") // Remove script tags
            .replaceAll("<[^>]*>", "") // Remove HTML tags
            .replaceAll("javascript:", "") // Remove javascript: protocol
            .replaceAll("on\\w+\\s*=", "") // Remove event handlers
            .trim();
    }
    
    /**
     * Validate and sanitize email
     */
    public static ValidationResult<String> validateAndSanitizeEmail(String email) {
        if (email == null) {
            return new ValidationResult<>(false, Map.of("email", "Email cannot be null"), null);
        }
        
        String sanitized = sanitizeInput(email);
        
        if (!isValidEmail(sanitized)) {
            return new ValidationResult<>(false, Map.of("email", "Invalid email format"), null);
        }
        
        return new ValidationResult<>(true, null, sanitized);
    }
    
    /**
     * Validate and sanitize phone number
     */
    public static ValidationResult<String> validateAndSanitizePhone(String phone) {
        if (phone == null) {
            return new ValidationResult<>(false, Map.of("phone", "Phone cannot be null"), null);
        }
        
        String sanitized = sanitizeInput(phone);
        
        if (!isValidPhone(sanitized)) {
            return new ValidationResult<>(false, Map.of("phone", "Invalid phone format"), null);
        }
        
        return new ValidationResult<>(true, null, sanitized);
    }
    
    /**
     * Validate and sanitize username
     */
    public static ValidationResult<String> validateAndSanitizeUsername(String username) {
        if (username == null) {
            return new ValidationResult<>(false, Map.of("username", "Username cannot be null"), null);
        }
        
        String sanitized = sanitizeInput(username);
        
        if (!isValidUsername(sanitized)) {
            return new ValidationResult<>(false, Map.of("username", "Username must be 3-20 characters, alphanumeric with ._-"), null);
        }
        
        return new ValidationResult<>(true, null, sanitized);
    }
    
    /**
     * Validate password with detailed feedback
     */
    public static ValidationResult<String> validatePassword(String password) {
        if (password == null) {
            return new ValidationResult<>(false, Map.of("password", "Password cannot be null"), null);
        }
        
        Map<String, String> errors = new HashMap<>();
        
        if (password.length() < 8) {
            errors.put("password", "Password must be at least 8 characters long");
        }
        
        if (!password.matches(".*[0-9].*")) {
            errors.put("password", "Password must contain at least one digit");
        }
        
        if (!password.matches(".*[a-z].*")) {
            errors.put("password", "Password must contain at least one lowercase letter");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            errors.put("password", "Password must contain at least one uppercase letter");
        }
        
        if (!password.matches(".*[@#$%^&+=!].*")) {
            errors.put("password", "Password must contain at least one special character (@#$%^&+=!)");
        }
        
        if (password.contains(" ")) {
            errors.put("password", "Password cannot contain spaces");
        }
        
        if (errors.isEmpty()) {
            return new ValidationResult<>(true, null, password);
        } else {
            return new ValidationResult<>(false, errors, null);
        }
    }
    
    /**
     * Validation result wrapper
     */
    public static class ValidationResult<T> {
        private final boolean valid;
        private final Map<String, String> errors;
        private final T data;
        
        public ValidationResult(boolean valid, Map<String, String> errors, T data) {
            this.valid = valid;
            this.errors = errors;
            this.data = data;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public Map<String, String> getErrors() {
            return errors;
        }
        
        public T getData() {
            return data;
        }
        
        public String getFirstError() {
            if (errors != null && !errors.isEmpty()) {
                return errors.values().iterator().next();
            }
            return null;
        }
    }
}
