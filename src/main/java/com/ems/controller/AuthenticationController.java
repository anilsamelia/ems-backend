package com.ems.controller;

import com.ems.dto.LoginRequest;
import com.ems.dto.LoginResponse;
import com.ems.dto.SignUpRequest;
import com.ems.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication", description = "Authentication endpoints for user login, signup, and token refresh")
public class AuthenticationController {

    private AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
public ResponseEntity<LoginResponse> getTest() {
        LoginRequest request = new LoginRequest();
        request.setPassword("welcome");
        request.setUsername("user");
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Login user and return JWT token
     * POST /api/auth/login
     */
    @Operation(summary = "User Login", description = "Authenticate user with username/email and password to receive JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request for user: {}", loginRequest.getUsername());
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Register new user
     * POST /api/auth/signup
     */
    @Operation(summary = "User Registration", description = "Register a new user account and receive JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info("Signup request for user: {}", signUpRequest.getUsername());
        LoginResponse response = authService.signup(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Refresh JWT token
     * POST /api/auth/refresh
     */
    @Operation(summary = "Refresh Token", description = "Refresh an existing JWT token to extend its validity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestHeader("Authorization") String token) {
        log.info("Token refresh request");
        LoginResponse response = authService.refreshToken(token);
        return ResponseEntity.ok(response);
    }
}

