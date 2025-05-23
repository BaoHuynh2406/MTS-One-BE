package com.mts.mtsone.modules.order.mapper;

import com.mts.mtsone.modules.order.dto.CreateOrderDTO;
import com.mts.mtsone.modules.order.dto.OrderDTO;
import com.mts.mtsone.modules.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toDTO(Order order);
    Order toEntity(CreateOrderDTO createOrderDTO);
    void updateOrderFromDTO(CreateOrderDTO dto, @MappingTarget Order order);
}
