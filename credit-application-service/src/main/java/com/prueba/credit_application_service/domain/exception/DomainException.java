package com.prueba.credit_application_service.domain.exception;

/**
 * Base Domain Exception
 */
public abstract class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
