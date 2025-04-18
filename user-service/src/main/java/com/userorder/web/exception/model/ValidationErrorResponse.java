package com.userorder.web.exception.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Validation error response for API validation errors
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Validation error response")
public class ValidationErrorResponse {

    @Schema(description = "Timestamp when the error occurred")
    private LocalDateTime timestamp;
    
    @Schema(description = "HTTP status code")
    private int status;
    
    @Schema(description = "Error type")
    private String error;
    
    @Schema(description = "Error message")
    private String message;
    
    @Schema(description = "Request path that caused the error")
    private String path;
    
    @Schema(description = "Field-level validation errors")
    private Map<String, String> errors;
}