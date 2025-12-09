package com.prueba.risk_central_mock_service.controller;

import com.prueba.risk_central_mock_service.dto.RiskEvaluationRequest;
import com.prueba.risk_central_mock_service.dto.RiskEvaluationResponse;
import com.prueba.risk_central_mock_service.service.RiskEvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/risk-evaluation")
@RequiredArgsConstructor
@Slf4j
public class RiskEvaluationController {

    private final RiskEvaluationService riskEvaluationService;

    @PostMapping
    public ResponseEntity<RiskEvaluationResponse> evaluate(
            @Valid @RequestBody RiskEvaluationRequest request) {
        
        log.info("Solicitud de evaluación de riesgo recibida para documento: {}", request.getDocumento());
        
        RiskEvaluationResponse response = riskEvaluationService.evaluateRisk(request);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Risk Central Mock Service is running");
    }
}