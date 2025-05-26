package com.mts.mtsone.modules.order.controller;

import com.mts.mtsone.common.response.ApiResponse;
import com.mts.mtsone.common.response.PaginationInfo;
import com.mts.mtsone.modules.order.dto.CreateOrderDTO;
import com.mts.mtsone.modules.order.dto.OrderDTO;
import com.mts.mtsone.modules.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "API quản lý đơn hàng")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(
        summary = "Lấy danh sách đơn hàng có phân trang",
        description = "API này trả về danh sách đơn hàng với phân trang. Mặc định page=0, size=10"
    )
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKey
    ) {
        Page<OrderDTO> orderPage = searchKey != null && !searchKey.isEmpty() 
            ? orderService.searchOrders(searchKey, PageRequest.of(page-1, size))
            : orderService.getAllOrders(PageRequest.of(page-1, size));

        return ResponseEntity.ok(
            ApiResponse.success(
                "Lấy danh sách đơn hàng thành công",
                orderPage.getContent(),
                PaginationInfo.of(orderPage)
            )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin đơn hàng theo ID")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable UUID id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Tạo mới đơn hàng")
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(
            @RequestPart("orderData") CreateOrderDTO createOrderDTO,
            @RequestPart("orderPhoto") MultipartFile orderPhoto,
            @RequestPart("orderPhoto") MultipartFile phonePhoto,
            @RequestPart("deliveryPhoto") MultipartFile deliveryPhoto
    ) {
        OrderDTO order = orderService.createOrder(createOrderDTO, orderPhoto,phonePhoto, deliveryPhoto);
        return ResponseEntity.ok(ApiResponse.success("Tạo đơn hàng thành công", order));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin đơn hàng")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrder(
            @PathVariable UUID id,
            @RequestBody CreateOrderDTO orderDTO
    ) {
        OrderDTO order = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật đơn hàng thành công", order));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa đơn hàng")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa đơn hàng thành công"));
    }
}
