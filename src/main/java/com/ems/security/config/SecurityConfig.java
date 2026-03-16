package com.ems.security.config;

import com.ems.security.jwt.JwtAuthenticationFilter;
import com.ems.security.jwt.JwtTokenProvider;
import com.ems.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000", "https://ems-349572548293.europe-west1.run.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        // Use MVC matchers for DispatcherServlet-backed endpoints
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector).servletPath("/");

        http
                // Stateless JWT apps typically disable CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // H2 console uses frames
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                // Stateless session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Enable CORS if your frontend is separate
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                        // H2 console is NOT via DispatcherServlet → use Ant matcher
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()

                        // Auth, public, and docs (MVC handlers)
                        .requestMatchers(new AntPathRequestMatcher(("/api/auth/**"))).permitAll()
                        .requestMatchers(new AntPathRequestMatcher(("/swagger-ui/**"))).permitAll()
                        .requestMatchers(new AntPathRequestMatcher(("/v3/api-docs/**"))).permitAll()
                        .requestMatchers(new AntPathRequestMatcher(("/swagger-ui.html"))).permitAll()
                        .requestMatchers(mvc.pattern("/api/public/**")).permitAll()
                        .requestMatchers(
                                mvc.pattern("/swagger-ui.html"),
                                mvc.pattern("/swagger-ui/**"),
                                mvc.pattern("/v3/api-docs/**")
                        ).permitAll()

                        // Preflight

                        // Employees API - role based
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/employees/**"))
                        .hasAnyRole("ADMIN", "MANAGER", "USER")
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/employees"))
                        .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/employees/**"))
                        .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/employees/**"))
                        .hasRole("ADMIN")

                        // Admin endpoints
                        .requestMatchers(mvc.pattern("/api/admin/**")).hasRole("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                // Plug in DAO auth provider
                .authenticationProvider(authenticationProvider())

                // Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}