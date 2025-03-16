package com.mts.mtsone.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(
            "RESOURCE_NOT_FOUND",
            String.format("%s không tìm thấy với %s: '%s'", resourceName, fieldName, fieldValue),
            HttpStatus.NOT_FOUND
        );
    }
} 