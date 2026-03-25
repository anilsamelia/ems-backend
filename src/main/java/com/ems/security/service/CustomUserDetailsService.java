package com.ems.security.service;

import com.ems.entity.User;
import com.ems.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!user.getIsActive()) {
            throw new UsernameNotFoundException("User account is inactive: " + username);
        }

        return new CustomUserDetails(user);
    }

    /**
     * Load user by email
     */
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!user.getIsActive()) {
            throw new UsernameNotFoundException("User account is inactive: " + email);
        }

        return new CustomUserDetails(user);
    }
}

