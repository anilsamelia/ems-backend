package com.ems.exception;

/**
 * Base custom exception for all Employee Management System exceptions
 */
public class EmsException extends RuntimeException {
    private String errorCode;
    private int statusCode;

    public EmsException(String message) {
        super(message);
        this.errorCode = "EMS_ERROR";
        this.statusCode = 500;
    }

    public EmsException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = 500;
    }

    public EmsException(String message, String errorCode, int statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public EmsException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "EMS_ERROR";
        this.statusCode = 500;
    }

    public EmsException(String message, String errorCode, int statusCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

