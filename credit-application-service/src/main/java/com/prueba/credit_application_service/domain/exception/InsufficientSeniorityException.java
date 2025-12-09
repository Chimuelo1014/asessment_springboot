package com.prueba.credit_application_service.domain.exception;

class InsufficientSeniorityException extends DomainException {
    public InsufficientSeniorityException(String message) {
        super(message);
    }
    
    public static InsufficientSeniorityException forAffiliate(Long affiliateId, int requiredMonths) {
        return new InsufficientSeniorityException(
            "Affiliate " + affiliateId + " does not have minimum seniority of " + requiredMonths + " months"
        );
    }
}
