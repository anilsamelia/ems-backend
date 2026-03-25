package com.ems.service;

import com.ems.dto.LoginRequest;
import com.ems.dto.LoginResponse;
import com.ems.dto.SignUpRequest;
import com.ems.entity.Role;
import com.ems.entity.User;
import com.ems.exception.DuplicateEmailException;
import com.ems.repository.RoleRepository;
import com.ems.repository.UserRepository;
import com.ems.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                      RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                      JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Login user with username and password
     */
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Authenticating user: {}", loginRequest.getUsername());
        System.out.println("Authenticating user: " + loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        String token = tokenProvider.generateToken(authentication);
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Set<String> roles = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());

        log.info("User authenticated successfully: {}", loginRequest.getUsername());

        return new LoginResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            token,
            "Bearer",
            roles
        );
    }

    /**
     * Register new user
     */
    public LoginResponse signup(SignUpRequest signUpRequest) {
        log.info("Registering new user: {}", signUpRequest.getUsername());

        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicateEmailException(signUpRequest.getEmail());
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setIsActive(true);

        // Assign default USER role
        Role userRole = roleRepository.findByName("USER")
            .orElseGet(() -> roleRepository.save(Role.builder()
                .name("USER")
                .description("User role")
                .build()));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        log.info("User registered successfully: {}", signUpRequest.getUsername());

        // Generate token
        String token = tokenProvider.generateTokenFromUsername(savedUser.getUsername());

        Set<String> roleNames = savedUser.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());

        return new LoginResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            token,
            "Bearer",
            roleNames
        );
    }

    /**
     * Refresh JWT token
     */
    public LoginResponse refreshToken(String token) {
        log.info("Refreshing token");

        String bearerToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        if (!tokenProvider.validateToken(bearerToken)) {
            throw new RuntimeException("Invalid or expired token");
        }

        String username = tokenProvider.getUsernameFromToken(bearerToken);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String newToken = tokenProvider.generateTokenFromUsername(username);

        Set<String> roles = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());

        log.info("Token refreshed for user: {}", username);

        return new LoginResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            newToken,
            "Bearer",
            roles
        );
    }
}

