package com.snrms.backend.exception;

/**
 * Thrown when trying to create/update something that would violate a
 * uniqueness rule - e.g. a category name that already exists, or a
 * username/email that's already taken.
 * The GlobalExceptionHandler turns this into a 409 Conflict response.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
