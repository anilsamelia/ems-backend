package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login response containing JWT token and user information")
public class LoginResponse {
    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "Username", example = "admin")
    private String username;

    @Schema(description = "User email", example = "admin@example.com")
    private String email;

    @Schema(description = "JWT authentication token")
    private String token;

    @Schema(description = "Token type", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "User roles", example = "[\"ADMIN\"]")
    private Set<String> roles;
}

