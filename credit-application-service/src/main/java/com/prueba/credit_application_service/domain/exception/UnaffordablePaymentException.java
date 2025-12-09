package com.prueba.credit_application_service.domain.exception;

public class UnaffordablePaymentException extends DomainException {
    public UnaffordablePaymentException(String message) {
        super(message);
    }
    
    public static UnaffordablePaymentException forRatio(Double actualRatio, Double maxRatio) {
        return new UnaffordablePaymentException(
            "Payment ratio " + actualRatio + " exceeds maximum allowed " + maxRatio
        );
    }
}