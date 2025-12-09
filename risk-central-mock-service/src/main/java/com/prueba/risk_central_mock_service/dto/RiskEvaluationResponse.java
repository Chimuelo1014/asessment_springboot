package com.prueba.risk_central_mock_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Risk Evaluation Response DTO
 * Contains the risk assessment results for a credit application
 * 
 * Format: { "document", "score", "riskLevel", "detail" }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationResponse {
    
    /**
     * Applicant's identification document number
     */
    private String document;
    
    /**
     * Credit score (300-950)
     * Higher scores indicate lower risk
     */
    private Integer score;
    
    /**
     * Risk level classification
     * Possible values: LOW, MEDIUM, HIGH
     */
    private String riskLevel;
    
    /**
     * Detailed explanation of the risk assessment
     */
    private String detail;
}