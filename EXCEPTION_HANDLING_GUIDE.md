# Global Exception Handling Documentation

## Overview

The Employee Management System implements a comprehensive global exception handling mechanism that ensures consistent error responses across all API endpoints.

## Exception Hierarchy

```
EmsException (Base Exception)
├── EmployeeNotFoundException
├── DuplicateEmailException
├── InvalidEmployeeDataException
├── InvalidPaginationException
└── DatabaseException
```

---

## Custom Exceptions

### 1. EmsException (Base Exception)

**Location:** `com.ems.exception.EmsException`

Base custom exception for all EMS-specific exceptions.

**Constructors:**
```java
public EmsException(String message)
public EmsException(String message, String errorCode)
public EmsException(String message, String errorCode, int statusCode)
public EmsException(String message, Throwable cause)
public EmsException(String message, String errorCode, int statusCode, Throwable cause)
```

**Usage:**
```java
throw new EmsException("Something went wrong", "CUSTOM_CODE", 500);
```

**Properties:**
- `errorCode`: Custom error code for identifying the error type
- `statusCode`: HTTP status code

---

### 2. EmployeeNotFoundException

**Location:** `com.ems.exception.EmployeeNotFoundException`

Thrown when an employee is not found in the database.

**Status Code:** 404 (Not Found)

**Error Code:** `EMPLOYEE_NOT_FOUND`

**Constructors:**
```java
public EmployeeNotFoundException(Long id)
public EmployeeNotFoundException(String message)
public EmployeeNotFoundException(String message, String errorCode)
```

**Usage Examples:**
```java
throw new EmployeeNotFoundException(1L);  // By ID
throw new EmployeeNotFoundException("Employee with ID 1 not found");  // Custom message
```

**Example Response:**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 1",
  "errorCode": "EMPLOYEE_NOT_FOUND",
  "path": "/api/employees/1",
  "timestamp": "2026-02-25T10:30:00"
}
```

---

### 3. DuplicateEmailException

**Location:** `com.ems.exception.DuplicateEmailException`

Thrown when attempting to create/update an employee with a duplicate email address.

**Status Code:** 409 (Conflict)

**Error Code:** `DUPLICATE_EMAIL`

**Constructors:**
```java
public DuplicateEmailException(String email)
public DuplicateEmailException(String message, String errorCode)
```

**Usage Examples:**
```java
throw new DuplicateEmailException("john@example.com");
throw new DuplicateEmailException("Email already in use", "DUPLICATE_EMAIL");
```

**Example Response:**
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists: john@example.com",
  "errorCode": "DUPLICATE_EMAIL",
  "path": "/api/employees",
  "timestamp": "2026-02-25T10:30:00"
}
```

---

### 4. InvalidEmployeeDataException

**Location:** `com.ems.exception.InvalidEmployeeDataException`

Thrown when employee data validation fails.

**Status Code:** 400 (Bad Request)

**Error Code:** `INVALID_EMPLOYEE_DATA`

**Constructors:**
```java
public InvalidEmployeeDataException(String message)
public InvalidEmployeeDataException(String message, String errorCode)
public InvalidEmployeeDataException(String message, Throwable cause)
```

**Usage Examples:**
```java
throw new InvalidEmployeeDataException("Employee name is required");
throw new InvalidEmployeeDataException("Invalid salary amount", "INVALID_SALARY");
throw new InvalidEmployeeDataException("Invalid data", cause);
```

**Example Response:**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Employee name is required",
  "errorCode": "INVALID_EMPLOYEE_DATA",
  "path": "/api/employees",
  "timestamp": "2026-02-25T10:30:00"
}
```

---

### 5. InvalidPaginationException

**Location:** `com.ems.exception.InvalidPaginationException`

Thrown when invalid pagination parameters are provided.

**Status Code:** 400 (Bad Request)

**Error Code:** `INVALID_PAGINATION`

**Constructors:**
```java
public InvalidPaginationException(String message)
public InvalidPaginationException(String message, String errorCode)
public InvalidPaginationException(int page, int size)
```

**Usage Examples:**
```java
throw new InvalidPaginationException("Page size must be less than 100");
throw new InvalidPaginationException(-1, 10);  // Invalid page number
```

**Example Response:**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid pagination parameters - Page: -1, Size: 10",
  "errorCode": "INVALID_PAGINATION",
  "path": "/api/employees/paginated/all",
  "timestamp": "2026-02-25T10:30:00"
}
```

---

### 6. DatabaseException

**Location:** `com.ems.exception.DatabaseException`

Thrown when a database operation fails.

**Status Code:** 500 (Internal Server Error)

**Error Code:** `DATABASE_ERROR`

**Constructors:**
```java
public DatabaseException(String message)
public DatabaseException(String message, String errorCode)
public DatabaseException(String message, Throwable cause)
public DatabaseException(String message, String errorCode, Throwable cause)
```

