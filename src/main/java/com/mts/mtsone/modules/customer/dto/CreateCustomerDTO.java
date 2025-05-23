package com.mts.mtsone.modules.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.Date;

@Data
public class CreateCustomerDTO {
    @NotBlank(message = "Tên không được để trống")
    private String fullName;
    
    private boolean sex;
    
    private Date birthday;
    
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;
    
    private String address;
    private String note;
}
