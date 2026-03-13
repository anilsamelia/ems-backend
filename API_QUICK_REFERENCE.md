# EMS REST API - Quick Reference Guide

## 🚀 Quick Start

### Base URL
```
http://localhost:8080/api/employees
```

---

## 📋 Endpoint Summary

### CRUD Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Get all employees |
| POST | `/` | Create new employee |
| GET | `/{id}` | Get employee by ID |
| PUT | `/{id}` | Update employee |
| DELETE | `/{id}` | Delete employee |

### Pagination & Filtering
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/paginated/all` | All with pagination |
| GET | `/paginated/dept/{dept}` | By department |
| GET | `/paginated/designation/{designation}` | By designation |
| GET | `/paginated/supervisor/{supervisor}` | By supervisor |
| GET | `/paginated/salary-range` | By salary range |
| GET | `/paginated/salary-greater` | Salary > X |
| GET | `/paginated/salary-less` | Salary < X |
| GET | `/paginated/dept-designation` | Dept + Designation |

### Search
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/paginated/search/name` | Search by name |
| GET | `/paginated/search/email` | Search by email |
| GET | `/paginated/search/advanced` | Multi-criteria search |

### Sorting
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/paginated/sorted/salary-desc` | By salary (high→low) |
| GET | `/paginated/sorted/name-asc` | By name (A→Z) |

### Utility
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/count/dept/{dept}` | Count by dept |
| GET | `/count/designation/{designation}` | Count by designation |
| GET | `/exists/email` | Check email existence |

---

## 🔧 Common Query Parameters

### Pagination
```
?page=0              # Page number (0-indexed, default: 0)
&size=10             # Records per page (default: 20)
&sort=name,asc       # Sort field and direction
```

### Examples
```
?page=0&size=15&sort=salary,desc&sort=name,asc
?page=1&size=20&sort=doj,desc
```

---

## 📝 Request/Response Examples

### Create Employee
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "dept": "HR",
    "designation": "Recruiter",
    "salary": 55000,
    "supervisor": "John Doe",
    "address": "789 Main St",
    "doj": "2023-08-10"
  }'
