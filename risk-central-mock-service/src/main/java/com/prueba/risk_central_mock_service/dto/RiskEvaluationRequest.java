package com.prueba.risk_central_mock_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Risk Evaluation Request DTO
 * Contains the data needed to evaluate credit risk for a potential borrower
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationRequest {
    
    /**
     * Applicant's identification document number
     */
    @NotBlank(message = "Document is required")
    private String document;
    
    /**
     * Requested credit amount
     */
    @Positive(message = "Amount must be positive")
    private Double amount;
    
    /**
     * Credit term in months
     */
    @Positive(message = "Term must be positive")
    private Integer term;
}