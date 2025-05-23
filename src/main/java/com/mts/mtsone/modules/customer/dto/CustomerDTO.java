package com.mts.mtsone.modules.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private long id;
    private String fullName;
    private boolean sex;
    private Date birthday;
    private String phoneNumber;
    private String address;
    private String note;
}
