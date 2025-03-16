package com.mts.mtsone.modules.auth.service;

import com.mts.mtsone.modules.auth.dto.RoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    
    RoleDTO updateRole(Long id, RoleDTO roleDTO);
    
    void deleteRole(Long id);
    
    RoleDTO getRoleById(Long id);
    
    RoleDTO getRoleByName(String name);
    
    Page<RoleDTO> getAllRoles(Pageable pageable);
    
    void updateRolePermissions(Long roleId, Set<String> permissionNames);
    
    Set<RoleDTO> getRolesByNames(Set<String> roleNames);
} 