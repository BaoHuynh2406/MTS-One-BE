package com.mts.mtsone.modules.order.service;

import com.mts.mtsone.modules.order.dto.CreateOrderDTO;
import com.mts.mtsone.modules.order.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;


public interface OrderService {
    OrderDTO createOrder(
        CreateOrderDTO createOrderDTO, 
        MultipartFile orderPhoto,
        MultipartFile phonePhoto,
        MultipartFile diliveryPhoto);
        
    OrderDTO updateOrder(UUID id, CreateOrderDTO orderDTO);
    void deleteOrder(UUID id);
    OrderDTO getOrderById(UUID id);
    Page<OrderDTO> getAllOrders(Pageable pageable);
    Page<OrderDTO> searchOrders(String searchKey, Pageable pageable);
}
