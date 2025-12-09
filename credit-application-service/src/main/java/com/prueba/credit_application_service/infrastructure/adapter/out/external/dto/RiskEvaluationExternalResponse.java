package com.prueba.credit_application_service.infrastructure.adapter.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * RiskEvaluationExternalResponse - DTO from external risk service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationExternalResponse {
    private String document;
    private Integer score;
    private String riskLevel;
    private String recommendation;
    private String message;
    private LocalDateTime evaluationDate;
}
