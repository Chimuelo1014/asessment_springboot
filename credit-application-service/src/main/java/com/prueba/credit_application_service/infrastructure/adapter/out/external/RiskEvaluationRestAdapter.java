package com.prueba.credit_application_service.infrastructure.adapter.out.external;

import com.prueba.credit_application_service.domain.model.RiskEvaluation;
import com.prueba.credit_application_service.domain.model.enums.RiskLevel;
import com.prueba.credit_application_service.domain.port.out.RiskEvaluationPort;
import com.prueba.credit_application_service.infrastructure.adapter.out.external.dto.RiskEvaluationExternalRequest;
import com.prueba.credit_application_service.infrastructure.adapter.out.external.dto.RiskEvaluationExternalResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

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
    @CircuitBreaker(name = "riskService", fallbackMethod = "fallbackEvaluateRisk")
    @Retry(name = "riskService")
    public RiskEvaluation evaluateRisk(String document, String fullName,
            Double requestedAmount, Double monthlyIncome) {
        log.info("Calling external risk service for document: {}", document);

        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            RiskEvaluationExternalRequest request = new RiskEvaluationExternalRequest();
            request.setDocument(document);
            request.setAmount(requestedAmount);
            request.setTerm(12);

            String url = riskServiceUrl + "/api/v1/risk-evaluation";

            RiskEvaluationExternalResponse response = restTemplate.postForObject(
                    url, request, RiskEvaluationExternalResponse.class);

            if (response == null) {
                throw new RuntimeException("Risk service returned null response");
            }

            Counter.builder("risk_evaluation_calls_total")
                    .tag("status", "success")
                    .register(meterRegistry)
                    .increment();

            sample.stop(Timer.builder("risk_evaluation_duration")
                    .tag("status", "success")
                    .register(meterRegistry));

            log.info("Risk evaluation successful - Document: {}, Score: {}",
                    document, response.getScore());

            return mapToDomain(response);

        } catch (Exception e) {
            log.error("Error calling risk service for document: {}", document, e);

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

    private RiskEvaluation fallbackEvaluateRisk(String document, String fullName,
            Double requestedAmount, Double monthlyIncome, Exception ex) {
        log.error("Circuit breaker fallback activated for document: {}. Error: {}", document, ex.getMessage());

        RiskEvaluation riskEvaluation = new RiskEvaluation();
        riskEvaluation.setScore(500);
        riskEvaluation.setRiskLevel(RiskLevel.MEDIUM);
        riskEvaluation.setRecommendation("REVIEW");
        riskEvaluation.setEvaluationMessage("Risk service unavailable. Default medium risk assigned.");
        riskEvaluation.setEvaluationDate(LocalDateTime.now());

        return riskEvaluation;
    }

    private RiskEvaluation mapToDomain(RiskEvaluationExternalResponse response) {
        RiskEvaluation riskEvaluation = new RiskEvaluation();
        riskEvaluation.setScore(response.getScore());

        RiskLevel riskLevel = switch (response.getRiskLevel()) {
            case "LOW" -> RiskLevel.LOW;
            case "MEDIUM" -> RiskLevel.MEDIUM;
            case "HIGH" -> RiskLevel.HIGH;
            default -> RiskLevel.MEDIUM;
        };
        riskEvaluation.setRiskLevel(riskLevel);

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