package it.unipd.bookly.exceptions;

/**
 * Custom exception to wrap database-related errors with clearer application-level messaging.
 */
public class DatabaseException extends Exception {

    /**
     * Constructs a new DatabaseException with the specified detail message.
     *
     * @param message the detailed error message.
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new DatabaseException with the specified detail message and cause.
     *
     * @param message the detailed error message.
     * @param cause   the original exception that caused this error.
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
