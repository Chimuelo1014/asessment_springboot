package com.prueba.credit_application_service.infrastructure.adapter.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * External Risk Evaluation Response DTO
 * 
 * Data Transfer Object for receiving risk evaluation results from external service.
 * All fields in English to match the corrected risk service API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationExternalResponse {
    
    /**
     * Applicant's identification document number
     */
    private String document;
    
    /**
     * Credit score (300-950)
     */
    private Integer score;
    
    /**
     * Risk level: LOW, MEDIUM, or HIGH
     */
    private String riskLevel;
    
    /**
     * Detailed explanation of the risk assessment
     */
    private String detail;
}