package com.enterpriseshop.auth.repository;

import com.enterpriseshop.auth.entity.RefreshToken;
import com.enterpriseshop.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for RefreshToken entity operations.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    /**
     * Find refresh token by token string.
     *
     * @param token the token string to search for
     * @return Optional containing the refresh token if found
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Find refresh token by user.
     *
     * @param user the user to search for
     * @return Optional containing the refresh token if found
     */
    Optional<RefreshToken> findByUser(User user);

    /**
     * Delete refresh token by user.
     *
     * @param user the user whose refresh token should be deleted
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteByUser(@Param("user") User user);

    /**
     * Delete expired refresh tokens.
     *
     * @param now the current time to compare against
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    /**
     * Find all expired refresh tokens.
     *
     * @param now the current time to compare against
     * @return list of expired refresh tokens
     */
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.expiresAt < :now")
    java.util.List<RefreshToken> findExpiredTokens(@Param("now") LocalDateTime now);
}
