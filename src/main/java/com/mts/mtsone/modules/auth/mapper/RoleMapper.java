package com.mts.mtsone.modules.auth.mapper;

import com.mts.mtsone.modules.auth.dto.RoleDTO;
import com.mts.mtsone.modules.auth.entity.Role;
import com.mts.mtsone.modules.auth.entity.RolePermission;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    @Mapping(target = "permissions", source = "rolePermissions", qualifiedByName = "rolePermissionsToPermissionNames")
    RoleDTO toDTO(Role role);

    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    @Named("rolePermissionsToPermissionNames")
    default Set<String> rolePermissionsToPermissionNames(Set<RolePermission> rolePermissions) {
        if (rolePermissions == null) return null;
        return rolePermissions.stream()
            .map(rolePermission -> rolePermission.getPermission().getName())
            .collect(Collectors.toSet());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RoleDTO roleDTO, @MappingTarget Role role);
} 