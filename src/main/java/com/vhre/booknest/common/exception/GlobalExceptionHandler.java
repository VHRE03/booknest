package com.vhre.booknest.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler that intercepts exceptions thrown across the application.
 * Translates them into standardized HTTP responses ({@link ApiErrorResponse}).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ResourceNotFoundException (also used for missing related entities,
     * e.g. an authorId/bookId that does not exist) and translates it
     * into a 404 Not Found response.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Handles DuplicateResourceException (business uniqueness rules checked by the
     * services, e.g. duplicated email or ISBN) and translates it
     * into a 409 Conflict response.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Handles validation errors (@Valid failures on request bodies) and translates
     * them into a 400 Bad Request response with field-specific details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = error instanceof FieldError fieldError
                            ? fieldError.getField()
                            : error.getObjectName();
                    String errorMessage = error.getDefaultMessage();
                    return fieldName + ": " + errorMessage;
                })
                .collect(Collectors.toList());

        return build(HttpStatus.BAD_REQUEST, "Validation Failed", errors, request);
    }

    /**
     * Handles malformed path variables (e.g. a non-UUID id) and translates them
     * into a 400 Bad Request response instead of a 500.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";
        return build(HttpStatus.BAD_REQUEST, "Bad Request", Collections.singletonList(message), request);
    }

    /**
     * Handles unreadable request bodies (malformed JSON, wrong types) and translates
     * them into a 400 Bad Request response.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.debug("Unreadable request body on {}", request.getRequestURI(), ex);
        return build(HttpStatus.BAD_REQUEST, "Malformed request body",
                Collections.singletonList("The request body could not be parsed"), request);
    }

    /**
     * Handles database integrity violations (unique constraints, foreign keys, etc.)
     * and translates them into a 409 Conflict response without leaking schema details.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Data integrity violation on {}: {}", request.getRequestURI(),
                ex.getMostSpecificCause().getMessage());
        return build(HttpStatus.CONFLICT, "The operation violates a data integrity constraint "
                        + "(duplicate value or missing related resource).",
                request);
    }

    /**
     * Fallback handler for any unhandled exception, translating it into a
     * 500 Internal Server Error.
     *
     * <p>Business exceptions annotated with {@code @ResponseStatus} keep their declared
     * status code and reason instead of being masked as a 500, so custom rules thrown
     * from the services surface correctly.</p>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {
        ResponseStatus responseStatus =
                AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            String message = responseStatus.reason().isEmpty() ? ex.getMessage() : responseStatus.reason();
            log.debug("Resolving {} with @ResponseStatus {}", ex.getClass().getName(),
                    responseStatus.code());
            return build(HttpStatus.valueOf(responseStatus.code().value()), message, request);
        }
        log.error("Unhandled exception on {} {}", request.getMethod(), request.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please contact support.",
                request);
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String error, HttpServletRequest request) {
        return build(status, status.getReasonPhrase(), Collections.singletonList(error), request);
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String error, List<String> details,
                                                   HttpServletRequest request) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .details(details)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
