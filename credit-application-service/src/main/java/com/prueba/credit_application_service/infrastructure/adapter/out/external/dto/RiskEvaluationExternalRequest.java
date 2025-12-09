package com.prueba.credit_application_service.infrastructure.adapter.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * External Risk Evaluation Request DTO
 * 
 * Data Transfer Object for communicating with the external risk service.
 * All fields in English to match the corrected risk service API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationExternalRequest {
    
    /**
     * Applicant's identification document number
     */
    private String document;
    
    /**
     * Requested credit amount
     */
    private Double amount;
    
    /**
     * Credit term in months
     */
    private Integer term;
}