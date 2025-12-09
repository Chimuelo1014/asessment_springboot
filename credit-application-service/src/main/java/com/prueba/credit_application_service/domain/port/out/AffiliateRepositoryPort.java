package com.prueba.credit_application_service.domain.port.out;

import com.prueba.credit_application_service.domain.model.Affiliate;

import java.util.List;
import java.util.Optional;

/**
 * AffiliateRepositoryPort - OUTPUT PORT (DOMAIN INTERFACE)
 * 
 * This is a PURE domain interface that defines the contract
 * for affiliate persistence operations.
 * 
 * The implementation will be in the INFRASTRUCTURE layer.
 */
public interface AffiliateRepositoryPort {
    
    Affiliate save(Affiliate affiliate);
    
    Optional<Affiliate> findById(Long id);
    
    Optional<Affiliate> findByDocument(String document);
    
    List<Affiliate> findAll();
    
    boolean existsByDocument(String document);
    
    void deleteById(Long id);
}