# EMS API - Quick Reference (Fixed)

## Currently Implemented Endpoints (6 Total)

### CRUD Operations

#### 1. Create Employee
```
POST /api/employees
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "dept": "IT",
  "designation": "Senior Developer",
  "salary": 75000.0,
  "supervisor": "Jane Smith",
  "address": "123 Main St",
  "doj": "2023-01-15"
}
```
- Status: 201 Created
- Required: name, email
- Returns: Created Employee object with ID

---

#### 2. Get Employee by ID
```
GET /api/employees/{id}
```
- Status: 200 OK
- Status: 404 Not Found
- Returns: Employee object

---

#### 3. Update Employee
```
PUT /api/employees/{id}
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "dept": "HR",
  "designation": "Manager",
  "salary": 80000.0,
  "supervisor": "John Smith",
  "address": "456 Oak Ave",
  "doj": "2023-02-01"
}
```
- Status: 200 OK
- Status: 404 Not Found
- Returns: Updated Employee object

---

#### 4. Delete Employee
```
DELETE /api/employees/{id}
```
- Status: 204 No Content
- Status: 404 Not Found
- Returns: No body

---

### Pagination & Search

#### 5. Get All Employees with Pagination
```
GET /api/employees/paginated/all?page=0&size=10&sort=name,asc
```

Query Parameters:
- `page` (integer): Page number (default: 0)
- `size` (integer): Records per page (default: 20, max: 100)
- `sort` (string): Sort criteria, e.g., `name,asc` or `salary,desc`

Status: 200 OK
Returns: PagedEmployeeResponse with pagination metadata

Example Response:
```json
{
  "content": [
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
  ],
  "pageable": { ... },
  "last": false,
  "totalElements": 100,
  "totalPages": 5,
  "size": 20,
  "number": 0,
  "numberOfElements": 20,
  "first": true,
  "empty": false
}
```

---

#### 6. Advanced Search with Multiple Criteria
```
GET /api/employees/paginated/search/advanced?dept=IT&designation=Developer&minSalary=50000&maxSalary=80000&page=0&size=10
```

Query Parameters (all optional):
- `dept` (string): Department name
- `designation` (string): Job designation
- `minSalary` (number): Minimum salary
- `maxSalary` (number): Maximum salary
- `page` (integer): Page number
- `size` (integer): Records per page
- `sort` (string): Sort criteria

Status: 200 OK
Returns: PagedEmployeeResponse with filtered results

---

## Employee Model

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

### Field Constraints
| Field | Type | Required | Max Length | Notes |
|-------|------|----------|------------|-------|
| id | Long | No | - | Auto-generated |
| name | String | Yes | 100 | - |
| email | String | Yes | 100 | Must be unique |
| dept | String | No | 50 | - |
| designation | String | No | 100 | - |
| salary | Double | No | - | - |
| supervisor | String | No | 100 | - |
| address | String | No | 255 | - |
| doj | String | No | 50 | Date of Joining |

---

## Common Sort Examples

```
?sort=name,asc                    → Sort by name A-Z
?sort=salary,desc                 → Sort by salary high to low
?sort=salary,desc&sort=name,asc   → Sort by salary DESC, then name ASC
?sort=doj,desc                    → Sort by date of joining (newest first)
```

---

## HTTP Status Codes

| Code | Meaning | When |
|------|---------|------|
| 200 | OK | Successful GET, PUT, DELETE operation |
| 201 | Created | Successful POST (resource created) |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Invalid input data |
| 404 | Not Found | Employee or resource not found |
| 409 | Conflict | Email already exists (duplicate) |
| 500 | Server Error | Unexpected server error |

---

## Notes

- All endpoints require Bearer token authentication (JWT)
- API base URL: `http://localhost:8080`
- Swagger UI available at: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec available at: `http://localhost:8080/v3/api-docs`

---

## Updated Documentation Status

✅ openapi.yaml - Synchronized with actual endpoints
✅ data.sql - Fixed data type issues
✅ EmployeeController - 6 endpoints implemented
✅ OpenAPI Swagger UI - Accurate documentation

