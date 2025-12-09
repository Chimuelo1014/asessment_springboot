package com.prueba.credit_application_service.infrastructure.adapter.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RiskEvaluationExternalRequest - DTO for external risk service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationExternalRequest {
    private String document;
    private String fullName;
    private Double requestedAmount;
    private Double monthlyIncome;
}
