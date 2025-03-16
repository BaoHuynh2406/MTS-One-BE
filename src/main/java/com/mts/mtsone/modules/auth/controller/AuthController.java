package com.mts.mtsone.modules.auth.controller;

import com.mts.mtsone.common.response.ApiResponse;
import com.mts.mtsone.modules.auth.dto.AuthenticationRequest;
import com.mts.mtsone.modules.auth.dto.AuthenticationResponse;
import com.mts.mtsone.modules.auth.dto.UserCreateDTO;
import com.mts.mtsone.modules.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API xác thực người dùng")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(
        summary = "Đăng ký tài khoản",
        description = "API để đăng ký tài khoản mới. Trả về access token và refresh token sau khi đăng ký thành công."
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @RequestBody @Valid UserCreateDTO request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
            "Đăng ký thành công",
            authenticationService.register(request)
        ));
    }

    @Operation(
        summary = "Đăng nhập",
        description = "API để đăng nhập. Trả về access token và refresh token sau khi đăng nhập thành công."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
            "Đăng nhập thành công",
            authenticationService.authenticate(request)
        ));
    }

    @Operation(
        summary = "Làm mới token",
        description = "API để làm mới access token bằng refresh token. Trả về access token và refresh token mới."
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @RequestHeader("Authorization") String refreshToken
    ) {
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        return ResponseEntity.ok(ApiResponse.success(
            "Làm mới token thành công",
            authenticationService.refreshToken(refreshToken)
        ));
    }
} 