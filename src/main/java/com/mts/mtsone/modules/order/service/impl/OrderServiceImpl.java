package com.mts.mtsone.modules.order.service.impl;

import com.mts.mtsone.common.exception.BusinessException;
import com.mts.mtsone.modules.order.dto.CreateOrderDTO;
import com.mts.mtsone.modules.order.dto.OrderDTO;
import com.mts.mtsone.modules.order.entity.Order;
import com.mts.mtsone.modules.order.entity.OrderStatus;
import com.mts.mtsone.modules.order.mapper.OrderMapper;
import com.mts.mtsone.modules.order.repository.OrderRepository;
import com.mts.mtsone.modules.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderDTO createOrderDTO) {
        if (orderRepository.existsByOrderCode(createOrderDTO.getOrderCode())) {
            throw new BusinessException("ORDER_CODE_EXISTS", "Mã đơn hàng đã tồn tại");
        }

        Order order = orderMapper.toEntity(createOrderDTO);
        order.setStatus(OrderStatus.PENDING);
        order = orderRepository.save(order);
        return orderMapper.toDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(UUID id, CreateOrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ORDER_NOT_FOUND", "Không tìm thấy đơn hàng"));

        if (orderRepository.existsByOrderCodeAndIdNot(orderDTO.getOrderCode(), id)) {
            throw new BusinessException("ORDER_CODE_EXISTS", "Mã đơn hàng đã tồn tại");
        }

        orderMapper.updateOrderFromDTO(orderDTO, order);
        order = orderRepository.save(order);
        return orderMapper.toDTO(order);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new BusinessException("ORDER_NOT_FOUND", "Không tìm thấy đơn hàng");
        }
        orderRepository.deleteById(id);
    }

    @Override
    public OrderDTO getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("ORDER_NOT_FOUND", "Không tìm thấy đơn hàng"));
        return orderMapper.toDTO(order);
    }

    @Override
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDTO);
    }

    @Override
    public Page<OrderDTO> searchOrders(String searchKey, Pageable pageable) {
        return orderRepository.searchOrders(searchKey, pageable)
                .map(orderMapper::toDTO);
    }
}
