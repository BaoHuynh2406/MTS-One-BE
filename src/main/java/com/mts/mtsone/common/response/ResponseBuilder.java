package com.mts.mtsone.common.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseBuilder {
    private ResponseBuilder() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<List<T>>> success(Page<T> page) {
        return ResponseEntity.ok(
            ApiResponse.success("Thành công", page.getContent(), PaginationInfo.of(page))
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status)
            .body(ApiResponse.error(message, status));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status, T data) {
        return ResponseEntity.status(status)
            .body(ApiResponse.error(message, status, data));
    }
} 