package com.prueba.risk_central_mock_service.controller;

import com.prueba.risk_central_mock_service.dto.RiskEvaluationRequest;
import com.prueba.risk_central_mock_service.dto.RiskEvaluationResponse;
import com.prueba.risk_central_mock_service.service.RiskEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Risk Evaluation REST Controller
 * 
 * Provides endpoints for credit risk evaluation services.
 * This is a mock service that simulates an external credit bureau.
 */
@RestController
@RequestMapping("/api/v1/risk-evaluation")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Risk Evaluation", description = "Credit risk evaluation endpoints")
public class RiskEvaluationController {

    private final RiskEvaluationService riskEvaluationService;

    /**
     * Evaluates credit risk for an applicant
     * 
     * @param request Risk evaluation request with document, amount, and term
     * @return Risk evaluation response with score and risk level
     */
    @PostMapping
    @Operation(
        summary = "Evaluate credit risk",
        description = "Evaluates credit risk based on applicant's document and loan details. " +
                     "Returns a deterministic score (same document = same score)."
    )
    public ResponseEntity<RiskEvaluationResponse> evaluate(
            @Valid @RequestBody RiskEvaluationRequest request) {
        
        log.info("Risk evaluation request received for document: {}", request.getDocument());
        
        RiskEvaluationResponse response = riskEvaluationService.evaluateRisk(request);
        
        log.debug("Risk evaluation completed: score={}, level={}", 
                 response.getScore(), response.getRiskLevel());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint for the risk evaluation service
     * 
     * @return Service status message
     */
    @GetMapping("/health")
    @Operation(
        summary = "Health check",
        description = "Verifies that the risk evaluation service is running"
    )
    public ResponseEntity<String> health() {
        log.debug("Health check endpoint called");
        return ResponseEntity.ok("Risk Central Mock Service is running");
    }
}