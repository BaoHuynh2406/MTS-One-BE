package com.mts.mtsone.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final int status;
    private final LocalDateTime timestamp;
    private final T data;
    private final PaginationInfo pagination;

    private ApiResponse(boolean success, String message, int status, T data, PaginationInfo pagination) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.data = data;
        this.pagination = pagination;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Thành công", HttpStatus.OK.value(), data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, HttpStatus.OK.value(), data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data, PaginationInfo pagination) {
        return new ApiResponse<>(true, message, HttpStatus.OK.value(), data, pagination);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, "Tạo mới thành công", HttpStatus.CREATED.value(), data, null);
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return new ApiResponse<>(false, message, status.value(), null, null);
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status, T data) {
        return new ApiResponse<>(false, message, status.value(), data, null);
    }
} 