package com.ems.exception;

/**
 * Exception thrown when database operation fails
 */
public class DatabaseException extends EmsException {

    public DatabaseException(String message) {
        super(message, "DATABASE_ERROR", 500);
    }

    public DatabaseException(String message, String errorCode) {
        super(message, errorCode, 500);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, "DATABASE_ERROR", 500, cause);
    }

    public DatabaseException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, 500, cause);
    }
}

