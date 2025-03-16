package com.mts.mtsone.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class RoleDTO {
    private Long id;
    
    @NotBlank(message = "Tên role không được để trống")
    private String name;
    
    private String description;
    
    private Set<String> permissions;
} 