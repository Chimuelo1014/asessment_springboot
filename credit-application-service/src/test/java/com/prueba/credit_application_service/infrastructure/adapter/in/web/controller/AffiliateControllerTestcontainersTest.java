package com.prueba.credit_application_service.infrastructure.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.request.CreateAffiliateRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for AffiliateController using Testcontainers
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
        // Given
        CreateAffiliateRequest request = new CreateAffiliateRequest();
        request.setDocument("TC-" + System.currentTimeMillis());
        request.setFullName("Test Container User");
        request.setEmail("testcontainer@test.com");
        request.setPhone("3001112222");
        request.setMonthlySalary(4000000.0);
        request.setAffiliationDate(LocalDate.now());

        // When & Then
        mockMvc.perform(post("/api/v1/affiliates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.document").value(request.getDocument()))
                .andExpect(jsonPath("$.fullName").value("Test Container User"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllAffiliatesWithTestcontainers() throws Exception {
        mockMvc.perform(get("/api/v1/affiliates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
