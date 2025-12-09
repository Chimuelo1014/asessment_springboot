package com.prueba.credit_application_service.infrastructure.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SecurityIntegrationTest {

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
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldDenyAccessWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/affiliates"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AFILIADO")
    void shouldDenyAffiliateAccessToAnalystEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/credit-applications"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ANALISTA")
    void shouldAllowAnalystAccessToApplications() throws Exception {
        mockMvc.perform(get("/api/v1/credit-applications"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldGenerateValidJwtToken() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
            .username("admin")
            .password("password")
            .build();

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.username").value("admin"))
            .andReturn();

        String token = objectMapper.readTree(result.getResponse().getContentAsString())
            .get("token").asText();
        
        assertThat(token).isNotBlank();
    }
}
