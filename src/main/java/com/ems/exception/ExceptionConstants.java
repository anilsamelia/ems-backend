package com.ems.exception;

/**
 * Exception constants and utilities
 */
public class ExceptionConstants {

    // Exception Messages
    public static final String EMPLOYEE_NOT_FOUND = "Employee not found with id: ";
    public static final String DUPLICATE_EMAIL = "Email already exists: ";
    public static final String INVALID_NAME = "Employee name is required";
    public static final String INVALID_EMAIL = "Employee email is required";
    public static final String INVALID_SALARY = "Salary must be greater than 0";
    public static final String INVALID_PAGE = "Page number must be greater than or equal to 0";
    public static final String INVALID_SIZE = "Page size must be greater than 0 and less than or equal to 100";

    // Error Codes
    public static final String EMPLOYEE_NOT_FOUND_CODE = "EMPLOYEE_NOT_FOUND";
    public static final String DUPLICATE_EMAIL_CODE = "DUPLICATE_EMAIL";
    public static final String INVALID_EMPLOYEE_DATA_CODE = "INVALID_EMPLOYEE_DATA";
    public static final String INVALID_PAGINATION_CODE = "INVALID_PAGINATION";
    public static final String DATABASE_ERROR_CODE = "DATABASE_ERROR";
    public static final String INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR";

    // HTTP Status Codes
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;
    public static final int INTERNAL_SERVER_ERROR = 500;
}

