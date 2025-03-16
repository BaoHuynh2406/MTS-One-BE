package com.mts.mtsone.modules.auth.mapper;

import com.mts.mtsone.modules.auth.dto.UserCreateDTO;
import com.mts.mtsone.modules.auth.dto.UserDTO;
import com.mts.mtsone.modules.auth.dto.UserUpdateDTO;
import com.mts.mtsone.modules.auth.entity.User;
import com.mts.mtsone.modules.auth.entity.UserRole;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "userRolesToRoleNames")
    UserDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "active", constant = "true")
    User toEntity(UserCreateDTO createDTO);

    @Named("userRolesToRoleNames")
    default Set<String> userRolesToRoleNames(Set<UserRole> userRoles) {
        if (userRoles == null) return null;
        return userRoles.stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toSet());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UserUpdateDTO updateDTO, @MappingTarget User user);
}