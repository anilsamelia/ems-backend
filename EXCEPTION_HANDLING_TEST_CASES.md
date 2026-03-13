# Global Exception Handling - Test Cases

This document provides comprehensive test cases for validating the global exception handling implementation.

---

## Test Environment Setup

**Base URL:** `http://localhost:8080`

**Headers:**
```
Content-Type: application/json
```

---

## CRUD Operation Tests

### Test 1.1: Create Employee - Valid Data

**Test Case:** Create a new employee with valid data

**Request:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "dept": "IT",
    "designation": "Developer",
    "salary": 75000,
    "supervisor": "John Smith",
    "address": "123 Main St",
    "doj": "2024-01-15"
  }'
```

**Expected Response:** 201 Created
```json
{
  "id": 101,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "dept": "IT",
  "designation": "Developer",
  "salary": 75000.0,
  "supervisor": "John Smith",
  "address": "123 Main St",
  "doj": "2024-01-15"
}
```

**Status Code:** 201

---

### Test 1.2: Create Employee - Duplicate Email

**Test Case:** Try to create employee with email that already exists

**Prerequisites:** An employee with email "alice.johnson@example.com" exists

**Request:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Smith",
    "email": "alice.johnson@example.com",
    "dept": "HR",
    "designation": "Manager",
    "salary": 80000,
    "supervisor": "Jane Doe",
    "address": "456 Oak Ave",
    "doj": "2024-02-01"
  }'
```

**Expected Response:** 409 Conflict
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists: alice.johnson@example.com",
  "errorCode": "DUPLICATE_EMAIL",
  "path": "/api/employees",
  "timestamp": "2026-02-25T10:35:00",
  "details": {}
}
```

**Status Code:** 409

**Error Code:** DUPLICATE_EMAIL

---

### Test 1.3: Create Employee - Missing Name

**Test Case:** Try to create employee with empty/missing name

**Request:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "test@example.com",
    "dept": "IT",
    "designation": "Developer",
    "salary": 70000,
    "supervisor": "John Smith",
    "address": "123 Main St",
    "doj": "2024-01-15"
  }'
```

**Expected Response:** 400 Bad Request
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Employee name is required",
  "errorCode": "INVALID_EMPLOYEE_DATA",
  "path": "/api/employees",
  "timestamp": "2026-02-25T10:35:15",
  "details": {}
}
```

**Status Code:** 400

**Error Code:** INVALID_EMPLOYEE_DATA

---

### Test 1.4: Get Employee - Not Found

**Test Case:** Try to get an employee that doesn't exist

**Request:**
```bash
curl -v http://localhost:8080/api/employees/999
```

**Expected Response:** 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "errorCode": "EMPLOYEE_NOT_FOUND",
  "path": "/api/employees/999",
  "timestamp": "2026-02-25T10:35:30",
  "details": {}
}
```

**Status Code:** 404

**Error Code:** EMPLOYEE_NOT_FOUND

---

### Test 1.5: Update Employee - Valid Data

**Test Case:** Update an existing employee with valid data

**Prerequisites:** Employee with ID 1 exists

**Request:**
```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson Updated",
    "email": "alice.updated@example.com",
    "dept": "IT",
    "designation": "Senior Developer",
    "salary": 85000,
    "supervisor": "John Smith",
    "address": "789 New St",
    "doj": "2023-01-15"
  }'
```

**Expected Response:** 200 OK
```json
{
  "id": 1,
  "name": "Alice Johnson Updated",
  "email": "alice.updated@example.com",
  "dept": "IT",
  "designation": "Senior Developer",
  "salary": 85000.0,
  "supervisor": "John Smith",
  "address": "789 New St",
  "doj": "2023-01-15"
}
```

**Status Code:** 200

---

### Test 1.6: Update Employee - Not Found

**Test Case:** Try to update an employee that doesn't exist

**Request:**
```bash
curl -X PUT http://localhost:8080/api/employees/999 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test",
    "email": "test@example.com",
    "dept": "IT",
    "designation": "Developer",
    "salary": 70000,
    "supervisor": "John Smith",
    "address": "123 St",
    "doj": "2024-01-01"
  }'
```

