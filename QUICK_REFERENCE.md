# Quick Reference: Spring Security JWT

## Test Users
| User | Password | Role |
|------|----------|------|
| admin | admin123 | ADMIN |
| manager | manager123 | MANAGER |
| user | user123 | USER |

## Endpoints

### Auth (Public)
```
POST /api/auth/login      - Login
POST /api/auth/signup     - Register
POST /api/auth/refresh    - Refresh token
```

### Employees (Protected)
```
GET /api/employees              - List (all roles)
GET /api/employees/{id}         - View (all roles)
POST /api/employees             - Create (ADMIN/MANAGER)
PUT /api/employees/{id}         - Update (ADMIN/MANAGER)
DELETE /api/employees/{id}      - Delete (ADMIN only)
```

## Quick Commands

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Use Token
```bash
curl -X GET http://localhost:8080/api/employees \
  -H "Authorization: Bearer <token>"
```

### Signup
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username":"newuser",
    "email":"new@example.com",
    "password":"pass123",
    "confirmPassword":"pass123"
  }'
```

## Configuration
```properties
jwt.secret=mySecretKeyForJwtTokenGenerationWithMinimum32CharactersLongEnough
jwt.expiration=86400000
```

## HTTP Status Codes
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized (no/invalid token)
- 403: Forbidden (insufficient permissions)
- 404: Not Found
- 409: Conflict (duplicate email)
- 500: Server Error

## Permissions
- ADMIN: All operations
- MANAGER: Create, Read, Update
- USER: Read only

## Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

## Documentation
- JWT_SECURITY_GUIDE.md - Full reference
- SECURITY_INTEGRATION_GUIDE.md - Integration guide
- README_SECURITY.md - Project overview

---
Created: February 25, 2026 | Version: 1.0

