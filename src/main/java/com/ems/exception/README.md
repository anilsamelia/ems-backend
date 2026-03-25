# Exception Package - README

## Overview

This package contains the global exception handling system for the Employee Management System (EMS). It provides centralized, consistent error handling across all API endpoints.

**Location:** `src/main/java/com/ems/exception/`

---

## Package Contents

### Exception Classes

#### 1. **EmsException.java**
Base class for all custom EMS exceptions.

**Properties:**
- `errorCode`: Custom error code for identification
- `statusCode`: HTTP status code

**Usage:**
```java
throw new EmsException("Something went wrong", "CUSTOM_CODE", 500);
```

---

#### 2. **EmployeeNotFoundException.java**
Thrown when an employee is not found.

**Status Code:** 404  
**Error Code:** EMPLOYEE_NOT_FOUND

**Usage:**
```java
throw new EmployeeNotFoundException(employeeId);
throw new EmployeeNotFoundException("Custom message");
```

**Response:**
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

---

#### 3. **DuplicateEmailException.java**
Thrown when attempting to create/update with duplicate email.

**Status Code:** 409  
**Error Code:** DUPLICATE_EMAIL

**Usage:**
```java
throw new DuplicateEmailException(email);
throw new DuplicateEmailException("Email already in use", "DUPLICATE_EMAIL");
```

**Response:**
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

---

#### 4. **InvalidEmployeeDataException.java**
Thrown when employee data validation fails.

**Status Code:** 400  
**Error Code:** INVALID_EMPLOYEE_DATA

**Usage:**
```java
throw new InvalidEmployeeDataException("Employee name is required");
throw new InvalidEmployeeDataException("Invalid salary", "INVALID_SALARY");
throw new InvalidEmployeeDataException("Invalid data", exception);
```

**Response:**
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

---

#### 5. **InvalidPaginationException.java**
Thrown when invalid pagination parameters are provided.

**Status Code:** 400  
**Error Code:** INVALID_PAGINATION

**Usage:**
```java
throw new InvalidPaginationException("Page size must be less than 100");
throw new InvalidPaginationException(-1, 10);
```

**Response:**
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid pagination parameters - Page: -1, Size: 10",
  "errorCode": "INVALID_PAGINATION",
  "path": "/api/employees/paginated/all",
  "timestamp": "2026-02-25T10:31:30"
}
```

---

#### 6. **DatabaseException.java**
Thrown when a database operation fails.

**Status Code:** 500  
**Error Code:** DATABASE_ERROR

**Usage:**
```java
throw new DatabaseException("Failed to retrieve employees");
throw new DatabaseException("Connection lost", sqlException);
throw new DatabaseException("Operation failed", "CUSTOM_CODE", cause);
```

**Response:**
```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "Failed to retrieve employees",
  "errorCode": "DATABASE_ERROR",
  "path": "/api/employees",
  "timestamp": "2026-02-25T10:31:45"
}
```

---

### Support Classes

#### 7. **ErrorResponse.java**
Data Transfer Object for standardized error responses.

**Fields:**
- `status` (int): HTTP status code
- `error` (String): HTTP status text
- `message` (String): Detailed error message
- `errorCode` (String): Machine-readable error code
- `path` (String): Request path
- `timestamp` (LocalDateTime): When error occurred
- `details` (Map): Additional error information

**Methods:**
```java
// Create response
ErrorResponse error = new ErrorResponse(404, "Not Found", 
    "Employee not found", "EMPLOYEE_NOT_FOUND", "/api/employees/1");

// Add additional details
error.addDetail("employeeId", 1);
error.addDetail("requestTime", System.currentTimeMillis());
```

---

#### 8. **GlobalExceptionHandler.java**
Central exception handler using Spring's `@RestControllerAdvice`.

**Handlers Provided:**
1. `handleEmployeeNotFoundException()` - 404
2. `handleDuplicateEmailException()` - 409
3. `handleInvalidEmployeeDataException()` - 400
4. `handleInvalidPaginationException()` - 400
5. `handleDatabaseException()` - 500
6. `handleEmsException()` - Variable
7. `handleMethodArgumentNotValidException()` - 400
8. `handleNoHandlerFoundException()` - 404
9. `handleIllegalArgumentException()` - 400
10. `handleNullPointerException()` - 500
11. `handleGlobalException()` - 500

**Features:**
- Automatic exception routing
- Consistent response format
- No controller-level try-catch needed
- Support for validation errors
- Support for generic exceptions

---

#### 9. **ExceptionConstants.java**
Centralized constants for exception messages, codes, and status codes.

**Available Constants:**

**Messages:**
```java
EMPLOYEE_NOT_FOUND
DUPLICATE_EMAIL
INVALID_NAME
INVALID_EMAIL
INVALID_SALARY
INVALID_PAGE
INVALID_SIZE
```

**Error Codes:**
```java
EMPLOYEE_NOT_FOUND_CODE
DUPLICATE_EMAIL_CODE
INVALID_EMPLOYEE_DATA_CODE
INVALID_PAGINATION_CODE
DATABASE_ERROR_CODE
INTERNAL_SERVER_ERROR_CODE
```

**HTTP Status Codes:**
```java
BAD_REQUEST = 400
NOT_FOUND = 404
CONFLICT = 409
INTERNAL_SERVER_ERROR = 500
```

---

## Usage Examples

### Service Layer

```java
@Service
public class EmployeeServiceImpl implements EmployeeService {
    
    // Validate and throw appropriate exceptions
    @Override
    public Employee saveEmployee(Employee employee) {
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new InvalidEmployeeDataException("Employee name is required");
        }
        
