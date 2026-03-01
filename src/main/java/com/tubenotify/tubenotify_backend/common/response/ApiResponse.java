package com.tubenotify.tubenotify_backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * API response wrapper that provides a consistent structure for HTTP responses
 */
@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int statusCode;

    public static <T> ApiResponse<T> response(boolean success, String message, T data, int statusCode) {
        return new ApiResponse<>(success, message, data, statusCode);
    }
}
