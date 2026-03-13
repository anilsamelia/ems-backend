## Spring Security JWT - Integration & Usage Guide

### How to Build and Run

```bash
# Navigate to project directory
cd C:\java_workspace\ems

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Application will start on http://localhost:8080
```

### How to Test the API

#### Option 1: Using cURL

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.token')

# 2. Use token in requests
curl -X GET http://localhost:8080/api/employees \
  -H "Authorization: Bearer $TOKEN"

# 3. Signup new user
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username":"testuser",
    "email":"test@example.com",
    "password":"password123",
    "confirmPassword":"password123"
  }'
```

#### Option 2: Using Postman

1. Import the Postman collection: `EMS_API_Postman_Collection.json`
2. Set base URL: `http://localhost:8080`
3. Login endpoint returns token automatically
4. Use token in subsequent requests

#### Option 3: Using VS Code REST Client

Create file `test-auth.rest`:
```
### Login
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

### Get Employees
GET http://localhost:8080/api/employees
Authorization: Bearer <paste-token-here>

### Create Employee
POST http://localhost:8080/api/employees
Authorization: Bearer <paste-token-here>
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "dept": "IT",
  "designation": "Developer",
  "salary": 70000,
  "supervisor": "John Smith",
  "address": "123 Main St",
  "doj": "2024-01-01"
}
```

### Common Issues & Solutions

**Issue: "401 Unauthorized"**
- Solution: Include Authorization header with Bearer token
- Format: `Authorization: Bearer <token>`

**Issue: "403 Forbidden"**
- Solution: User role doesn't have permission for this endpoint
- Check user role and required permissions

**Issue: "Invalid JWT"**
- Solution: Token may be expired or invalid
- Get a new token by logging in again

**Issue: "Password mismatch"**
- Solution: Password and confirmPassword fields must match during signup

**Issue: "Email already exists"**
- Solution: Use a different email address for signup

### Files Overview

All security-related code is in:
- `src/main/java/com/ems/security/` - Security configuration and JWT handling
- `src/main/java/com/ems/entity/User.java` - User entity
- `src/main/java/com/ems/entity/Role.java` - Role entity
- `src/main/java/com/ems/repository/UserRepository.java` - User repository
- `src/main/java/com/ems/repository/RoleRepository.java` - Role repository
- `src/main/java/com/ems/service/AuthService.java` - Auth service
- `src/main/java/com/ems/controller/AuthenticationController.java` - Auth controller
- `src/main/java/com/ems/dto/LoginRequest.java` - Login request DTO
- `src/main/java/com/ems/dto/LoginResponse.java` - Login response DTO
- `src/main/java/com/ems/dto/SignUpRequest.java` - Signup request DTO

### Configuration Files

- `application.properties` - JWT secret and expiration
- `data.sql` - Initial roles and test users

### Security Best Practices Implemented

✅ BCrypt password hashing
✅ JWT token authentication
✅ Role-based authorization
✅ Stateless API design
✅ Token validation on every request
✅ Configurable token expiration
✅ Input validation
✅ Error handling with proper HTTP status codes
✅ CORS configuration (disabled by default)
✅ CSRF protection (disabled for stateless API)

### Ready for Production

The implementation includes:
- Comprehensive error handling
- Security annotations
- Input validation
- Proper logging
- Configuration management
- Test data for validation

Change only these before production:
1. `jwt.secret` - Use a strong random key
2. Database - Replace H2 with production database
3. CORS settings - Configure allowed origins
4. Logging levels - Adjust for production

