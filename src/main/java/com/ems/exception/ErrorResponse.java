package com.ems.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Standard error response format for all API errors
 */
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String errorCode;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, Object> details;

    /**
     * Constructor with basic error information
     */
    public ErrorResponse(int status, String error, String message, String errorCode, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.errorCode = errorCode;
        this.path = path;
        this.timestamp = LocalDateTime.now();
        this.details = new HashMap<>();
    }

    /**
     * Constructor with all fields
     */
    public ErrorResponse(int status, String error, String message, String errorCode, String path, LocalDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.errorCode = errorCode;
        this.path = path;
        this.timestamp = timestamp;
        this.details = new HashMap<>();
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    /**
     * Add detail to the error response
     */
    public void addDetail(String key, Object value) {
        if (this.details == null) {
            this.details = new HashMap<>();
        }
        this.details.put(key, value);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", path='" + path + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

