package com.prueba.credit_application_service.infrastructure.adapter.in.web.controller;

import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.AuthenticationResponse;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.auth.AuthenticationService;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.LoginRequest;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController - Handles authentication endpoints
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());
        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest request) {
        log.info("User login attempt: {}", request.getUsername());
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
