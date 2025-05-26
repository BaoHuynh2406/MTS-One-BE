package com.mts.mtsone.modules.customer.controller;

import com.mts.mtsone.common.response.ApiResponse;
import com.mts.mtsone.common.response.PaginationInfo;
import com.mts.mtsone.modules.customer.dto.CreateCustomerDTO;
import com.mts.mtsone.modules.customer.dto.CustomerDTO;
import com.mts.mtsone.modules.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "API quản lý khách hàng")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Tạo mới khách hàng")
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(
            @Valid @RequestBody CreateCustomerDTO createCustomerDTO) {
        CustomerDTO customer = customerService.createCustomer(createCustomerDTO);
        return ResponseEntity.ok(ApiResponse.success(customer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin khách hàng")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CreateCustomerDTO customerDTO) {
        CustomerDTO customer = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(ApiResponse.success(customer));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa khách hàng")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa khách hàng thành công"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin khách hàng theo ID")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(customer));
    }

    @GetMapping
    @Operation(
        summary = "Lấy danh sách khách hàng có phân trang",
        description = "API này trả về danh sách khách hàng với phân trang. Mặc định page=0, size=10"
    )
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchKey) {
        Page<CustomerDTO> customers = customerService.getAllCustomers(page-1, size, searchKey);
        return ResponseEntity.ok(
            ApiResponse.success(
                "Lấy danh sách khách hàng thành công",
                customers.getContent(),
                PaginationInfo.of(customers)
            )
        );
    }
}
