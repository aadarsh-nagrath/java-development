package com.enterpriseshop.user.repository;

import com.enterpriseshop.user.entity.Address;
import com.enterpriseshop.user.entity.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Address entity
 * 
 * Provides methods for:
 * - Basic CRUD operations (inherited from JpaRepository)
 * - Finding addresses by user and type
 * - Finding default addresses
 * - Address validation queries
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    
    /**
     * Find all addresses for a specific user
     */
    List<Address> findByUserId(UUID userId);
    
    /**
     * Find addresses by user ID and address type
     */
    List<Address> findByUserIdAndAddressType(UUID userId, AddressType addressType);
    
    /**
     * Find default address for a user
     */
    Optional<Address> findByUserIdAndIsDefaultTrue(UUID userId);
    
    /**
     * Find default address by user ID and address type
     */
    Optional<Address> findByUserIdAndAddressTypeAndIsDefaultTrue(UUID userId, AddressType addressType);
    
    /**
     * Find active addresses for a user
     */
    List<Address> findByUserIdAndIsActiveTrue(UUID userId);
    
    /**
     * Find addresses by city
     */
    List<Address> findByCityIgnoreCaseContaining(String city);
    
    /**
     * Find addresses by state/province
     */
    List<Address> findByStateProvinceIgnoreCaseContaining(String stateProvince);
    
    /**
     * Find addresses by country
     */
    List<Address> findByCountryIgnoreCaseContaining(String country);
    
    /**
     * Find addresses by postal code
     */
    List<Address> findByPostalCode(String postalCode);
    
    /**
     * Find addresses by label (e.g., "Home", "Work")
     */
    List<Address> findByUserIdAndLabelIgnoreCase(UUID userId, String label);
    
    /**
     * Check if user has any addresses
     */
    boolean existsByUserId(UUID userId);
    
    /**
     * Check if user has a default address
     */
    boolean existsByUserIdAndIsDefaultTrue(UUID userId);
    
    /**
     * Count addresses by user
     */
    long countByUserId(UUID userId);
    
    /**
     * Count addresses by user and type
     */
    long countByUserIdAndAddressType(UUID userId, AddressType addressType);
    
    /**
     * Find addresses in a specific country
     */
    @Query("SELECT a FROM Address a WHERE LOWER(a.country) = LOWER(:country)")
    List<Address> findByCountryExact(@Param("country") String country);
    
    /**
     * Find addresses by city and country
     */
    @Query("SELECT a FROM Address a WHERE LOWER(a.city) = LOWER(:city) AND LOWER(a.country) = LOWER(:country)")
    List<Address> findByCityAndCountry(@Param("city") String city, @Param("country") String country);
    
    /**
     * Find addresses within a postal code range (for nearby delivery)
     */
    @Query("SELECT a FROM Address a WHERE a.postalCode BETWEEN :startPostalCode AND :endPostalCode")
    List<Address> findByPostalCodeRange(@Param("startPostalCode") String startPostalCode, 
                                       @Param("endPostalCode") String endPostalCode);
}
