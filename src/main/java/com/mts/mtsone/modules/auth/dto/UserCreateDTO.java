package com.mts.mtsone.modules.auth.dto;

import com.mts.mtsone.modules.auth.entity.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class UserCreateDTO {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải từ 3-50 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String phone;

    private Gender gender;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private Date dateOfBirth;

    private Set<String> roles;
} 