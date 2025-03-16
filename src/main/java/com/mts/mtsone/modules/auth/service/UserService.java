package com.mts.mtsone.modules.auth.service;

import com.mts.mtsone.modules.auth.dto.UserCreateDTO;
import com.mts.mtsone.modules.auth.dto.UserDTO;
import com.mts.mtsone.modules.auth.dto.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserCreateDTO createDTO);
    
    UserDTO updateUser(UUID id, UserUpdateDTO updateDTO);
    
    void deleteUser(UUID id);
    
    UserDTO getUserById(UUID id);
    
    UserDTO getUserByUsername(String username);
    
    Page<UserDTO> getAllUsers(Pageable pageable);
    
    void updateUserRoles(UUID userId, String[] roleNames);
    
    void activateUser(UUID id);
    
    void deactivateUser(UUID id);
    
    void updateLastLogin(UUID id);

    List<UserDTO> getAllUsers();
}