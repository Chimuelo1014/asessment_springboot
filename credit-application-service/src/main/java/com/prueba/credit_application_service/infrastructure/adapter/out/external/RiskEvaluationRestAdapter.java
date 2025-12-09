package com.prueba.credit_application_service.infrastructure.adapter.out.external;

import com.prueba.credit_application_service.domain.model.RiskEvaluation;
import com.prueba.credit_application_service.domain.model.enums.RiskLevel;
import com.prueba.credit_application_service.domain.port.out.RiskEvaluationPort;
import com.prueba.credit_application_service.infrastructure.adapter.out.external.dto.RiskEvaluationExternalRequest;
import com.prueba.credit_application_service.infrastructure.adapter.out.external.dto.RiskEvaluationExternalResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

/**
 * Risk Evaluation REST Adapter
 * 
 * Output adapter that communicates with the external risk evaluation service.
 * Implements the RiskEvaluationPort interface from the domain layer.
 */
@Component
public class RiskEvaluationRestAdapter implements RiskEvaluationPort {

    private static final Logger log = LoggerFactory.getLogger(RiskEvaluationRestAdapter.class);

    private final RestTemplate restTemplate;
    private final MeterRegistry meterRegistry;
    private final String riskServiceUrl;

    public RiskEvaluationRestAdapter(
            RestTemplate restTemplate,
            MeterRegistry meterRegistry,
            @Value("${external.risk-service.url}") String riskServiceUrl) {
        this.restTemplate = restTemplate;
        this.meterRegistry = meterRegistry;
        this.riskServiceUrl = riskServiceUrl;
    }

    @Override
    public RiskEvaluation evaluateRisk(String document, String fullName,
            Double requestedAmount, Double monthlyIncome) {
        log.info("Calling external risk service for document: {}", document);

        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            // Build request DTO with English field names
            RiskEvaluationExternalRequest request = new RiskEvaluationExternalRequest();
            request.setDocument(document);
            request.setAmount(requestedAmount);
            request.setTerm(12); // Default term, could be parameterized

            String url = riskServiceUrl + "/api/v1/risk-evaluation";

            // Call external service
            RiskEvaluationExternalResponse response = restTemplate.postForObject(
                    url, request, RiskEvaluationExternalResponse.class);

            if (response == null) {
                throw new RuntimeException("Risk service returned null response");
            }

            // Record success metric
            Counter.builder("risk_evaluation_calls_total")
                    .tag("status", "success")
                    .register(meterRegistry)
                    .increment();

            sample.stop(Timer.builder("risk_evaluation_duration")
                    .tag("status", "success")
                    .register(meterRegistry));

            log.info("Risk evaluation successful - Document: {}, Score: {}",
                    document, response.getScore());

            // Convert external DTO to domain object
            return mapToDomain(response);

        } catch (Exception e) {
            log.error("Error calling risk service for document: {}", document, e);

            // Record failure metric
            Counter.builder("risk_evaluation_calls_total")
                    .tag("status", "failure")
                    .register(meterRegistry)
                    .increment();

            sample.stop(Timer.builder("risk_evaluation_duration")
                    .tag("status", "failure")
                    .register(meterRegistry));

            throw new RuntimeException("Failed to evaluate risk: " + e.getMessage(), e);
        }
    }

    /**
     * Maps external service response to domain model
     * 
     * @param response External service response
     * @return Domain RiskEvaluation object
     */
    private RiskEvaluation mapToDomain(RiskEvaluationExternalResponse response) {
        RiskEvaluation riskEvaluation = new RiskEvaluation();
        riskEvaluation.setScore(response.getScore());
        
        // Map risk level (English enum mapping)
        RiskLevel riskLevel = switch (response.getRiskLevel()) {
            case "LOW" -> RiskLevel.LOW;
            case "MEDIUM" -> RiskLevel.MEDIUM;
            case "HIGH" -> RiskLevel.HIGH;
            default -> RiskLevel.MEDIUM; // Default fallback
        };
        riskEvaluation.setRiskLevel(riskLevel);
        
        // Determine recommendation based on level
        String recommendation = switch (riskLevel) {
            case LOW -> "APPROVED";
            case MEDIUM -> "REVIEW";
            case HIGH -> "REJECTED";
        };
        riskEvaluation.setRecommendation(recommendation);
        
        riskEvaluation.setEvaluationMessage(response.getDetail());
        riskEvaluation.setEvaluationDate(LocalDateTime.now());
        
        return riskEvaluation;
    }
}