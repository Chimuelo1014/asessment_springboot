package com.prueba.credit_application_service.infrastructure.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for AffiliateController using Testcontainers with
 * PostgreSQL
 * This demonstrates real database integration testing as required by the
 * specification
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AffiliateControllerTestcontainersTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateAffiliateWithTestcontainers() throws Exception {
        // Given - Create request as Map to avoid DTO dependency
        Map<String, Object> request = new HashMap<>();
        request.put("document", "TC-" + System.currentTimeMillis());
        request.put("fullName", "Test Container User");
        request.put("email", "testcontainer@test.com");
        request.put("phone", "3001112222");
        request.put("monthlySalary", 4000000.0);
        request.put("affiliationDate", LocalDate.now().toString());

        // When & Then
        mockMvc.perform(post("/api/v1/affiliates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.document").value(request.get("document")))
                .andExpect(jsonPath("$.fullName").value("Test Container User"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllAffiliatesWithTestcontainers() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/affiliates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAffiliateByIdWithTestcontainers() throws Exception {
        // Given - First create an affiliate
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("document", "TC-GETTEST-" + System.currentTimeMillis());
        createRequest.put("fullName", "Get Test User");
        createRequest.put("email", "gettest@test.com");
        createRequest.put("phone", "3009998888");
        createRequest.put("monthlySalary", 3500000.0);
        createRequest.put("affiliationDate", LocalDate.now().toString());

        String response = mockMvc.perform(post("/api/v1/affiliates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract ID from response
        Map<String, Object> createdAffiliate = objectMapper.readValue(response, Map.class);
        Integer affiliateId = (Integer) createdAffiliate.get("id");

        // When & Then - Get by ID
        mockMvc.perform(get("/api/v1/affiliates/" + affiliateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(affiliateId))
                .andExpect(jsonPath("$.document").value(createRequest.get("document")));
    }
}
