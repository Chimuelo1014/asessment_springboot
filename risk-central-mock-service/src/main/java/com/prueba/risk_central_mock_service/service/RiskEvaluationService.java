package com.prueba.risk_central_mock_service.service;

import com.prueba.risk_central_mock_service.dto.RiskEvaluationRequest;
import com.prueba.risk_central_mock_service.dto.RiskEvaluationResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Risk Evaluation Service
 * 
 * Provides deterministic risk assessment based on applicant's document number.
 * The same document always returns the same score (using document hash as seed).
 * 
 * Score ranges:
 * - 300-500: HIGH RISK
 * - 501-700: MEDIUM RISK
 * - 701-950: LOW RISK
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RiskEvaluationService {

    private final MeterRegistry meterRegistry;

    /**
     * Evaluates credit risk for a given applicant
     * 
     * @param request Risk evaluation request containing document, amount, and term
     * @return Risk evaluation response with score, level, and details
     */
    public RiskEvaluationResponse evaluateRisk(RiskEvaluationRequest request) {
        log.info("Evaluating risk for document: {}", request.getDocument());
        
        // Use document hash as seed for consistency
        int seed = request.getDocument().hashCode();
        Random random = new Random(seed);
        
        // Generate score between 300 and 950 (651 possible values)
        int score = 300 + random.nextInt(651);
        
        // Determine risk level based on score
        String riskLevel = determineRiskLevel(score);
        String detail = generateDetailMessage(score, riskLevel);
        
        // Register metric
        Counter.builder("risk_evaluations_total")
            .tag("risk_level", riskLevel)
            .register(meterRegistry)
            .increment();
        
        log.info("Evaluation completed - Document: {}, Score: {}, Level: {}", 
            request.getDocument(), score, riskLevel);
        
        return RiskEvaluationResponse.builder()
            .document(request.getDocument())
            .score(score)
            .riskLevel(riskLevel)
            .detail(detail)
            .build();
    }

    /**
     * Determines risk level based on credit score
     * 
     * @param score Credit score (300-950)
     * @return Risk level: LOW, MEDIUM, or HIGH
     */
    private String determineRiskLevel(int score) {
        if (score >= 701) {
            return "LOW";
        } else if (score >= 501) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    /**
     * Generates detailed message explaining the risk assessment
     * 
     * @param score Credit score
     * @param riskLevel Risk level
     * @return Detailed explanation message
     */
    private String generateDetailMessage(int score, String riskLevel) {
        return switch (riskLevel) {
            case "LOW" -> String.format(
                "Excellent credit history (Score: %d). Reliable customer with low default risk.", 
                score
            );
            case "MEDIUM" -> String.format(
                "Moderate credit history (Score: %d). Requires additional analysis before approval.", 
                score
            );
            case "HIGH" -> String.format(
                "Poor credit history (Score: %d). High default risk, rejection recommended.", 
                score
            );
            default -> "Unable to determine risk level";
        };
    }
}