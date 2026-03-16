package com.ems.exception;

/**
 * Exception thrown when invalid pagination parameters are provided
 */
public class InvalidPaginationException extends EmsException {

    public InvalidPaginationException(String message) {
        super(message, "INVALID_PAGINATION", 400);
    }

    public InvalidPaginationException(String message, String errorCode) {
        super(message, errorCode, 400);
    }

    public InvalidPaginationException(int page, int size) {
        super("Invalid pagination parameters - Page: " + page + ", Size: " + size,
              "INVALID_PAGINATION", 400);
    }
}

