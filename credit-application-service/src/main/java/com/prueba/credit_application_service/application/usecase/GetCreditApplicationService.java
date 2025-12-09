package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.CreditApplicationNotFoundException;
import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import com.prueba.credit_application_service.domain.port.in.GetCreditApplicationUseCase;
import com.prueba.credit_application_service.domain.port.out.CreditApplicationRepositoryPort;

import java.util.List;

/**
 * GetCreditApplicationService - USE CASE (PURE APPLICATION LOGIC)
 */
public class GetCreditApplicationService implements GetCreditApplicationUseCase {

    private final CreditApplicationRepositoryPort creditApplicationRepository;

    public GetCreditApplicationService(CreditApplicationRepositoryPort creditApplicationRepository) {
        this.creditApplicationRepository = creditApplicationRepository;
    }

    @Override
    public CreditApplication getById(Long id) {
        return creditApplicationRepository.findById(id)
                .orElseThrow(() -> CreditApplicationNotFoundException.withId(id));
    }

    @Override
    public List<CreditApplication> getAll() {
        return creditApplicationRepository.findAll();
    }

    @Override
    public List<CreditApplication> getByAffiliateId(Long affiliateId) {
        return creditApplicationRepository.findByAffiliateId(affiliateId);
    }

    @Override
    public List<CreditApplication> getByStatus(CreditApplicationStatus status) {
        return creditApplicationRepository.findByStatus(status);
    }
}