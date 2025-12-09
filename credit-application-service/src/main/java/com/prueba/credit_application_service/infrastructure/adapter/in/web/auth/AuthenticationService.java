package com.prueba.credit_application_service.infrastructure.adapter.in.web.auth;

import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import com.prueba.credit_application_service.infrastructure.config.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AuthenticationService - Handles registration and login
 */
@Service
@RequiredArgsConstructor
class AuthenticationService {
    
    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AuthenticationResponse register(RegisterRequest request) {
        // Validate username not exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Create user
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        // Generate token
        UserDetails userDetails = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole())))
                .build();
        
        String jwtToken = jwtService.generateToken(userDetails);
        
        return new AuthenticationResponse(jwtToken, user.getUsername(), user.getRole());
    }
    
    public AuthenticationResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        
        UserDetails userDetails = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole())))
                .build();
        
        String jwtToken = jwtService.generateToken(userDetails);
        
        return new AuthenticationResponse(jwtToken, user.getUsername(), user.getRole());
    }
}
