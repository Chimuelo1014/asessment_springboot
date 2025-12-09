package com.prueba.credit_application_service.infrastructure.adapter.out.external;

import com.prueba.credit_application_service.domain.model.RiskEvaluation;
import com.prueba.credit_application_service.infrastructure.adapter.out.external.dto.RiskEvaluationExternalResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskEvaluationRestAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private MeterRegistry meterRegistry;
    private RiskEvaluationRestAdapter adapter;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        adapter = new RiskEvaluationRestAdapter(
                restTemplate,
                meterRegistry,
                "http://localhost:8081");
    }

    @Test
    void shouldCallExternalServiceSuccessfully() {
        // Given
        RiskEvaluationExternalResponse mockResponse = new RiskEvaluationExternalResponse();
        mockResponse.setDocument("123456789");
        mockResponse.setScore(750);
        mockResponse.setRiskLevel("LOW");
        mockResponse.setDetail("Test evaluation");

        when(restTemplate.postForObject(anyString(), any(), eq(RiskEvaluationExternalResponse.class)))
                .thenReturn(mockResponse);

        // When
        RiskEvaluation result = adapter.evaluateRisk("123456789", "John Doe", 10000.0, 5000.0);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getScore()).isEqualTo(750);
        verify(restTemplate).postForObject(anyString(), any(), eq(RiskEvaluationExternalResponse.class));
    }

    @Test
    void shouldHandleExternalServiceFailure() {
        // Given
        when(restTemplate.postForObject(anyString(), any(), eq(RiskEvaluationExternalResponse.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        // When & Then
        assertThatThrownBy(() -> adapter.evaluateRisk("123456789", "John Doe", 10000.0, 5000.0))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to evaluate risk");
    }

    @Test
    void shouldMapLowRiskCorrectly() {
        // Given
        RiskEvaluationExternalResponse mockResponse = new RiskEvaluationExternalResponse();
        mockResponse.setDocument("123456789");
        mockResponse.setScore(800);
        mockResponse.setRiskLevel("LOW");
        mockResponse.setDetail("Low risk");

        when(restTemplate.postForObject(anyString(), any(), eq(RiskEvaluationExternalResponse.class)))
                .thenReturn(mockResponse);

        // When
        RiskEvaluation result = adapter.evaluateRisk("123456789", "John Doe", 10000.0, 5000.0);

        // Then
        assertThat(result.getRecommendation()).isEqualTo("APPROVED");
    }
}
