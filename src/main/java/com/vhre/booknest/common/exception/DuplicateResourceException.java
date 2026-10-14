package com.vhre.booknest.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown when a business uniqueness rule is violated
 * (e.g. an email or an ISBN that already exists in the system).
 * Intercepted by the {@link GlobalExceptionHandler} to return a standardized
 * 409 Conflict HTTP response.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     *
     * @param message the detail message explaining which unique value is duplicated
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
