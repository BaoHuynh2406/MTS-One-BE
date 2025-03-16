package com.mts.mtsone.modules.auth.service.impl;

import com.mts.mtsone.common.exception.DuplicateResourceException;
import com.mts.mtsone.common.exception.ResourceNotFoundException;
import com.mts.mtsone.modules.auth.dto.RoleDTO;
import com.mts.mtsone.modules.auth.entity.Permission;
import com.mts.mtsone.modules.auth.entity.Role;
import com.mts.mtsone.modules.auth.entity.RolePermission;
import com.mts.mtsone.modules.auth.mapper.RoleMapper;
import com.mts.mtsone.modules.auth.repository.PermissionRepository;
import com.mts.mtsone.modules.auth.repository.RolePermissionRepository;
import com.mts.mtsone.modules.auth.repository.RoleRepository;
import com.mts.mtsone.modules.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.getName())) {
            throw new DuplicateResourceException("Role", "name", roleDTO.getName());
        }

        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);

        if (roleDTO.getPermissions() != null && !roleDTO.getPermissions().isEmpty()) {
            Set<Permission> permissions = permissionRepository.findByNameIn(roleDTO.getPermissions());
            if (permissions.size() != roleDTO.getPermissions().size()) {
                Set<String> foundPermissions = permissions.stream()
                    .map(Permission::getName)
                    .collect(Collectors.toSet());
                Set<String> notFoundPermissions = roleDTO.getPermissions().stream()
                    .filter(p -> !foundPermissions.contains(p))
                    .collect(Collectors.toSet());
                throw new ResourceNotFoundException("Permission", "names", String.join(", ", notFoundPermissions));
            }

            Role finalRole = role;
            permissions.forEach(permission -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRole(finalRole);
                rolePermission.setPermission(permission);
                rolePermission.setAssignedAt(LocalDateTime.now());
                rolePermissionRepository.save(rolePermission);
            });
        }

        return roleMapper.toDTO(role);
    }

    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        if (!role.getName().equals(roleDTO.getName()) && roleRepository.existsByName(roleDTO.getName())) {
            throw new DuplicateResourceException("Role", "name", roleDTO.getName());
        }

        roleMapper.updateEntityFromDTO(roleDTO, role);
        return roleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role", "id", id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getRoleById(Long id) {
        return roleRepository.findById(id)
            .map(roleMapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getRoleByName(String name) {
        return roleRepository.findByNameWithPermissions(name)
            .map(roleMapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleDTO> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable).map(roleMapper::toDTO);
    }

    @Override
    public void updateRolePermissions(Long roleId, Set<String> permissionNames) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));

        rolePermissionRepository.deleteAll(role.getRolePermissions());

        if (permissionNames != null && !permissionNames.isEmpty()) {
            Set<Permission> permissions = permissionRepository.findByNameIn(permissionNames);
            if (permissions.size() != permissionNames.size()) {
                Set<String> foundPermissions = permissions.stream()
                    .map(Permission::getName)
                    .collect(Collectors.toSet());
                Set<String> notFoundPermissions = permissionNames.stream()
                    .filter(p -> !foundPermissions.contains(p))
                    .collect(Collectors.toSet());
                throw new ResourceNotFoundException("Permission", "names", String.join(", ", notFoundPermissions));
            }

            permissions.forEach(permission -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRole(role);
                rolePermission.setPermission(permission);
                rolePermission.setAssignedAt(LocalDateTime.now());
                rolePermissionRepository.save(rolePermission);
            });
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RoleDTO> getRolesByNames(Set<String> roleNames) {
        return roleNames.stream()
            .map(name -> roleRepository.findByNameWithPermissions(name)
                .map(roleMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name)))
            .collect(Collectors.toSet());
    }
} 