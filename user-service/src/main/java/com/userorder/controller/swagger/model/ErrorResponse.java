package com.userorder.controller.swagger.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard error response for API errors
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response")
public class ErrorResponse {

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
}