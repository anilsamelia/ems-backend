# Swagger/OpenAPI Integration - Complete Guide

## Overview
The EMS API now includes comprehensive Swagger/OpenAPI 3.0 documentation with interactive UI powered by SpringDoc OpenAPI.

## 📌 Quick Links

### **Swagger UI** (Interactive API Documentation)
- **URL**: `http://localhost:8080/swagger-ui.html`
- **Alternative**: `http://localhost:8080/swagger-ui/index.html`
- Interactive testing of all API endpoints
- Try-it-out feature for live requests
- Schema visualization

### **OpenAPI Specification** (JSON)
- **URL**: `http://localhost:8080/v3/api-docs`
- Machine-readable API specification
- Can be imported into API clients (Postman, Insomnia, etc.)

### **H2 Console**
- **URL**: `http://localhost:8080/h2-console`
- Database viewer and query tool
- Default credentials: sa / (empty password)

---

## 🔐 Authentication

All endpoints except `/api/auth/login` and `/api/auth/signup` require JWT authentication.

### Getting a JWT Token

**Step 1:** Login with default credentials
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "welcome"
  }'
```

**Step 2:** Response contains JWT token
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "roles": ["ADMIN"]
}
```

**Step 3:** Use token in Authorization header
```bash
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### In Swagger UI
1. Click the **"Authorize"** button (🔒) at the top right
2. Enter: `Bearer eyJhbGciOiJIUzUxMiJ9...` (full token with Bearer prefix)
3. Click **"Authorize"**
4. All subsequent requests will include the token

---

## 📚 API Endpoints Overview

### **Authentication Endpoints** (`/api/auth`)
No authentication required for login/signup

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login with username and password |
| POST | `/api/auth/signup` | Register new user account |
| POST | `/api/auth/refresh` | Refresh JWT token (requires valid token) |

### **Employee Endpoints** (`/api/employees`)
All require JWT authentication (except indicated)

#### Basic CRUD
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees` | Get all employees (no pagination) |
| POST | `/api/employees` | Create new employee |
| GET | `/api/employees/{id}` | Get employee by ID |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

#### Pagination Endpoints
| Method | Endpoint | Description | Query Params |
|--------|----------|-------------|--------------|
| GET | `/api/employees/paginated/all` | Get all with pagination | `page`, `size`, `sort` |
| GET | `/api/employees/paginated/dept/{dept}` | Filter by department | `page`, `size`, `sort` |
| GET | `/api/employees/paginated/designation/{designation}` | Filter by designation | `page`, `size`, `sort` |
| GET | `/api/employees/paginated/search/name` | Search by name | `keyword`, `page`, `size` |
| GET | `/api/employees/paginated/search/email` | Search by email | `keyword`, `page`, `size` |
| GET | `/api/employees/paginated/supervisor/{supervisor}` | Filter by supervisor | `page`, `size`, `sort` |
| GET | `/api/employees/paginated/salary-range` | Salary range filter | `minSalary`, `maxSalary`, `page`, `size` |
| GET | `/api/employees/paginated/salary-greater` | Salary greater than | `salary`, `page`, `size` |
| GET | `/api/employees/paginated/salary-less` | Salary less than | `salary`, `page`, `size` |
| GET | `/api/employees/paginated/dept-designation` | Multi-filter | `dept`, `designation`, `page`, `size` |
| GET | `/api/employees/paginated/sorted/salary-desc` | Sort by salary desc | `page`, `size` |
| GET | `/api/employees/paginated/sorted/name-asc` | Sort by name asc | `page`, `size` |
| GET | `/api/employees/paginated/search/advanced` | Advanced multi-criteria search | `dept`, `designation`, `minSalary`, `maxSalary`, `page`, `size` |

#### Utility Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees/count/dept/{dept}` | Count employees by department |
| GET | `/api/employees/count/designation/{designation}` | Count by designation |
| GET | `/api/employees/exists/email` | Check if email exists |

---

## 🔍 Pagination Parameters

### Standard Pagination
```
GET /api/employees/paginated/all?page=0&size=10&sort=name,asc
```

**Parameters:**
- `page`: Page number (0-indexed), default 0
- `size`: Items per page, default 10
- `sort`: Sort field and direction (field,asc|desc)

**Example Responses:**

Request:
```bash
GET /api/employees/paginated/all?page=0&size=2&sort=salary,desc
```

Response:
```json
{
  "content": [
    {
      "id": 51,
      "name": "Yara Doyle",
      "email": "yara.doyle@example.com",
      "dept": "Sales",
      "designation": "Executive Vice President",
      "salary": 130000.0,
      "supervisor": "CEO",
      "address": "789 Elm Rd",
      "doj": "2018-03-10"
    },
    {
      "id": 23,
      "name": "Caleb Mitchell",
      "email": "caleb.mitchell@example.com",
      "dept": "Sales",
      "designation": "VP",
      "salary": 105000.0,
      "supervisor": "CEO",
      "address": "789 Oak St",
      "doj": "2019-03-15"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 2,
    "sort": [
      {
        "direction": "DESC",
        "property": "salary",
        "ignoreCase": false,
        "nullHandling": "NATIVE",
        "ascending": false
      }
    ],
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalElements": 100,
  "totalPages": 50,
  "size": 2,
  "number": 0,
  "numberOfElements": 2,
  "first": true,
  "empty": false
}
```

