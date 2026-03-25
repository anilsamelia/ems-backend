package com.ems.exception;

/**
 * Exception thrown when employee data validation fails
 */
public class InvalidEmployeeDataException extends EmsException {

    public InvalidEmployeeDataException(String message) {
        super(message, "INVALID_EMPLOYEE_DATA", 400);
    }

    public InvalidEmployeeDataException(String message, String errorCode) {
        super(message, errorCode, 400);
    }

    public InvalidEmployeeDataException(String message, Throwable cause) {
        super(message, "INVALID_EMPLOYEE_DATA", 400, cause);
    }
}

