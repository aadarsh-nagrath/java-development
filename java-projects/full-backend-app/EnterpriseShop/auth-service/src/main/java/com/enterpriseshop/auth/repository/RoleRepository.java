package com.enterpriseshop.auth.repository;

import com.enterpriseshop.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Role entity operations.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    /**
     * Find role by name.
     *
     * @param name the role name to search for
     * @return Optional containing the role if found
     */
    Optional<Role> findByName(String name);

    /**
     * Check if a role exists by name.
     *
     * @param name the role name to check
     * @return true if role exists, false otherwise
     */
    boolean existsByName(String name);
}
