package com.mts.mtsone.modules.auth.dto;

import com.mts.mtsone.modules.auth.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private Gender gender;
    private Date dateOfBirth;
    private Date createdAt;
    private Date lastLoginAt;
    private boolean isActive;
    private Set<String> roles;
} 