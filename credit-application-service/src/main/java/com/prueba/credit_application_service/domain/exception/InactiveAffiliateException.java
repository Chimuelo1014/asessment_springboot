package com.prueba.credit_application_service.domain.exception;

public class InactiveAffiliateException extends DomainException {
    public InactiveAffiliateException(String message) {
        super(message);
    }
    
    public static InactiveAffiliateException forAffiliate(Long affiliateId) {
        return new InactiveAffiliateException("Affiliate is not active: " + affiliateId);
    }
}