---

## 📝 Example API Usage

### 1. User Registration
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }'
```

### 2. User Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "welcome"
  }'
```

### 3. Get All Employees (Paginated)
```bash
curl -X GET "http://localhost:8080/api/employees/paginated/all?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Create Employee
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "dept": "IT",
    "designation": "Software Engineer",
    "salary": 75000.0,
    "supervisor": "John Doe",
    "address": "456 Oak Avenue",
    "doj": "2024-01-15"
  }'
```

### 5. Search Employees by Name
```bash
curl -X GET "http://localhost:8080/api/employees/paginated/search/name?keyword=Alice&page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 6. Advanced Search (Multiple Criteria)
```bash
curl -X GET "http://localhost:8080/api/employees/paginated/search/advanced?dept=IT&designation=Developer&minSalary=50000&maxSalary=80000&page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🔑 Default Test Users

| Username | Email | Password | Role |
|----------|-------|----------|------|
| admin | admin@example.com | welcome | ADMIN |
| manager | manager@example.com | welcome | MANAGER |
| user | user@example.com | welcome | USER |

---

## 📋 Data Models

### LoginRequest
```json
{
  "username": "string",
  "password": "string"
}
```

### LoginResponse
```json
{
  "id": 1,
  "username": "string",
  "email": "string",
  "token": "string",
  "tokenType": "Bearer",
  "roles": ["ADMIN", "MANAGER"]
}
```

### SignUpRequest
```json
{
  "username": "string (3-100 chars)",
  "email": "string (valid email)",
  "password": "string (min 6 chars)",
  "confirmPassword": "string (must match password)"
}
```

### Employee
```json
{
  "id": 1,
  "name": "string (required, max 100)",
  "email": "string (unique, max 100)",
  "dept": "string (max 50)",
  "designation": "string (max 100)",
  "salary": 60000.0,
  "supervisor": "string (max 100)",
  "address": "string (max 255)",
  "doj": "string (date format YYYY-MM-DD)"
}
```

---

## 🛠️ Swagger Configuration Files

### SwaggerConfig.java
Located in: `src/main/java/com/ems/config/SwaggerConfig.java`

Features:
- OpenAPI 3.0 specification
- JWT Bearer authentication scheme
- API title, version, and description
- Contact and license information
- Unified security requirement for protected endpoints

### application.properties
Swagger UI configuration properties:
```properties
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.operations-sorter=method
springdoc.swagger-ui.tags-sorter=alpha
```

---

## 🚀 Accessing Swagger UI

### Local Development
1. Start the application:
   ```bash
   mvn spring-boot:run
   ```

2. Open browser to:
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Docs JSON: `http://localhost:8080/v3/api-docs`

### Export OpenAPI Specification
1. Visit `http://localhost:8080/v3/api-docs`
2. Save the JSON content
3. Import into tools like:
   - **Postman**: Import → Paste Raw Text
   - **Insomnia**: Import → Paste URL
   - **Swagger Editor**: https://editor.swagger.io/

---

## 📊 Swagger UI Features

1. **Interactive API Testing**
   - "Try it out" button for each endpoint
   - Auto-filled request examples
   - Real-time response preview

2. **Schema Documentation**
   - View all data model structures
   - Field descriptions and constraints
   - Example values

3. **Authorization**
   - Centralized JWT token management
   - Automatic token injection in requests
   - Token refresh workflow

4. **Sorting & Organization**
   - Operations sorted by HTTP method
   - Tags sorted alphabetically
   - Search functionality

---

## 🔗 Integration Examples

### Postman
1. Click **Import** → **Paste Raw Text**
2. Paste OpenAPI specification from `/v3/api-docs`
3. Collection auto-generates with all endpoints
4. Add Authorization tab with JWT token

### Insomnia
1. Click **Create** → **Import from URL**
2. Enter: `http://localhost:8080/v3/api-docs`
3. All endpoints auto-imported
4. Configure Bearer Token in Auth

---

## ✅ Best Practices

1. **Always authenticate first** - Get JWT token before testing protected endpoints
2. **Use pagination** - For large datasets, use `/paginated/*` endpoints
3. **Specify sort order** - Default sorting may vary; explicitly set `sort` parameter
4. **Validate input** - Check schema requirements in Swagger UI
5. **Check status codes** - Review HTTP responses in Swagger documentation

---

## 🎯 Common Use Cases

### Search for employees in IT department
```bash
GET /api/employees/paginated/dept/IT?page=0&size=20&sort=name,asc
```

### Find high earners
```bash
GET /api/employees/paginated/salary-greater?salary=80000&page=0&size=10&sort=salary,desc
```

### Search specific employee
```bash
GET /api/employees/paginated/search/email?keyword=alice@example.com
```

### Count department employees
```bash
GET /api/employees/count/dept/IT
```

### Complex multi-criteria search
```bash
GET /api/employees/paginated/search/advanced?dept=IT&designation=Developer&minSalary=60000&maxSalary=90000&page=0&size=10
```

---

## 📞 Support

For issues or questions:
1. Check Swagger UI documentation at `/swagger-ui.html`
2. Review API specification at `/v3/api-docs`
3. Check application logs for error details

---

**Last Updated**: February 27, 2026
**Swagger Version**: SpringDoc OpenAPI 2.0.2
**OpenAPI Specification**: 3.0.0

