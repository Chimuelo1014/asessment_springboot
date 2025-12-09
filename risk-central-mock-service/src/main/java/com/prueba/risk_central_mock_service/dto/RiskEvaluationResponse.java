package com.prueba.risk_central_mock_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationResponse {
    private String document;
    private Integer score; // 300-850
    private String riskLevel; // LOW, MEDIUM, HIGH
    private String recommendation; // APPROVED, REVIEW, REJECTED
    private String message;
    private LocalDateTime evaluationDate;
}