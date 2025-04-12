package com.mts.mtsone.modules.auth.repository;

import com.mts.mtsone.modules.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(String username);
    
    @Query("SELECT u FROM User u WHERE " +
           "(:active IS NULL OR u.isActive = :active) AND " +
           "(:searchKey IS NULL OR :searchKey = '' OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR " +
           "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR " +
           "LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchKey, '%')))")
    Page<User> findAllWithFilters(
        @Param("active") Boolean active,
        @Param("searchKey") String searchKey,
        Pageable pageable
    );
}