package com.prueba.credit_application_service.domain.exception;

public class ExcessiveCreditAmountException extends DomainException {
    public ExcessiveCreditAmountException(String message) {
        super(message);
    }
    
    public static ExcessiveCreditAmountException forAmount(Double requested, Double maximum) {
        return new ExcessiveCreditAmountException(
            "Requested amount " + requested + " exceeds maximum allowed " + maximum
        );
    }
}