```

### Get All with Pagination
```bash
curl "http://localhost:8080/api/employees/paginated/all?page=0&size=10&sort=name,asc"
```

### Search by Department
```bash
curl "http://localhost:8080/api/employees/paginated/dept/IT?page=0&size=10&sort=salary,desc"
```

### Advanced Search
```bash
curl "http://localhost:8080/api/employees/paginated/search/advanced?dept=IT&designation=Developer&minSalary=60000&maxSalary=80000&page=0&size=10"
```

### Search by Name
```bash
curl "http://localhost:8080/api/employees/paginated/search/name?keyword=John&page=0&size=10"
```

### Get Top 5 Highest Paid
```bash
curl "http://localhost:8080/api/employees/paginated/sorted/salary-desc?page=0&size=5"
```

### Count IT Department Employees
```bash
curl "http://localhost:8080/api/employees/count/dept/IT"
```

### Check Email Exists
```bash
curl "http://localhost:8080/api/employees/exists/email?email=john@example.com"
```

---

## 🎯 Employee Model

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
- `id`: Auto-generated, Long
- `name`: Required, max 100 chars, non-null
- `email`: Required, unique, max 100 chars
- `dept`: Max 50 chars
- `designation`: Max 100 chars
- `salary`: Double
- `supervisor`: Max 100 chars
- `address`: Max 255 chars
- `doj`: Date of Joining, max 50 chars

---

## 🌐 HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | ✅ OK - Request successful |
| 201 | ✅ Created - Resource created |
| 204 | ✅ No Content - Delete successful |
| 400 | ❌ Bad Request - Invalid parameters |
| 404 | ❌ Not Found - Resource not found |
| 409 | ❌ Conflict - Email already exists |
| 500 | ❌ Server Error |

---

## 💡 Tips & Tricks

### 1. Sort by Multiple Fields
```
?sort=salary,desc&sort=name,asc
```
First sorts by salary (high to low), then by name (A-Z)

### 2. Large Result Sets
```
?page=0&size=50&sort=id,asc
```
Use larger page sizes for better performance

### 3. Search with Pagination
```
?keyword=john&page=0&size=10&sort=name,asc
```
Combine search with pagination for large result sets

### 4. Count Operations
```
GET /api/employees/count/dept/IT
```
Returns: `15` (as plain number)

### 5. Check Before Create
```bash
# Check if email exists
curl "http://localhost:8080/api/employees/exists/email?email=new@example.com"
# Returns: true or false
```

---

## 🔗 Available Departments
- IT
- HR
- Finance
- Sales

## 💼 Available Designations
- Developer
- Senior Developer
- Manager
- Analyst
- Senior Analyst
- Engineer
- Executive
- Coordinator
- Specialist
- Officer
- And more...

---

## 📊 Response Format

### Paginated Response
```json
{
  "content": [
    { "id": 1, "name": "...", ... },
    { "id": 2, "name": "...", ... }
  ],
  "pageable": { "sort": {...}, "pageNumber": 0, "pageSize": 10 },
  "last": false,
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0,
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

### Error Response
```json
{
  "timestamp": "2026-02-25T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "path": "/api/employees/999"
}
```

---

## 🎓 Common Use Cases

### Get IT Developers with Salary 60k-80k
```bash
curl "http://localhost:8080/api/employees/paginated/search/advanced?dept=IT&designation=Developer&minSalary=60000&maxSalary=80000&page=0&size=10"
```

### Find All Employees Named John
```bash
curl "http://localhost:8080/api/employees/paginated/search/name?keyword=John&page=0&size=20&sort=name,asc"
```

### Get Top 10 Highest Paid Employees
```bash
curl "http://localhost:8080/api/employees/paginated/sorted/salary-desc?page=0&size=10"
```

### Get All HR Employees Sorted by Name
```bash
curl "http://localhost:8080/api/employees/paginated/dept/HR?page=0&size=20&sort=name,asc"
```

### Count Developers in Company
```bash
curl "http://localhost:8080/api/employees/count/designation/Developer"
```

### Get Employees with Salary > 100k
```bash
curl "http://localhost:8080/api/employees/paginated/salary-greater?salary=100000&page=0&size=10"
```

### Get Employees Earning 40k-60k
```bash
curl "http://localhost:8080/api/employees/paginated/salary-range?minSalary=40000&maxSalary=60000&page=0&size=20&sort=salary,desc"
```

---

## 🛠️ Testing with cURL

### 1. Create Employee
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Test User",
    "email":"test@example.com",
    "dept":"IT",
    "designation":"Developer",
    "salary":70000,
    "supervisor":"John Smith",
    "address":"Test St",
    "doj":"2024-01-01"
  }'
```

### 2. Get by ID
```bash
curl http://localhost:8080/api/employees/1
```

### 3. Update
```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Updated Name",
    "email":"updated@example.com",
    "dept":"HR",
    "designation":"Manager",
    "salary":80000,
    "supervisor":"Jane Doe",
    "address":"Updated St",
    "doj":"2024-01-01"
  }'
```

### 4. Delete
```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

---

## 📚 Testing with Postman

### Import OpenAPI Spec
1. Open Postman
2. Click "Import"
3. Select "openapi.yaml" from the project root
4. All endpoints will be auto-imported with proper structure

### Quick Collections
- **CRUD**: POST, GET, PUT, DELETE operations
- **Pagination**: Filter and page through employees
- **Search**: Name, email, and advanced search
- **Sorting**: Sort by salary and name
- **Utility**: Count and existence checks

---

## 🐛 Troubleshooting

### 404 Not Found
- Check if employee ID exists
- Verify the endpoint path

### 409 Conflict
- Email already exists in system
- Use different email address

### 400 Bad Request
- Check query parameter format
- Verify required fields in POST/PUT body

### 500 Server Error
- Check server logs
- Ensure database is running

---

## 📖 Full Documentation

See `REST_API_DOCUMENTATION.md` for complete details including:
- Detailed endpoint descriptions
- All response formats
- Field constraints
- Error handling
- Complex examples

---

## 🔗 Resources

- **OpenAPI Spec**: `openapi.yaml`
- **Full Docs**: `REST_API_DOCUMENTATION.md`
- **Pagination Guide**: `PAGINATION_UPDATES.md`

---

**Last Updated**: February 25, 2026  
**API Version**: 1.0  
**Status**: Active ✅

