package com.mts.mtsone.modules.auth.controller;

import com.mts.mtsone.common.response.ApiResponse;
import com.mts.mtsone.modules.auth.dto.AuthenticationRequest;
import com.mts.mtsone.modules.auth.dto.AuthenticationResponse;
import com.mts.mtsone.modules.auth.dto.UserCreateDTO;
import com.mts.mtsone.modules.auth.security.JwtService;
import com.mts.mtsone.modules.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API xác thực người dùng")
public class AuthController {

    private final AuthenticationService authenticationService;


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

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất", description = "API để đăng xuất và vô hiệu hóa token")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            authenticationService.logout(jwt);
            return ResponseEntity.ok(ApiResponse.success("Đăng xuất thành công"));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Token không hợp lệ", HttpStatus.BAD_REQUEST));
    }
} 