**Expected Response:** 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "errorCode": "EMPLOYEE_NOT_FOUND",
  "path": "/api/employees/999",
  "timestamp": "2026-02-25T10:35:45",
  "details": {}
}
```

**Status Code:** 404

---

### Test 1.7: Update Employee - Duplicate Email

**Test Case:** Try to update employee with email that belongs to another employee

**Prerequisites:** 
- Employee with ID 1 has email "alice@example.com"
- Employee with ID 2 has email "bob@example.com"
- Try to update employee 1 with Bob's email

**Request:**
```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "bob.williams@example.com",
    "dept": "IT",
    "designation": "Developer",
    "salary": 60000,
    "supervisor": "John Smith",
    "address": "123 Main St",
    "doj": "2023-01-15"
  }'
```

**Expected Response:** 409 Conflict
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists: bob.williams@example.com",
  "errorCode": "DUPLICATE_EMAIL",
  "path": "/api/employees/1",
  "timestamp": "2026-02-25T10:36:00",
  "details": {}
}
```

**Status Code:** 409

---

### Test 1.8: Delete Employee - Valid ID

**Test Case:** Delete an existing employee

**Prerequisites:** Employee with ID 101 exists

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/employees/101
```

**Expected Response:** 204 No Content

**Status Code:** 204

---

### Test 1.9: Delete Employee - Not Found

**Test Case:** Try to delete an employee that doesn't exist

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/employees/999
```

**Expected Response:** 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "errorCode": "EMPLOYEE_NOT_FOUND",
  "path": "/api/employees/999",
  "timestamp": "2026-02-25T10:36:15",
  "details": {}
}
```

**Status Code:** 404

---

## Pagination Tests

### Test 2.1: Invalid Page Number

**Test Case:** Provide negative page number

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/all?page=-1&size=10"
```

**Expected:** May succeed (depends on implementation) or throw InvalidPaginationException

**Status Code:** 200 or 400

---

### Test 2.2: Invalid Page Size

**Test Case:** Provide page size greater than 100

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/all?page=0&size=200"
```

**Expected:** May succeed or throw InvalidPaginationException

**Status Code:** 200 or 400

---

### Test 2.3: Valid Pagination

**Test Case:** Get employees with valid pagination

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/all?page=0&size=10&sort=name,asc"
```

**Expected Response:** 200 OK with paginated data

**Status Code:** 200

---

## Search Tests

### Test 3.1: Search by Name - Valid

**Test Case:** Search for employees by name

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/search/name?keyword=John&page=0&size=10"
```

**Expected Response:** 200 OK with matching employees

**Status Code:** 200

---

### Test 3.2: Search by Email - Valid

**Test Case:** Search for employees by email

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/search/email?keyword=john@example.com&page=0&size=10"
```

**Expected Response:** 200 OK with matching employees

**Status Code:** 200

---

## Advanced Search Tests

### Test 4.1: Advanced Search - Valid Criteria

**Test Case:** Search with multiple criteria

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/search/advanced?dept=IT&designation=Developer&minSalary=60000&maxSalary=80000&page=0&size=10"
```

**Expected Response:** 200 OK with filtered results

**Status Code:** 200

---

### Test 4.2: Advanced Search - No Criteria

**Test Case:** Search with no criteria (should return all)

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/search/advanced?page=0&size=10"
```

**Expected Response:** 200 OK with all employees

**Status Code:** 200

---

## Utility Tests

### Test 5.1: Count by Department

**Test Case:** Count employees in a department

**Request:**
```bash
curl "http://localhost:8080/api/employees/count/dept/IT"
```

**Expected Response:** 200 OK with count
```
15
```

**Status Code:** 200

---

### Test 5.2: Check Email Exists - True

**Test Case:** Check if existing email exists

**Request:**
```bash
curl "http://localhost:8080/api/employees/exists/email?email=alice.johnson@example.com"
```

**Expected Response:** 200 OK
```
true
```

**Status Code:** 200

---

### Test 5.3: Check Email Exists - False

**Test Case:** Check if non-existing email exists

**Request:**
```bash
curl "http://localhost:8080/api/employees/exists/email?email=nonexistent@example.com"
```

**Expected Response:** 200 OK
```
false
```

**Status Code:** 200

---

## Error Scenario Tests

### Test 6.1: Endpoint Not Found

**Test Case:** Request non-existent endpoint

**Request:**
```bash
curl -v http://localhost:8080/api/invalid-endpoint
```

