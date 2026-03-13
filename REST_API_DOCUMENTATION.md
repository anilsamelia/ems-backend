# Employee Management System (EMS) - REST API Documentation

## Table of Contents
1. [Overview](#overview)
2. [Base URL](#base-url)
3. [Authentication](#authentication)
4. [Response Format](#response-format)
5. [CRUD Endpoints](#crud-endpoints)
6. [Pagination Endpoints](#pagination-endpoints)
7. [Search Endpoints](#search-endpoints)
8. [Sorting Endpoints](#sorting-endpoints)
9. [Utility Endpoints](#utility-endpoints)
10. [Error Handling](#error-handling)
11. [Pagination Parameters](#pagination-parameters)
12. [Status Codes](#status-codes)
13. [Examples](#examples)

---

## Overview

The Employee Management System (EMS) REST API provides comprehensive endpoints for managing employee records. The API supports full CRUD operations, advanced pagination, filtering, searching, and sorting capabilities.

**Version:** 1.0  
**Last Updated:** February 25, 2026

---

## Base URL

```
http://localhost:8080/api/employees
```

---

## Authentication

Currently, no authentication is required. In production, implement OAuth2 or JWT tokens.

---

## Response Format

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

### Paginated Response (200 OK)
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
    },
    ...
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

### Error Response (4xx/5xx)
```json
{
  "timestamp": "2026-02-25T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "path": "/api/employees/999"
}
```

---

## CRUD Endpoints

### 1. Get All Employees

**Endpoint:** `GET /api/employees`

**Description:** Retrieve all employees (without pagination)

**Request:**
```
GET /api/employees
Content-Type: application/json
```

**Response:** `200 OK`
```json
[
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
  },
  ...
]
```

**Status Code:** 200 OK

---

### 2. Create New Employee

**Endpoint:** `POST /api/employees`

**Description:** Create a new employee record

**Request:**
```
POST /api/employees
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "dept": "Sales",
  "designation": "Sales Executive",
  "salary": 65000.0,
  "supervisor": "Michael Wilson",
  "address": "789 Oak Ave",
  "doj": "2023-06-01"
}
```

**Response:** `201 Created`
```json
{
  "id": 101,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "dept": "Sales",
  "designation": "Sales Executive",
  "salary": 65000.0,
  "supervisor": "Michael Wilson",
  "address": "789 Oak Ave",
  "doj": "2023-06-01"
}
```

**Status Code:** 201 Created

**Field Requirements:**
- `name` (String, max 100 chars, required)
- `email` (String, max 100 chars, unique, required)
- `dept` (String, max 50 chars)
- `designation` (String, max 100 chars)
- `salary` (Double)
- `supervisor` (String, max 100 chars)
- `address` (String, max 255 chars)
- `doj` (String, max 50 chars - Date of Joining)

---

### 3. Get Employee by ID

**Endpoint:** `GET /api/employees/{id}`

**Description:** Retrieve a specific employee by their ID

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | Employee ID |

**Request:**
```
GET /api/employees/1
Content-Type: application/json
```

**Response:** `200 OK`
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

**Status Codes:**
- 200 OK - Employee found
- 404 Not Found - Employee not found

---

### 4. Update Employee

**Endpoint:** `PUT /api/employees/{id}`

**Description:** Update an existing employee record

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | Employee ID |

**Request:**
```
PUT /api/employees/1
Content-Type: application/json

{
  "name": "Alice Smith",
  "email": "alice.smith@example.com",
  "dept": "IT",
  "designation": "Senior Developer",
  "salary": 85000.0,
  "supervisor": "John Smith",
  "address": "123 Updated St",
  "doj": "2023-01-15"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Alice Smith",
  "email": "alice.smith@example.com",
  "dept": "IT",
  "designation": "Senior Developer",
  "salary": 85000.0,
  "supervisor": "John Smith",
  "address": "123 Updated St",
  "doj": "2023-01-15"
}
```

**Status Codes:**
- 200 OK - Update successful
- 404 Not Found - Employee not found

---

### 5. Delete Employee

**Endpoint:** `DELETE /api/employees/{id}`

**Description:** Delete an employee record

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | Employee ID |

**Request:**
```
DELETE /api/employees/1
Content-Type: application/json
```

**Response:** `204 No Content`

**Status Codes:**
- 204 No Content - Employee deleted successfully
- 404 Not Found - Employee not found

---

## Pagination Endpoints

### 1. Get All Employees with Pagination

**Endpoint:** `GET /api/employees/paginated/all`

**Description:** Retrieve all employees with pagination support

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | Integer | 0 | Zero-indexed page number |
| size | Integer | 20 | Number of records per page |
| sort | String | - | Sort criteria (e.g., name,asc or salary,desc) |

**Request:**
```
GET /api/employees/paginated/all?page=0&size=10&sort=name,asc
Content-Type: application/json
```

**Response:** `200 OK`
```json
{
  "content": [...],
  "pageable": {...},
  "last": false,
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0,
  "sort": {...},
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

**Status Code:** 200 OK

---

### 2. Get Employees by Department with Pagination

**Endpoint:** `GET /api/employees/paginated/dept/{dept}`

**Description:** Retrieve employees filtered by department with pagination

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| dept | String | Yes | Department name (IT, HR, Finance, Sales, etc.) |
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |
| sort | String | No | Sort criteria |

**Request:**
```
GET /api/employees/paginated/dept/IT?page=0&size=10&sort=salary,desc
Content-Type: application/json
```

**Response:** `200 OK`
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
    },
    ...
  ],
  "pageable": {...},
  "last": true,
  "totalElements": 15,
  "totalPages": 2,
  "size": 10,
  "number": 0,
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

**Status Code:** 200 OK

---

### 3. Get Employees by Designation with Pagination

**Endpoint:** `GET /api/employees/paginated/designation/{designation}`

**Description:** Retrieve employees filtered by designation with pagination

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| designation | String | Yes | Job designation (Developer, Manager, Analyst, etc.) |
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |
| sort | String | No | Sort criteria |

**Request:**
```
GET /api/employees/paginated/designation/Developer?page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK` (same format as department endpoint)

**Status Code:** 200 OK

---

### 4. Get Employees by Supervisor with Pagination

**Endpoint:** `GET /api/employees/paginated/supervisor/{supervisor}`

**Description:** Retrieve employees filtered by supervisor with pagination

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| supervisor | String | Yes | Supervisor name |
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |
| sort | String | No | Sort criteria |

**Request:**
```
GET /api/employees/paginated/supervisor/John%20Smith?page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK` (same format as department endpoint)

**Status Code:** 200 OK

---

### 5. Get Employees by Salary Range with Pagination

**Endpoint:** `GET /api/employees/paginated/salary-range`

**Description:** Retrieve employees filtered by salary range with pagination

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| minSalary | Double | Yes | Minimum salary (inclusive) |
| maxSalary | Double | Yes | Maximum salary (inclusive) |
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |
| sort | String | No | Sort criteria |

**Request:**
```
GET /api/employees/paginated/salary-range?minSalary=50000&maxSalary=80000&page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK` (same format as department endpoint)

**Status Code:** 200 OK

---

### 6. Get Employees with Salary Greater Than

**Endpoint:** `GET /api/employees/paginated/salary-greater`

**Description:** Retrieve employees with salary greater than specified amount

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| salary | Double | Yes | Minimum salary threshold |
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |
| sort | String | No | Sort criteria |

**Request:**
```
GET /api/employees/paginated/salary-greater?salary=70000&page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK` (same format as department endpoint)

**Status Code:** 200 OK

---

### 7. Get Employees with Salary Less Than

**Endpoint:** `GET /api/employees/paginated/salary-less`

**Description:** Retrieve employees with salary less than specified amount

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| salary | Double | Yes | Maximum salary threshold |
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |
| sort | String | No | Sort criteria |

**Request:**
```
GET /api/employees/paginated/salary-less?salary=60000&page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK` (same format as department endpoint)

**Status Code:** 200 OK

---

### 8. Get Employees by Department and Designation

**Endpoint:** `GET /api/employees/paginated/dept-designation`

**Description:** Retrieve employees filtered by both department and designation

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| dept | String | Yes | Department name |
| designation | String | Yes | Job designation |
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |
| sort | String | No | Sort criteria |

**Request:**
```
GET /api/employees/paginated/dept-designation?dept=IT&designation=Developer&page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK` (same format as department endpoint)

**Status Code:** 200 OK

---

## Search Endpoints

### 1. Search by Name

**Endpoint:** `GET /api/employees/paginated/search/name`

**Description:** Search employees by name (partial match, case-insensitive)

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | String | Yes | Name or partial name to search for |
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |
| sort | String | No | Sort criteria |

**Request:**
```
GET /api/employees/paginated/search/name?keyword=John&page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 20,
      "name": "John Smith",
      "email": "john.smith@example.com",
      "dept": "IT",
      "designation": "Manager",
      "salary": 85000.0,
      "supervisor": "Jane Doe",
      "address": "456 Oak Ave",
      "doj": "2022-06-20"
    },
    ...
  ],
  "pageable": {...},
  "last": true,
  "totalElements": 3,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "numberOfElements": 3,
  "first": true,
  "empty": false
}
```

**Status Code:** 200 OK

---

### 2. Search by Email

**Endpoint:** `GET /api/employees/paginated/search/email`

**Description:** Search employees by email (partial match, case-insensitive)

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | String | Yes | Email or partial email to search for |
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |
| sort | String | No | Sort criteria |

**Request:**
```
GET /api/employees/paginated/search/email?keyword=john@example.com&page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK` (same format as name search endpoint)

**Status Code:** 200 OK

---

### 3. Advanced Multi-Criteria Search

**Endpoint:** `GET /api/employees/paginated/search/advanced`

**Description:** Advanced search with optional multiple criteria (department, designation, salary range)

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| dept | String | No | Department name (optional) |
| designation | String | No | Job designation (optional) |
| minSalary | Double | No | Minimum salary (optional) |
| maxSalary | Double | No | Maximum salary (optional) |
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |
| sort | String | No | Sort criteria |

**Request:**
```
GET /api/employees/paginated/search/advanced?dept=IT&designation=Developer&minSalary=60000&maxSalary=80000&page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK`
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
    },
    {
      "id": 18,
      "name": "Nathan Hall",
      "email": "nathan.hall@example.com",
      "dept": "IT",
      "designation": "Developer",
      "salary": 62000.0,
      "supervisor": "John Smith",
      "address": "357 Elm Rd",
      "doj": "2023-09-01"
    }
  ],
  "pageable": {...},
  "last": true,
  "totalElements": 2,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "numberOfElements": 2,
  "first": true,
  "empty": false
}
```

**Notes:**
- All parameters are optional
- If no parameters provided, returns all employees
- Filters are combined with AND logic

**Status Code:** 200 OK

---

## Sorting Endpoints

### 1. Get All Employees Sorted by Salary (Descending)

**Endpoint:** `GET /api/employees/paginated/sorted/salary-desc`

**Description:** Retrieve all employees sorted by salary from highest to lowest

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |

**Request:**
```
GET /api/employees/paginated/sorted/salary-desc?page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 99,
      "name": "Nolan Perry",
      "email": "nolan.perry@example.com",
      "dept": "IT",
      "designation": "CTO",
      "salary": 160000.0,
      "supervisor": "CEO",
      "address": "321 Maple Ave",
      "doj": "2018-04-20"
    },
    {
      "id": 76,
      "name": "Lance Coleman",
      "email": "lance.coleman@example.com",
      "dept": "Finance",
      "designation": "CFO",
      "salary": 150000.0,
      "supervisor": "CEO",
      "address": "654 Ash St",
      "doj": "2019-02-10"
    },
    ...
  ],
  "pageable": {...},
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

**Status Code:** 200 OK

---

### 2. Get All Employees Sorted by Name (Ascending)

**Endpoint:** `GET /api/employees/paginated/sorted/name-asc`

**Description:** Retrieve all employees sorted by name alphabetically (A-Z)

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | Integer | No | Zero-indexed page number (default: 0) |
| size | Integer | No | Number of records per page (default: 20) |

**Request:**
```
GET /api/employees/paginated/sorted/name-asc?page=0&size=10
Content-Type: application/json
```

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 28,
      "name": "Adam Nelson",
      "email": "adam.nelson@example.com",
      "dept": "HR",
      "designation": "Specialist",
      "salary": 51000.0,
      "supervisor": "Jane Doe",
      "address": "456 Willow Rd",
      "doj": "2023-01-10"
    },
    {
      "id": 56,
      "name": "Amber Ramirez",
      "email": "amber.ramirez@example.com",
      "dept": "Sales",
      "designation": "Product Manager",
      "salary": 76000.0,
      "supervisor": "Michael Wilson",
      "address": "789 Spruce Ave",
      "doj": "2020-03-15"
    },
    ...
  ],
  "pageable": {...},
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

**Status Code:** 200 OK

---

## Utility Endpoints

### 1. Count Employees by Department

**Endpoint:** `GET /api/employees/count/dept/{dept}`

**Description:** Get the total count of employees in a specific department

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| dept | String | Yes | Department name |

**Request:**
```
GET /api/employees/count/dept/IT
Content-Type: application/json
```

**Response:** `200 OK`
```json
15
```

**Status Code:** 200 OK

**Examples:**
- `GET /api/employees/count/dept/IT` → `15`
- `GET /api/employees/count/dept/HR` → `12`
- `GET /api/employees/count/dept/Finance` → `18`
- `GET /api/employees/count/dept/Sales` → `20`

---

### 2. Count Employees by Designation

**Endpoint:** `GET /api/employees/count/designation/{designation}`

**Description:** Get the total count of employees with a specific designation

**Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| designation | String | Yes | Job designation |

**Request:**
```
GET /api/employees/count/designation/Developer
Content-Type: application/json
```

**Response:** `200 OK`
```json
8
```

**Status Code:** 200 OK

**Examples:**
- `GET /api/employees/count/designation/Developer` → `8`
- `GET /api/employees/count/designation/Manager` → `12`
- `GET /api/employees/count/designation/Analyst` → `6`

---

### 3. Check if Employee Exists by Email

**Endpoint:** `GET /api/employees/exists/email`

**Description:** Check if an employee with a specific email address exists

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| email | String | Yes | Email address to check |

**Request:**
```
GET /api/employees/exists/email?email=alice.johnson@example.com
Content-Type: application/json
```

**Response:** `200 OK`
```json
true
```

**Status Code:** 200 OK

**Examples:**
- `GET /api/employees/exists/email?email=alice.johnson@example.com` → `true`
- `GET /api/employees/exists/email?email=nonexistent@example.com` → `false`

---

## Error Handling

### Common Error Responses

#### 400 Bad Request
Occurs when invalid parameters are provided or required fields are missing.

```json
{
  "timestamp": "2026-02-25T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid parameters provided",
  "path": "/api/employees"
}
```

#### 404 Not Found
Occurs when the requested resource does not exist.

```json
{
  "timestamp": "2026-02-25T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "path": "/api/employees/999"
}
```

#### 409 Conflict
Occurs when trying to create an employee with a duplicate email.

```json
{
  "timestamp": "2026-02-25T10:30:00.000+00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists: alice.johnson@example.com",
  "path": "/api/employees"
}
```

#### 500 Internal Server Error
Occurs when an unexpected server error happens.

```json
{
  "timestamp": "2026-02-25T10:30:00.000+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/employees"
}
```

---

## Pagination Parameters

### Understanding Pagination

Pagination helps manage large datasets by breaking them into smaller, manageable pages.

#### Page Parameter
- **Range:** 0 to (totalPages - 1)
- **Default:** 0
- **Example:** `page=0` returns the first page

#### Size Parameter
- **Range:** 1 to any positive integer (recommended max: 100)
- **Default:** 20
- **Example:** `size=15` returns 15 records per page

#### Sort Parameter
- **Format:** `fieldName,direction` or `fieldName,direction;field2,direction`
- **Direction:** `asc` (ascending) or `desc` (descending)
- **Sortable Fields:** `id`, `name`, `email`, `dept`, `designation`, `salary`, `supervisor`, `address`, `doj`
- **Default:** Unsorted (by insertion order)

### Sort Examples
```
?sort=name,asc                    → Sort by name A-Z
?sort=salary,desc                 → Sort by salary high to low
?sort=salary,desc&sort=name,asc   → Sort by salary DESC, then name ASC
?sort=doj,desc                    → Sort by date of joining (newest first)
```

### Complete Pagination Example
```
GET /api/employees/paginated/dept/IT?page=1&size=15&sort=salary,desc&sort=name,asc
```

This request:
- Filters employees in the IT department
- Returns page 1 (records 15-29)
- Shows 15 records per page
- Sorts first by salary (highest to lowest)
- Then sorts by name (A-Z)

---

## Status Codes

| Code | Status | Description |
|------|--------|-------------|
| 200 | OK | Request successful, data returned |
| 201 | Created | Resource created successfully |
| 204 | No Content | Request successful, no content to return (DELETE) |
| 400 | Bad Request | Invalid request parameters |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource conflict (e.g., duplicate email) |
| 500 | Internal Server Error | Server error |
| 503 | Service Unavailable | Server temporarily unavailable |

---

## Examples

### Example 1: Create an Employee

**Request:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "dept": "HR",
    "designation": "Recruiter",
    "salary": 55000.0,
    "supervisor": "Jane Doe",
    "address": "789 Main St",
    "doj": "2023-08-10"
  }'
```

**Response:**
```json
{
  "id": 101,
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "dept": "HR",
  "designation": "Recruiter",
  "salary": 55000.0,
  "supervisor": "Jane Doe",
  "address": "789 Main St",
  "doj": "2023-08-10"
}
```

---

### Example 2: Search Developers in IT Department with Salary Range

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/search/advanced?dept=IT&designation=Developer&minSalary=60000&maxSalary=80000&page=0&size=10&sort=salary,desc"
```

**Response:**
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
    },
    {
      "id": 18,
      "name": "Nathan Hall",
      "email": "nathan.hall@example.com",
      "dept": "IT",
      "designation": "Developer",
      "salary": 62000.0,
      "supervisor": "John Smith",
      "address": "357 Elm Rd",
      "doj": "2023-09-01"
    }
  ],
  "pageable": {...},
  "last": true,
  "totalElements": 2,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "numberOfElements": 2,
  "first": true,
  "empty": false
}
```

---

### Example 3: Get Highest Paid Employees (Page 1)

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/sorted/salary-desc?page=0&size=5"
```

**Response:**
```json
{
  "content": [
    {
      "id": 99,
      "name": "Nolan Perry",
      "email": "nolan.perry@example.com",
      "dept": "IT",
      "designation": "CTO",
      "salary": 160000.0,
      "supervisor": "CEO",
      "address": "321 Maple Ave",
      "doj": "2018-04-20"
    },
    {
      "id": 76,
      "name": "Lance Coleman",
      "email": "lance.coleman@example.com",
      "dept": "Finance",
      "designation": "CFO",
      "salary": 150000.0,
      "supervisor": "CEO",
      "address": "654 Ash St",
      "doj": "2019-02-10"
    },
    {
      "id": 77,
      "name": "Meredith Jenkins",
      "email": "meredith.jenkins@example.com",
      "dept": "Sales",
      "designation": "Chief Sales Officer",
      "salary": 140000.0,
      "supervisor": "CEO",
      "address": "789 Oak Rd",
      "doj": "2019-03-15"
    },
    {
      "id": 78,
      "name": "Ophelia Powell",
      "email": "ophelia.powell@example.com",
      "dept": "HR",
      "designation": "Chief HR Officer",
      "salary": 135000.0,
      "supervisor": "CEO",
      "address": "147 Pine Rd",
      "doj": "2019-05-25"
    },
    {
      "id": 85,
      "name": "Yara Doyle",
      "email": "yara.doyle@example.com",
      "dept": "Sales",
      "designation": "Executive Vice President",
      "salary": 130000.0,
      "supervisor": "CEO",
      "address": "789 Elm Rd",
      "doj": "2018-03-10"
    }
  ],
  "pageable": {...},
  "last": false,
  "totalElements": 100,
  "totalPages": 20,
  "size": 5,
  "number": 0,
  "numberOfElements": 5,
  "first": true,
  "empty": false
}
```

---

### Example 4: Update Employee Information

**Request:**
```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice.new@example.com",
    "dept": "IT",
    "designation": "Senior Developer",
    "salary": 75000.0,
    "supervisor": "John Smith",
    "address": "456 New St",
    "doj": "2023-01-15"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Alice Johnson",
  "email": "alice.new@example.com",
  "dept": "IT",
  "designation": "Senior Developer",
  "salary": 75000.0,
  "supervisor": "John Smith",
  "address": "456 New St",
  "doj": "2023-01-15"
}
```

---

### Example 5: Delete Employee

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/employees/101
```

**Response:**
```
204 No Content
```

---

### Example 6: Count Employees in Each Department

**Request:**
```bash
curl "http://localhost:8080/api/employees/count/dept/IT"
curl "http://localhost:8080/api/employees/count/dept/HR"
curl "http://localhost:8080/api/employees/count/dept/Finance"
curl "http://localhost:8080/api/employees/count/dept/Sales"
```

**Responses:**
```
15
12
18
20
```

---

### Example 7: Search by Name with Pagination

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/search/name?keyword=John&page=0&size=5"
```

**Response:**
```json
{
  "content": [
    {
      "id": 20,
      "name": "John Smith",
      "email": "john.smith@example.com",
      "dept": "IT",
      "designation": "Manager",
      "salary": 85000.0,
      "supervisor": "Jane Doe",
      "address": "456 Oak Ave",
      "doj": "2022-06-20"
    },
    {
      "id": 74,
      "name": "John Miller",
      "email": "john.miller@example.com",
      "dept": "Sales",
      "designation": "Sales Manager",
      "salary": 70000.0,
      "supervisor": "Michael Wilson",
      "address": "123 Main St",
      "doj": "2023-01-15"
    },
    {
      "id": 88,
      "name": "John Williamson",
      "email": "john.williamson@example.com",
      "dept": "Finance",
      "designation": "Financial Controller",
      "salary": 95000.0,
      "supervisor": "Robert Davis",
      "address": "789 Oak St",
      "doj": "2021-02-20"
    }
  ],
  "pageable": {...},
  "last": true,
  "totalElements": 3,
  "totalPages": 1,
  "size": 5,
  "number": 0,
  "numberOfElements": 3,
  "first": true,
  "empty": false
}
```

---

### Example 8: Get HR Employees Sorted by Name

**Request:**
```bash
curl "http://localhost:8080/api/employees/paginated/dept/HR?page=0&size=20&sort=name,asc"
```

**Response:**
```json
{
  "content": [
    {
      "id": 7,
      "name": "Grace Lee",
      "email": "grace.lee@example.com",
      "dept": "HR",
      "designation": "Recruiter",
      "salary": 52000.0,
      "supervisor": "Jane Doe",
      "address": "147 Birch Ave",
      "doj": "2022-11-15"
    },
    {
      "id": 11,
      "name": "Karen Clark",
      "email": "karen.clark@example.com",
      "dept": "HR",
      "designation": "Specialist",
      "salary": 50000.0,
      "supervisor": "Jane Doe",
      "address": "852 Oak St",
      "doj": "2023-06-14"
    },
    ...
  ],
  "pageable": {...},
  "last": true,
  "totalElements": 12,
  "totalPages": 1,
  "size": 20,
  "number": 0,
  "numberOfElements": 12,
  "first": true,
  "empty": false
}
```

---

## Rate Limiting

Currently, no rate limiting is implemented. In production, implement rate limiting to prevent API abuse.

---

## API Versioning

The current API version is v1. Future versions may be released as:
- `/api/v2/employees`
- `/api/v3/employees`

---

## Security Notes

1. **In Production:**
   - Implement SSL/TLS (HTTPS)
   - Add authentication (OAuth2, JWT)
   - Implement rate limiting
   - Add input validation
   - Implement CORS policies
   - Add audit logging

2. **Data Protection:**
   - Hash and salt passwords
   - Encrypt sensitive data
   - Implement proper access controls
   - Follow GDPR compliance

---

## Changelog

### Version 1.0 (February 25, 2026)
- Initial release
- Full CRUD operations
- Comprehensive pagination support
- Advanced search and filtering
- Multiple sorting options
- Utility endpoints for counting and existence checks
- 100 test employee records

---

## Support

For API support or issues, please contact:
- **Email:** support@example.com
- **Documentation:** https://docs.example.com
- **Issues:** https://github.com/example/ems/issues

---

## License

This API is proprietary and subject to the terms and conditions of the EMS system.

---

**Last Updated:** February 25, 2026  
**API Version:** 1.0  
**Status:** Active ✅

