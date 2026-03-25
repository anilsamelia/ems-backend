package com.ems.exception;

/**
 * Exception thrown when attempting to create an employee with duplicate email
 */
public class DuplicateEmailException extends EmsException {

    public DuplicateEmailException(String email) {
        super("Email already exists: " + email, "DUPLICATE_EMAIL", 409);
    }

    public DuplicateEmailException(String message, String errorCode) {
        super(message, errorCode, 409);
    }
}

