package com.prueba.credit_application_service.infrastructure.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.enums.AffiliateStatus;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.CreditApplicationRequest;
import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CreditApplicationControllerIntegrationTest {

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

    @Autowired
    private AffiliateRepositoryPort affiliateRepository;

    private Long affiliateId;

    @BeforeEach
    void setUp() {
        // Create an affiliate for testing
        Affiliate affiliate = new Affiliate();
        affiliate.setDocument("TEST-" + System.nanoTime());
        affiliate.setFullName("Test Affiliate");
        affiliate.setEmail("test.affiliate@example.com");
        affiliate.setPhone("555-9999");
        affiliate.setMonthlySalary(6000.0);
        affiliate.setStatus(AffiliateStatus.ACTIVE);
        affiliate.setAffiliationDate(LocalDate.now().minusYears(1));
        
        Affiliate saved = affiliateRepository.save(affiliate);
        affiliateId = saved.getId();
    }

    @Test
    @WithMockUser(roles = "AFILIADO")
    void shouldCreateCreditApplicationSuccessfully() throws Exception {
        // Given
        CreditApplicationRequest request = new CreditApplicationRequest();
        request.setAffiliateId(affiliateId);
        request.setRequestedAmount(15000.0);
        request.setTermMonths(24);

        // When & Then
        mockMvc.perform(post("/api/v1/credit-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.affiliateId").value(affiliateId))
            .andExpect(jsonPath("$.requestedAmount").value(15000.0))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @WithMockUser(roles = "ANALISTA")
    void shouldGetAllApplications() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/credit-applications"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "ANALISTA")
    void shouldGetPendingApplications() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/credit-applications/pending"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}