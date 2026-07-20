package com.snrms.backend.exception;

/**
 * Thrown when trying to delete something that's still referenced by other
 * records - e.g. deleting a Category that Resources still point to, or a
 * Resource that Projects still point to.
 * The GlobalExceptionHandler turns this into a 409 Conflict response.
 *
 * This is exactly what the frontend's alert() messages already anticipate:
 * Categories.jsx: "Failed to delete. It might be in use by resources."
 * Resources.jsx:  "Failed to delete. Ensure it's not referenced by other records."
 * Users.jsx:      "Failed to delete user. They might have generated reports."
 */
public class ResourceInUseException extends RuntimeException {
    public ResourceInUseException(String message) {
        super(message);
    }
}
