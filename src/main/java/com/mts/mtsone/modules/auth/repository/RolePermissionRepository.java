package com.mts.mtsone.modules.auth.repository;

import com.mts.mtsone.modules.auth.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    Set<RolePermission> findByRoleId(Long roleId);
    
    void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);
    
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
    
    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.name = :roleName")
    Set<RolePermission> findByRoleName(String roleName);
    
    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.name IN :roleNames")
    Set<RolePermission> findByRoleNames(Set<String> roleNames);
} 