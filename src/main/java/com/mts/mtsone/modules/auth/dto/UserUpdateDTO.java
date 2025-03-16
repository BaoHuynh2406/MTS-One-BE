package com.mts.mtsone.modules.auth.dto;

import com.mts.mtsone.modules.auth.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.util.Date;

@Data
public class UserUpdateDTO {
    private String fullName;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    private String phone;
    
    private Gender gender;
    
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private Date dateOfBirth;
} 