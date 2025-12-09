package com.prueba.risk_central_mock_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.risk_central_mock_service.dto.RiskEvaluationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for RiskEvaluationController
 */
@SpringBootTest
@AutoConfigureMockMvc
class RiskEvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldEvaluateRiskSuccessfully() throws Exception {
        // Given
        RiskEvaluationRequest request = new RiskEvaluationRequest();
        request.setDocument("1017654311");
        request.setAmount(5000000.0);
        request.setTerm(24);

        // When & Then
        mockMvc.perform(post("/api/v1/risk-evaluation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.document").value("1017654311"))
                .andExpect(jsonPath("$.score").isNumber())
                .andExpect(jsonPath("$.score").value(org.hamcrest.Matchers.allOf(
                        org.hamcrest.Matchers.greaterThanOrEqualTo(300),
                        org.hamcrest.Matchers.lessThanOrEqualTo(950))))
                .andExpect(jsonPath("$.riskLevel").exists())
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void shouldReturnConsistentScoreForSameDocument() throws Exception {
        // Given
        RiskEvaluationRequest request = new RiskEvaluationRequest();
        request.setDocument("TEST123");
        request.setAmount(3000000.0);
        request.setTerm(36);

        // When - Call twice
        String response1 = mockMvc.perform(post("/api/v1/risk-evaluation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String response2 = mockMvc.perform(post("/api/v1/risk-evaluation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Then - Responses should be identical
        org.assertj.core.api.Assertions.assertThat(response1).isEqualTo(response2);
    }

    @Test
    void shouldReturnBadRequestForInvalidRequest() throws Exception {
        // Given - Empty request
        String invalidRequest = "{}";

        // When & Then
        mockMvc.perform(post("/api/v1/risk-evaluation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnHealthCheckSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/risk-evaluation/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Risk Central Mock Service"));
    }

    @Test
    void shouldValidateRequiredFields() throws Exception {
        // Given - Request missing document
        RiskEvaluationRequest request = new RiskEvaluationRequest();
        request.setAmount(1000000.0);
        request.setTerm(12);
        // document is null

        // When & Then
        mockMvc.perform(post("/api/v1/risk-evaluation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleDifferentAmountsAndTerms() throws Exception {
        // Test with different amounts and terms
        RiskEvaluationRequest request1 = new RiskEvaluationRequest();
        request1.setDocument("DOC1");
        request1.setAmount(1000000.0);
        request1.setTerm(12);

        RiskEvaluationRequest request2 = new RiskEvaluationRequest();
        request2.setDocument("DOC1"); // Same document
        request2.setAmount(10000000.0); // Different amount
        request2.setTerm(60); // Different term

        // Both should return same score (deterministic by document)
        String response1 = mockMvc.perform(post("/api/v1/risk-evaluation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String response2 = mockMvc.perform(post("/api/v1/risk-evaluation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Parse and compare scores
        var mapper = new ObjectMapper();
        int score1 = mapper.readTree(response1).get("score").asInt();
        int score2 = mapper.readTree(response2).get("score").asInt();

        org.assertj.core.api.Assertions.assertThat(score1).isEqualTo(score2);
    }
}
