package com.prueba.credit_application_service.domain.port.out;

import com.prueba.credit_application_service.domain.model.RiskEvaluation;

/**
 * RiskEvaluationPort - OUTPUT PORT (DOMAIN INTERFACE)
 * 
 * This is a PURE domain interface that defines the contract
 * for evaluating risk with an external service.
 * 
 * The implementation will be in the INFRASTRUCTURE layer.
 */
public interface RiskEvaluationPort {
    
    /**
     * Evaluates the credit risk for an applicant
     * 
     * @param document Applicant's document ID
     * @param fullName Applicant's full name
     * @param requestedAmount Amount requested for credit
     * @param monthlyIncome Applicant's monthly income
     * @return RiskEvaluation domain object with the assessment
     */
    RiskEvaluation evaluateRisk(
        String document, 
        String fullName,
        Double requestedAmount, 
        Double monthlyIncome
    );
}
