package com.prueba.credit_application_service.domain.exception;

public class CreditApplicationNotFoundException extends DomainException {
    public CreditApplicationNotFoundException(String message) {
        super(message);
    }
    
    public static CreditApplicationNotFoundException withId(Long id) {
        return new CreditApplicationNotFoundException("Credit application not found with id: " + id);
    }
}