package com.prueba.risk_central_mock_service.service;

import com.prueba.risk_central_mock_service.dto.RiskEvaluationRequest;
import com.prueba.risk_central_mock_service.dto.RiskEvaluationResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskEvaluationService {

    private final MeterRegistry meterRegistry;

    public RiskEvaluationResponse evaluateRisk(RiskEvaluationRequest request) {
        log.info("Evaluating risk for document: {}", request.getDocument());
        
        // Use document hash as seed for consistent results
        int seed = request.getDocument().hashCode();
        Random random = new Random(seed);
        
        // Generate score between 300 and 850 (like FICO score)
        int score = 300 + random.nextInt(551);
        
        // Determine risk level and recommendation
        String riskLevel = determineRiskLevel(score);
        String recommendation = determineRecommendation(score);
        String message = generateMessage(score, riskLevel);
        
        // Increment metrics
        Counter.builder("risk_evaluations_total")
            .tag("risk_level", riskLevel)
            .tag("recommendation", recommendation)
            .register(meterRegistry)
            .increment();
        
        log.info("Risk evaluation completed - Document: {}, Score: {}, Risk: {}", 
            request.getDocument(), score, riskLevel);
        
        return RiskEvaluationResponse.builder()
            .document(request.getDocument())
            .score(score)
            .riskLevel(riskLevel)
            .recommendation(recommendation)
            .message(message)
            .evaluationDate(LocalDateTime.now())
            .build();
    }

    private String determineRiskLevel(int score) {
        if (score >= 700) {
            return "LOW";
        } else if (score >= 550) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    private String determineRecommendation(int score) {
        if (score >= 700) {
            return "APPROVED";
        } else if (score >= 550) {
            return "REVIEW";
        } else {
            return "REJECTED";
        }
    }

    private String generateMessage(int score, String riskLevel) {
        return switch (riskLevel) {
            case "LOW" -> String.format("Excellent credit profile (Score: %d). Low risk applicant.", score);
            case "MEDIUM" -> String.format("Acceptable credit profile (Score: %d). Manual review recommended.", score);
            case "HIGH" -> String.format("Poor credit profile (Score: %d). High risk applicant.", score);
            default -> "Unable to determine risk level";
        };
    }
}