**Usage Examples:**
```java
throw new DatabaseException("Failed to retrieve employees");
throw new DatabaseException("Database connection lost", sqlException);
```

**Example Response:**
```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "Failed to retrieve employees",
  "errorCode": "DATABASE_ERROR",
  "path": "/api/employees",
  "timestamp": "2026-02-25T10:30:00"
}
```

---

## Global Exception Handler

**Location:** `com.ems.exception.GlobalExceptionHandler`

The `@RestControllerAdvice` class that handles all exceptions globally.

### Handled Exceptions

| Exception | Handler Method | Status Code | Details |
|-----------|-----------------|-------------|---------|
| `EmployeeNotFoundException` | `handleEmployeeNotFoundException()` | 404 | Employee not found |
| `DuplicateEmailException` | `handleDuplicateEmailException()` | 409 | Email already exists |
| `InvalidEmployeeDataException` | `handleInvalidEmployeeDataException()` | 400 | Invalid data |
| `InvalidPaginationException` | `handleInvalidPaginationException()` | 400 | Invalid pagination |
| `DatabaseException` | `handleDatabaseException()` | 500 | Database error |
| `EmsException` | `handleEmsException()` | Variable | Generic EMS error |
| `MethodArgumentNotValidException` | `handleMethodArgumentNotValidException()` | 400 | Validation error |
| `NoHandlerFoundException` | `handleNoHandlerFoundException()` | 404 | Endpoint not found |
| `IllegalArgumentException` | `handleIllegalArgumentException()` | 400 | Illegal argument |
| `NullPointerException` | `handleNullPointerException()` | 500 | Null pointer |
| `Exception` | `handleGlobalException()` | 500 | Unexpected error |

---

## Error Response Format

All error responses follow the same format:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Detailed error message",
  "errorCode": "ERROR_CODE",
  "path": "/api/endpoint",
  "timestamp": "2026-02-25T10:30:00",
  "details": {
    "key1": "value1",
    "key2": "value2"
  }
}
```

### Fields:
- **status**: HTTP status code
- **error**: HTTP status reason phrase
- **message**: Detailed error message
- **errorCode**: Machine-readable error code
- **path**: Request path that caused the error
- **timestamp**: When the error occurred
- **details**: Additional error details (validation errors, etc.)

---

## ErrorResponse Class

**Location:** `com.ems.exception.ErrorResponse`

DTO for standardized error responses.

**Constructors:**
```java
public ErrorResponse(int status, String error, String message, 
                     String errorCode, String path)
                     
public ErrorResponse(int status, String error, String message, 
                     String errorCode, String path, LocalDateTime timestamp)
```

**Methods:**
```java
public void addDetail(String key, Object value)  // Add detail to error response
```

---

## Exception Constants

**Location:** `com.ems.exception.ExceptionConstants`

Centralized constants for exception messages, codes, and HTTP status codes.

**Available Constants:**

### Messages
```java
EMPLOYEE_NOT_FOUND = "Employee not found with id: "
DUPLICATE_EMAIL = "Email already exists: "
INVALID_NAME = "Employee name is required"
INVALID_EMAIL = "Employee email is required"
INVALID_SALARY = "Salary must be greater than 0"
INVALID_PAGE = "Page number must be greater than or equal to 0"
INVALID_SIZE = "Page size must be greater than 0 and less than or equal to 100"
```

### Error Codes
```java
EMPLOYEE_NOT_FOUND_CODE = "EMPLOYEE_NOT_FOUND"
DUPLICATE_EMAIL_CODE = "DUPLICATE_EMAIL"
INVALID_EMPLOYEE_DATA_CODE = "INVALID_EMPLOYEE_DATA"
INVALID_PAGINATION_CODE = "INVALID_PAGINATION"
DATABASE_ERROR_CODE = "DATABASE_ERROR"
INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR"
```

### Status Codes
```java
BAD_REQUEST = 400
NOT_FOUND = 404
CONFLICT = 409
INTERNAL_SERVER_ERROR = 500
```

---

## Usage Examples

### In Service Layer

```java
@Override
public Employee saveEmployee(Employee employee) {
    if (employee == null || employee.getName() == null || 
        employee.getName().trim().isEmpty()) {
        throw new InvalidEmployeeDataException("Employee name is required");
    }
    
    if (repository.existsByEmail(employee.getEmail())) {
        throw new DuplicateEmailException(employee.getEmail());
    }
    
    return repository.save(employee);
}