**Expected Response:** 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "The requested endpoint does not exist",
  "errorCode": "ENDPOINT_NOT_FOUND",
  "path": "/api/invalid-endpoint",
  "timestamp": "2026-02-25T10:36:30"
}
```

**Status Code:** 404

---

### Test 6.2: Invalid JSON Format

**Test Case:** Send malformed JSON

**Request:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{invalid json'
```

**Expected Response:** 400 Bad Request

**Status Code:** 400

---

### Test 6.3: Missing Content-Type Header

**Test Case:** POST without Content-Type header

**Request:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -d '{
    "name": "Test"
  }'
```

**Expected:** May fail with 415 Unsupported Media Type

**Status Code:** 415

---

## Test Summary Table

| Test # | Description | Expected Status | Error Code | Pass/Fail |
|--------|-------------|-----------------|-----------|-----------|
| 1.1 | Create valid employee | 201 | N/A | |
| 1.2 | Duplicate email | 409 | DUPLICATE_EMAIL | |
| 1.3 | Missing name | 400 | INVALID_EMPLOYEE_DATA | |
| 1.4 | Get non-existent | 404 | EMPLOYEE_NOT_FOUND | |
| 1.5 | Update valid | 200 | N/A | |
| 1.6 | Update non-existent | 404 | EMPLOYEE_NOT_FOUND | |
| 1.7 | Update duplicate email | 409 | DUPLICATE_EMAIL | |
| 1.8 | Delete valid | 204 | N/A | |
| 1.9 | Delete non-existent | 404 | EMPLOYEE_NOT_FOUND | |
| 2.1 | Invalid page | Variable | INVALID_PAGINATION | |
| 2.2 | Invalid size | Variable | INVALID_PAGINATION | |
| 2.3 | Valid pagination | 200 | N/A | |
| 3.1 | Search by name | 200 | N/A | |
| 3.2 | Search by email | 200 | N/A | |
| 4.1 | Advanced search | 200 | N/A | |
| 4.2 | Search no criteria | 200 | N/A | |
| 5.1 | Count by dept | 200 | N/A | |
| 5.2 | Email exists true | 200 | N/A | |
| 5.3 | Email exists false | 200 | N/A | |
| 6.1 | Invalid endpoint | 404 | ENDPOINT_NOT_FOUND | |
| 6.2 | Invalid JSON | 400 | N/A | |
| 6.3 | Missing header | 415 | N/A | |

---

## Postman Collection Testing

### Import Collection
1. Open Postman
2. Click "Import"
3. Select `EMS_API_Postman_Collection.json`

### Run Tests
All test cases above can be quickly tested using the pre-configured Postman collection.

---

## Automated Test Cases (JUnit)

Example test class (for future implementation):

```java
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerExceptionHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetEmployeeNotFound() throws Exception {
        mockMvc.perform(get("/api/employees/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode")
                .value("EMPLOYEE_NOT_FOUND"));
    }

    @Test
    public void testCreateDuplicateEmail() throws Exception {
        mockMvc.perform(post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Test\",\"email\":\"alice@example.com\",...}"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.errorCode")
                .value("DUPLICATE_EMAIL"));
    }

    @Test
    public void testCreateInvalidData() throws Exception {
        mockMvc.perform(post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"\",\"email\":\"test@example.com\",...}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode")
                .value("INVALID_EMPLOYEE_DATA"));
    }
}
```

---

## Test Execution Checklist

- [ ] All CRUD tests pass
- [ ] All pagination tests pass
- [ ] All search tests pass
- [ ] All utility tests pass
- [ ] All error scenario tests pass
- [ ] Error codes are correct
- [ ] Status codes are correct
- [ ] Error messages are clear
- [ ] Response format is consistent
- [ ] No unhandled exceptions

---

## Troubleshooting Test Failures

### Issue: 404 when employee exists
**Solution:** Verify employee ID exists in database

### Issue: 409 Conflict not thrown
**Solution:** Check if email already exists in database

### Issue: 400 Bad Request not thrown for invalid data
**Solution:** Verify validation logic in service layer

### Issue: 500 Server Error
**Solution:** Check application logs for stack trace

---

## Performance Considerations

- Test with various page sizes: 10, 20, 50, 100
- Test with large result sets (100+ records)
- Measure response times for each endpoint
- Monitor database query performance

---

## Security Testing

- Test SQL injection in search parameters
- Test XSS in input fields
- Test CORS headers
- Test rate limiting (when implemented)

---

**Last Updated:** February 25, 2026  
**Test Version:** 1.0  
**Status:** Ready for Testing ✅

