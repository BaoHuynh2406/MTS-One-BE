package com.mts.mtsone.modules.auth.service.impl;

import com.mts.mtsone.common.exception.BusinessException;
import com.mts.mtsone.common.exception.DuplicateResourceException;
import com.mts.mtsone.common.exception.ResourceNotFoundException;
import com.mts.mtsone.modules.auth.dto.UserCreateDTO;
import com.mts.mtsone.modules.auth.dto.UserDTO;
import com.mts.mtsone.modules.auth.dto.UserUpdateDTO;
import com.mts.mtsone.modules.auth.entity.Role;
import com.mts.mtsone.modules.auth.entity.User;
import com.mts.mtsone.modules.auth.entity.UserRole;
import com.mts.mtsone.modules.auth.mapper.UserMapper;
import com.mts.mtsone.modules.auth.repository.RoleRepository;
import com.mts.mtsone.modules.auth.repository.UserRepository;
import com.mts.mtsone.modules.auth.repository.UserRoleRepository;
import com.mts.mtsone.modules.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(UserCreateDTO createDTO) {
        if (userRepository.existsByUsername(createDTO.getUsername())) {
            throw new DuplicateResourceException("User", "username", createDTO.getUsername());
        }
        if (createDTO.getEmail() != null && userRepository.existsByEmail(createDTO.getEmail())) {
            throw new DuplicateResourceException("User", "email", createDTO.getEmail());
        }

        User user = userMapper.toEntity(createDTO);
        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        user.setCreatedAt(new Date());
        user = userRepository.save(user);

        if (createDTO.getRoles() != null && !createDTO.getRoles().isEmpty()) {
            User finalUser = user;
            createDTO.getRoles().forEach(roleName -> {
                Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
                
                UserRole userRole = new UserRole();
                userRole.setUser(finalUser);
                userRole.setRole(role);
                userRole.setAssignedAt(LocalDateTime.now());
                userRoleRepository.save(userRole);
            });
        }

        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO updateUser(UUID id, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(user.getEmail()) 
            && userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new DuplicateResourceException("User", "email", updateDTO.getEmail());
        }

        userMapper.updateEntityFromDTO(updateDTO, user);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(UUID id) {
        return userRepository.findById(id)
            .map(userMapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        return userRepository.findByUsernameWithRoles(username)
            .map(userMapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Override
    public UserDTO getMyInfo() {
        //Lấy user từ token
        User currentUser = getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("User not found in the current context");
        }

        return userMapper.toDTO(currentUser);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("UNAUTHORIZED", "Người dùng chưa đăng nhập");
        }

        String username = authentication.getName();
        return userRepository.findByUsernameWithRoles(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    @Override
    public void updateUserRoles(UUID userId, String[] roleNames) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        userRoleRepository.deleteAll(user.getUserRoles());

        Arrays.stream(roleNames).forEach(roleName -> {
            Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
            
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRole.setAssignedAt(LocalDateTime.now());
            userRoleRepository.save(userRole);
        });
    }

    @Override
    public void activateUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void updateLastLogin(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setLastLoginAt(new Date());
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}