@Override
public Optional<Employee> getEmployeeById(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
}
```

### In Controller Layer

```java
@GetMapping("/{id}")
public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    Optional<Employee> employee = employeeService.getEmployeeById(id);
    return employee.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
}
```

The exception handling is automatic - the controller doesn't need to catch these exceptions. The `GlobalExceptionHandler` will handle them.

---

## Response Examples

### Success Response (200 OK)
```json
{
  "id": 1,
  "name": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "dept": "IT",
  "designation": "Developer",
  "salary": 60000.0,
  "supervisor": "John Smith",
  "address": "123 Main St",
  "doj": "2023-01-15"
}
```

### Not Found (404)
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "errorCode": "EMPLOYEE_NOT_FOUND",
  "path": "/api/employees/999",
  "timestamp": "2026-02-25T10:30:45"
}
```

### Duplicate Email (409)
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists: alice@example.com",
  "errorCode": "DUPLICATE_EMAIL",
  "path": "/api/employees",
  "timestamp": "2026-02-25T10:31:00"
}
```

### Invalid Data (400)
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Employee name is required",
  "errorCode": "INVALID_EMPLOYEE_DATA",
  "path": "/api/employees",
  "timestamp": "2026-02-25T10:31:15"
}
```

### Validation Error (400)
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errorCode": "VALIDATION_ERROR",
  "path": "/api/employees",
  "timestamp": "2026-02-25T10:31:30",
  "details": {
    "email": "must be a valid email address",
    "name": "must not be blank"
  }
}
```

### Internal Server Error (500)
```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred: NullPointerException",
  "errorCode": "INTERNAL_SERVER_ERROR",
  "path": "/api/employees",
  "timestamp": "2026-02-25T10:31:45"
}
```

---

## Best Practices

### 1. Use Specific Exceptions
```java
// ✅ Good
throw new EmployeeNotFoundException(id);

// ❌ Avoid
throw new RuntimeException("Not found");
```

### 2. Provide Meaningful Messages
```java
// ✅ Good
throw new InvalidEmployeeDataException("Salary must be greater than 0");

// ❌ Avoid
throw new InvalidEmployeeDataException("Invalid data");
```

### 3. Validate Input Early
```java
@Override
public Employee saveEmployee(Employee employee) {
    // Validate before saving
    if (employee.getName() == null || employee.getName().isEmpty()) {
        throw new InvalidEmployeeDataException("Name is required");
    }
    return repository.save(employee);
}
```

### 4. Check for Duplicates
```java
if (repository.existsByEmail(email)) {
    throw new DuplicateEmailException(email);
}
```

### 5. Use Error Codes
All custom exceptions should include error codes for client-side handling:
```java
// Client can check errorCode
if (error.errorCode == "DUPLICATE_EMAIL") {
    // Handle duplicate email
}
```

---

## Testing Exception Handling

### cURL Examples

**Test 404:**
```bash
curl -v http://localhost:8080/api/employees/999
# Returns: 404 Not Found with EmployeeNotFoundException details
```

**Test Duplicate Email:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice",
    "email": "alice.johnson@example.com",
    ...
  }'
# Returns: 409 Conflict with DuplicateEmailException details
```

**Test Invalid Data:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "test@example.com",
    ...
  }'
# Returns: 400 Bad Request with InvalidEmployeeDataException details
```

---

## Exception Flow Diagram

```
Request → Controller → Service → Repository
                         ↓
                    Throws Exception
                         ↓
         GlobalExceptionHandler catches it
                         ↓
         Returns ErrorResponse with proper HTTP status
                         ↓
                      Client
```

---

## File Structure

```
ems/src/main/java/com/ems/exception/
├── EmsException.java                      (Base exception)
├── EmployeeNotFoundException.java          (404)
├── DuplicateEmailException.java            (409)
├── InvalidEmployeeDataException.java       (400)
├── InvalidPaginationException.java         (400)
├── DatabaseException.java                  (500)
├── ErrorResponse.java                      (DTO)
├── GlobalExceptionHandler.java             (Handler)
├── ExceptionConstants.java                 (Constants)
└── EXCEPTION_HANDLING_GUIDE.md             (This file)
```

---

## Configuration

No additional configuration is required. The `GlobalExceptionHandler` is automatically detected and applied due to the `@RestControllerAdvice` annotation.

---

## Future Enhancements

1. **Logging Integration**
   - Integrate SLF4J/Logback for better logging
   - Log all exceptions with appropriate levels

2. **Metrics**
   - Track exception frequency
   - Monitor error rates

3. **Notifications**
   - Alert on critical exceptions
   - Email notifications for specific error types

4. **Versioning**
   - Support different error response formats for different API versions

---

## Summary

The global exception handling system provides:
- ✅ Consistent error responses across the API
- ✅ Clear error codes for client-side handling
- ✅ Proper HTTP status codes
- ✅ Detailed error messages
- ✅ Additional error details when needed
- ✅ Easy to extend with new exception types
- ✅ Centralized exception management

---

**Last Updated:** February 25, 2026  
**Version:** 1.0  
**Status:** Active ✅