        if (repository.existsByEmail(employee.getEmail())) {
            throw new DuplicateEmailException(employee.getEmail());
        }
        
        return repository.save(employee);
    }
    
    // Find by ID or throw exception
    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}
```

### Controller Layer

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    // No try-catch needed! GlobalExceptionHandler handles it
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
    
    // Exceptions handled automatically
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.saveEmployee(employee), 
            HttpStatus.CREATED);
    }
}
```

### Exception Responses

```bash
# Test 404 - Not Found
curl http://localhost:8080/api/employees/999

# Test 409 - Conflict
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice.johnson@example.com",...}'

# Test 400 - Bad Request
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"test@example.com",...}'
```

---

## Exception Hierarchy

```
Exception
    └── RuntimeException
            └── EmsException
                    ├── EmployeeNotFoundException
                    ├── DuplicateEmailException
                    ├── InvalidEmployeeDataException
                    ├── InvalidPaginationException
                    └── DatabaseException
```

---

## Error Response Format

All exceptions are caught by `GlobalExceptionHandler` and converted to consistent error responses:

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

**Fields:**
- **status**: HTTP status code
- **error**: HTTP status text
- **message**: Detailed, user-friendly message
- **errorCode**: Machine-readable code for programmatic handling
- **path**: Request path that caused the error
- **timestamp**: When the error occurred
- **details**: Additional error information (optional)

---

## Best Practices

### 1. Use Specific Exceptions
```java
// ✅ Good
throw new EmployeeNotFoundException(id);

// ❌ Bad
throw new RuntimeException("Not found");
```

### 2. Provide Meaningful Messages
```java
// ✅ Good
throw new InvalidEmployeeDataException("Salary must be greater than 0");

// ❌ Bad
throw new InvalidEmployeeDataException("Invalid data");
```

### 3. Validate Early
```java
// ✅ Good - Validate before saving
if (employee.getName() == null) {
    throw new InvalidEmployeeDataException("Name is required");
}

// ❌ Bad - Let database handle validation
try {
    repository.save(employee);
} catch (Exception e) {
    // Handle later
}
```

### 4. Use Error Codes
```java
// ✅ Good - Clients can check error code
if (error.errorCode == "DUPLICATE_EMAIL") {
    // Handle duplicate email
}
```

### 5. Extend the Pattern
```java
// ✅ Good - Follow existing pattern
public class CustomException extends EmsException {
    public CustomException(String message) {
        super(message, "CUSTOM_CODE", 400);
    }
}
```

---

## Testing

### Test with cURL

```bash
# Test 404
curl -v http://localhost:8080/api/employees/999

# Test 409
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"existing@example.com",...}'

# Test 400
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"test@example.com",...}'
```

### Test with Postman

1. Import `EMS_API_Postman_Collection.json`
2. All endpoints pre-configured for testing
3. Run individual requests to see exception handling

### Test with JUnit

```java
@SpringBootTest
@AutoConfigureMockMvc
public class ExceptionHandlingTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testEmployeeNotFound() throws Exception {
        mockMvc.perform(get("/api/employees/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode")
                .value("EMPLOYEE_NOT_FOUND"));
    }
}
```

---

## Extending the Package

### Adding New Exception

1. **Create Exception Class**
```java
public class MyException extends EmsException {
    public MyException(String message) {
        super(message, "MY_ERROR_CODE", 400);
    }
}
```

2. **Add Handler in GlobalExceptionHandler**
```java
@ExceptionHandler(MyException.class)
public ResponseEntity<ErrorResponse> handleMyException(
        MyException ex, WebRequest request) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Bad Request",
        ex.getMessage(),
        ex.getErrorCode(),
        request.getDescription(false).replace("uri=", "")
    );
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}
```

3. **Update ExceptionConstants**
```java
public static final String MY_ERROR = "My error message";
public static final String MY_ERROR_CODE = "MY_ERROR_CODE";
```

4. **Use in Service**
```java
throw new MyException("Something specific went wrong");
```

5. **Document in Guide**
- Add to EXCEPTION_HANDLING_GUIDE.md
- Add test cases to EXCEPTION_HANDLING_TEST_CASES.md

---

## Documentation

For complete documentation, see:

- **EXCEPTION_HANDLING_GUIDE.md** - Complete reference
- **EXCEPTION_HANDLING_IMPLEMENTATION_SUMMARY.md** - Quick overview
- **EXCEPTION_HANDLING_TEST_CASES.md** - Test scenarios
- **EXCEPTION_HANDLING_INDEX.md** - Navigation guide

---

## Configuration

No additional configuration is required. The `GlobalExceptionHandler` is automatically detected and applied due to the `@RestControllerAdvice` annotation.

---

## Key Features

✅ **Centralized Management** - All exceptions handled in one place  
✅ **Consistent Format** - All errors follow same response format  
✅ **Proper Status Codes** - Correct HTTP status codes  
✅ **Machine-Readable Codes** - Easy programmatic handling  
✅ **Detailed Messages** - User-friendly error information  
✅ **Extensible** - Easy to add new exception types  
✅ **Well-Tested** - 20+ test cases documented  
✅ **Production-Ready** - Enterprise-grade implementation  

---

## Summary

This exception package provides professional, production-ready error handling for the EMS application. It ensures consistent error responses, proper HTTP status codes, and makes it easy for clients to handle different error scenarios.

**Status:** ✅ Production Ready  
**Version:** 1.0  
**Last Updated:** February 25, 2026

