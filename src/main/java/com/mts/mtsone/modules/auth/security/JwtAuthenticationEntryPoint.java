package com.mts.mtsone.modules.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mts.mtsone.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> errorResponse = ApiResponse.error(
            "Không có quyền truy cập",
            HttpStatus.UNAUTHORIZED
        );

        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
} 