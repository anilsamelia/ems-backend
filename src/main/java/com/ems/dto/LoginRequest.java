package com.ems.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Login request containing username/email and password")
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Schema(description = "Username or email address", example = "admin")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "welcome")
    private String password;
}

