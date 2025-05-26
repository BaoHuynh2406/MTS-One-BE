package com.mts.mtsone.modules.order.dto;

import com.mts.mtsone.modules.order.entity.Ecom;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class CreateOrderDTO {
    @NotBlank(message = "Mã đơn hàng không được để trống")
    private String orderCode;
    private Ecom ecom;
    private Date deliveryAt;
    private UUID customerId;
    private String phoneNumber;
}
