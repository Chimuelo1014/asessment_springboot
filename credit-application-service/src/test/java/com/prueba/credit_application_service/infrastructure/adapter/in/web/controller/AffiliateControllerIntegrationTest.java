package com.prueba.credit_application_service.infrastructure.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.AffiliateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AffiliateControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("test_db")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("external.risk-service.url", () -> "http://localhost:8081");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ANALISTA")
    void shouldCreateAffiliateSuccessfully() throws Exception {
        // Given
        AffiliateRequest request = new AffiliateRequest();
        request.setDocument("987654321");
        request.setFullName("Jane Doe");
        request.setEmail("jane@example.com");
        request.setPhone("555-5678");
        request.setMonthlySalary(4000.0);

        // When & Then
        mockMvc.perform(post("/api/v1/affiliates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.document").value("987654321"))
            .andExpect(jsonPath("$.fullName").value("Jane Doe"))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "ANALISTA")
    void shouldReturnConflictForDuplicateDocument() throws Exception {
        // Given - First create an affiliate
        AffiliateRequest request = new AffiliateRequest();
        request.setDocument("111222333");
        request.setFullName("John Smith");
        request.setEmail("john.smith@example.com");
        request.setPhone("555-0000");
        request.setMonthlySalary(5000.0);

        mockMvc.perform(post("/api/v1/affiliates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        // When & Then - Try to create again with same document
        mockMvc.perform(post("/api/v1/affiliates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.title").value("Conflict Detected"));
    }

    @Test
    @WithMockUser(roles = "AFILIADO")
    void shouldDenyAccessForAffiliadoRole() throws Exception {
        // Given
        AffiliateRequest request = new AffiliateRequest();
        request.setDocument("444555666");
        request.setFullName("Test User");
        request.setEmail("test@example.com");
        request.setMonthlySalary(3000.0);

        // When & Then
        mockMvc.perform(post("/api/v1/affiliates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }
}