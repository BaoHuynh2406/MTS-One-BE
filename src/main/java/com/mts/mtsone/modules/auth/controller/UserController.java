package com.mts.mtsone.modules.auth.controller;

import com.mts.mtsone.common.response.ApiResponse;
import com.mts.mtsone.common.response.PaginationInfo;
import com.mts.mtsone.modules.auth.dto.UserCreateDTO;
import com.mts.mtsone.modules.auth.dto.UserDTO;
import com.mts.mtsone.modules.auth.dto.UserUpdateDTO;
import com.mts.mtsone.modules.auth.dto.UserUpdatePasswordDTO;
import com.mts.mtsone.modules.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API quản lý người dùng")
public class UserController {

    private final UserService userService;

    // Thiếu role controller và permision controler

    @GetMapping
    @Operation(
        summary = "Lấy danh sách người dùng có phân trang", 
        description = "API này trả về danh sách người dùng với phân trang. Mặc định page=0, size=10"
    )
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "true") boolean active,
        @RequestParam(defaultValue = "") String searchKey
    ) {
        return ResponseEntity.ok(
            ApiResponse.success(
                "Lấy danh sách người dùng thành công",
                userService.getAllUsers(page-1, size, active, searchKey).getContent(),
                PaginationInfo.of(userService.getAllUsers(page, size, active, searchKey))
            )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin người dùng theo ID")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/my-info")
    @Operation(summary = "Lấy thông tin người dùng đang đăng nhập")
    public ResponseEntity<ApiResponse<UserDTO>> getMyInfo() {
        UserDTO user = userService.getMyInfo();
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping
    @Operation(summary = "Tạo mới người dùng")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        UserDTO user = userService.createUser(userCreateDTO);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin người dùng")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable UUID id, @RequestBody UserUpdateDTO userUpdateDTO) {
        UserDTO user = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    //Khoá người dùng
    @PutMapping("/{id}/active")
    @Operation(summary = "Thay đổi trạng thái Active của người dùng")
    public ResponseEntity<ApiResponse<UserDTO>> updateActiveUser(@PathVariable UUID id, @RequestBody boolean isActive) {
        UserDTO user = userService.updateActiveUser(id, isActive);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    //Cập nhật mật khẩu
    @PutMapping("/{id}/password")
    @Operation(summary = "Cập nhật mật khẩu người dùng")
    public ResponseEntity<ApiResponse<String>> updatePassword(@PathVariable UUID id, @RequestBody UserUpdatePasswordDTO userDTO) {
        userService.updatePassword(id, userDTO.getPassword());
        return ResponseEntity.ok(ApiResponse.success("Cập nhật mật khẩu thành công"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa người dùng")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa người dùng thành công"));
    }
}