package com.prueba.credit_application_service.domain.exception;

class AffiliateNotFoundException extends DomainException {
    public AffiliateNotFoundException(String message) {
        super(message);
    }
    
    public static AffiliateNotFoundException withId(Long id) {
        return new AffiliateNotFoundException("Affiliate not found with id: " + id);
    }
    
    public static AffiliateNotFoundException withDocument(String document) {
        return new AffiliateNotFoundException("Affiliate not found with document: " + document);
    }
}
