package com.prueba.credit_application_service.domain.exception;

public class DuplicateDocumentException extends DomainException {
    public DuplicateDocumentException(String message) {
        super(message);
    }
    
    public static DuplicateDocumentException forDocument(String document) {
        return new DuplicateDocumentException("Document already exists: " + document);
    }
}