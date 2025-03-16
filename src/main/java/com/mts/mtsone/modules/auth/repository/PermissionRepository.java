package com.mts.mtsone.modules.auth.repository;

import com.mts.mtsone.modules.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    
    boolean existsByName(String name);
    
    Set<Permission> findByNameIn(Set<String> names);
} 