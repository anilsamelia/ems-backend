package com.ems.exception;

/**
 * Exception thrown when an employee is not found
 */
public class EmployeeNotFoundException extends EmsException {

    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id: " + id, "EMPLOYEE_NOT_FOUND", 404);
    }

    public EmployeeNotFoundException(String message) {
        super(message, "EMPLOYEE_NOT_FOUND", 404);
    }

    public EmployeeNotFoundException(String message, String errorCode) {
        super(message, errorCode, 404);
    }
}

