package com.snrms.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * A single consistent JSON shape for every error the API can return.
 * Axios (used by the frontend) exposes this as error.response.data,
 * and AuthContext.jsx already reads error.response.data.message -
 * so "message" is the field name that matters most here.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    // Only populated for validation errors: { "fieldName": "error message" }
    private Map<String, String> validationErrors;

    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
