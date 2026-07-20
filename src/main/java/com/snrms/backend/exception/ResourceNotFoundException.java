package com.snrms.backend.exception;

/**
 * Thrown whenever a service tries to find something by ID
 * (a category, a resource, a project...) and it doesn't exist.
 * The GlobalExceptionHandler turns this into a 404 response.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
