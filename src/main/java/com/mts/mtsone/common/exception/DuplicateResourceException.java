package com.mts.mtsone.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BaseException {
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(
            "DUPLICATE_RESOURCE",
            String.format("%s đã tồn tại với %s: '%s'", resourceName, fieldName, fieldValue),
            HttpStatus.CONFLICT
        );
    }
} 