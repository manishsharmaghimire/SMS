package com.sms.exception;

/**
 * Custom exception class for mapping related errors.
 */
public class MappingException extends RuntimeException {

    /**
     * Constructs a new mapping exception with the specified detail message.
     *
     * @param message the detail message
     */
    public MappingException(String message) {
        super(message);
    }

    /**
     * Constructs a new mapping exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
