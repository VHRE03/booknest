package com.vhre.booknest.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized error response payload for the API.
 * Ensures a consistent error structure across all endpoints.
 */
@Data
@Builder
public class ApiErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2026-10-14T10:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Brief error message", example = "Not Found")
    private String error;

    @Schema(description = "Detailed message or field-specific validation errors")
    private List<String> details;

    @Schema(description = "API path where the error occurred", example = "/api/v1/books/550e8400-e29b-41d4-a716-446655440000")
    private String path;
}
