package com.enterpriseshop.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT Authentication Filter for API Gateway
 * 
 * Intercepts all requests and validates JWT tokens
 * Sets security context for downstream services
 * Handles authentication and authorization
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    
    @Autowired
    private JwtTokenValidator jwtTokenValidator;
    
    // Public endpoints that don't require authentication
    private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
        "/health",
        "/actuator/health",
        "/actuator/info",
        "/docs",
        "/swagger-ui",
        "/v3/api-docs"
    );
    
    // Auth endpoints that don't require existing token
    private static final List<String> AUTH_ENDPOINTS = Arrays.asList(
        "/auth/api/auth/login",
        "/auth/api/auth/register",
        "/auth/api/auth/refresh"
    );
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        
        // Check if endpoint is public
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }
        
        // Check if endpoint is auth-related
        if (isAuthEndpoint(path)) {
            return chain.filter(exchange);
        }
        
        // Extract and validate JWT token
        String token = extractToken(request);
        
        if (!StringUtils.hasText(token)) {
            return onError(exchange, "No JWT token provided", HttpStatus.UNAUTHORIZED);
        }
        
        if (!jwtTokenValidator.validateToken(token)) {
            return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
        
        if (jwtTokenValidator.isTokenExpired(token)) {
            return onError(exchange, "JWT token expired", HttpStatus.UNAUTHORIZED);
        }
        
        // Set security context
        setSecurityContext(token);
        
        // Add user info to headers for downstream services
        ServerHttpRequest modifiedRequest = addUserInfoToHeaders(request, token);
        
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }
    
    @Override
    public int getOrder() {
        return -100; // High priority filter
    }
    
    /**
     * Check if endpoint is public (no authentication required)
     */
    private boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(path::startsWith);
    }
    
    /**
     * Check if endpoint is auth-related (login, register, etc.)
     */
    private boolean isAuthEndpoint(String path) {
        return AUTH_ENDPOINTS.stream().anyMatch(path::startsWith);
    }
    
    /**
     * Extract JWT token from Authorization header
     */
    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
    
    /**
     * Set security context for the current request
     */
    private void setSecurityContext(String token) {
        String username = jwtTokenValidator.getUsernameFromToken(token);
        String userId = jwtTokenValidator.getUserIdFromToken(token);
        String[] roles = jwtTokenValidator.getRolesFromToken(token);
        
        if (username != null) {
            List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("ROLE_", "")))
                    .collect(Collectors.toList());
            
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(username, null, authorities);
            
            // Set additional details
            authentication.setDetails(new JwtAuthenticationDetails(userId, username, roles));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
    
    /**
     * Add user information to request headers for downstream services
     */
    private ServerHttpRequest addUserInfoToHeaders(ServerHttpRequest request, String token) {
        String username = jwtTokenValidator.getUsernameFromToken(token);
        String userId = jwtTokenValidator.getUserIdFromToken(token);
        String[] roles = jwtTokenValidator.getRolesFromToken(token);
        
        return request.mutate()
                .header("X-User-Id", userId)
                .header("X-Username", username)
                .header("X-User-Roles", String.join(",", roles))
                .build();
    }
    
    /**
     * Handle authentication errors
     */
    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        
        String errorResponse = String.format(
            "{\"error\": \"%s\", \"message\": \"%s\", \"status\": %d}",
            status.getReasonPhrase(),
            message,
            status.value()
        );
        
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.getBytes())));
    }
    
    /**
     * Custom authentication details class
     */
    private static class JwtAuthenticationDetails {
        private final String userId;
        private final String username;
        private final String[] roles;
        
        public JwtAuthenticationDetails(String userId, String username, String[] roles) {
            this.userId = userId;
            this.username = username;
            this.roles = roles;
        }
        
        public String getUserId() { return userId; }
        public String getUsername() { return username; }
        public String[] getRoles() { return roles; }
    }
}
