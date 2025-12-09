package com.prueba.credit_application_service.application.usecase;

import com.coopcredit.creditapp.domain.exception.CreditApplicationNotFoundException;
import com.coopcredit.creditapp.domain.model.CreditApplication;
import com.coopcredit.creditapp.domain.model.enums.CreditApplicationStatus;
import com.coopcredit.creditapp.domain.port.in.GetCreditApplicationUseCase;
import com.coopcredit.creditapp.domain.port.out.CreditApplicationRepositoryPort;

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