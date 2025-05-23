package com.mts.mtsone.modules.order.repository;

import com.mts.mtsone.modules.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    boolean existsByOrderCode(String orderCode);
    boolean existsByOrderCodeAndIdNot(String orderCode, UUID id);
    
    @Query("SELECT o FROM Order o WHERE " +
           "(:searchKey IS NULL OR LOWER(o.orderCode) LIKE LOWER(CONCAT('%', :searchKey, '%')))")
    Page<Order> searchOrders(String searchKey, Pageable pageable);
}
