package com.mts.mtsone.modules.order.dto;

import com.mts.mtsone.modules.order.entity.Ecom;
import com.mts.mtsone.modules.order.entity.OrderStatus;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class OrderDTO {
    private UUID id;
    private String orderCode;
    private Ecom ecom;
    private Date deliveryAt;
    private UUID userId;
    private UUID customerId;
    private String orderImage;
    private String deliveryPhoto;
    private OrderStatus status;
}
