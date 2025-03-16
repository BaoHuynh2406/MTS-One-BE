package com.mts.mtsone.modules.auth.repository;

import com.mts.mtsone.modules.auth.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Set<UserRole> findByUserId(UUID userId);
    
    void deleteByUserIdAndRoleId(UUID userId, Long roleId);
    
    boolean existsByUserIdAndRoleId(UUID userId, Long roleId);
    
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.name IN :roleNames")
    Set<UserRole> findByUserIdAndRoleNames(UUID userId, Set<String> roleNames);
} 