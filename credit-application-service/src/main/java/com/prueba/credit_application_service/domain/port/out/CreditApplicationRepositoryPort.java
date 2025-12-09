package com.prueba.credit_application_service.domain.port.out;

import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;

import java.util.List;
import java.util.Optional;

/**
 * CreditApplicationRepositoryPort - OUTPUT PORT (DOMAIN INTERFACE)
 * 
 * This is a PURE domain interface that defines the contract
 * for credit application persistence operations.
 * 
 * The implementation will be in the INFRASTRUCTURE layer.
 */
public interface CreditApplicationRepositoryPort {
    
    CreditApplication save(CreditApplication creditApplication);
    
    Optional<CreditApplication> findById(Long id);
    
    List<CreditApplication> findAll();
    
    List<CreditApplication> findByAffiliateId(Long affiliateId);
    
    List<CreditApplication> findByStatus(CreditApplicationStatus status);
    
    void deleteById(Long id);
}