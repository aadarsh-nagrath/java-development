package com.enterpriseshop.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import reactor.core.publisher.Mono;

import java.security.Principal;

/**
 * Rate Limiter Configuration for API Gateway
 * 
 * Configures:
 * - Redis-based rate limiting
 - Key resolution strategies
 * - Rate limit policies
 * - Burst capacity settings
 */
@Configuration
public class RateLimiterConfig {
    
    /**
     * Configure Redis rate limiter for auth service
     */
    @Bean("authRateLimiter")
    public RedisRateLimiter authRateLimiter() {
        return new RedisRateLimiter(5, 10); // 5 requests per second, burst of 10
    }
    
    /**
     * Configure Redis rate limiter for user service
     */
    @Bean("userRateLimiter")
    public RedisRateLimiter userRateLimiter() {
        return new RedisRateLimiter(20, 40); // 20 requests per second, burst of 40
    }
    
    /**
     * Configure Redis rate limiter for general API calls
     */
    @Bean("defaultRateLimiter")
    public RedisRateLimiter defaultRateLimiter() {
        return new RedisRateLimiter(10, 20); // 10 requests per second, burst of 20
    }
    
    /**
     * Key resolver for rate limiting based on user ID
     */
    @Bean("userKeyResolver")
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
            if (userId != null) {
                return Mono.just("user:" + userId);
            }
            return Mono.just("anonymous:" + exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        };
    }
    
    /**
     * Key resolver for rate limiting based on IP address
     */
    @Bean("ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            return Mono.just("ip:" + ip);
        };
    }
    
    /**
     * Key resolver for rate limiting based on authentication
     */
    @Bean("authKeyResolver")
    public KeyResolver authKeyResolver() {
        return exchange -> {
            Principal principal = exchange.getPrincipal().block();
            if (principal != null) {
                return Mono.just("auth:" + principal.getName());
            }
            return Mono.just("anonymous:" + exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        };
    }
    
    /**
     * Configure Redis serialization context
     */
    @Bean
    public RedisSerializationContext<String, String> redisSerializationContext() {
        return RedisSerializationContext.<String, String>newSerializationContext()
                .key(StringRedisSerializer.UTF_8)
                .value(StringRedisSerializer.UTF_8)
                .hashKey(StringRedisSerializer.UTF_8)
                .hashValue(StringRedisSerializer.UTF_8)
                .build();
    }
}
