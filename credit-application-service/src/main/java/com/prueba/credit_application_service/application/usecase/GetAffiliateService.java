package com.prueba.credit_application_service.application.usecase;

import com.coopcredit.creditapp.domain.exception.AffiliateNotFoundException;
import com.coopcredit.creditapp.domain.model.Affiliate;
import com.coopcredit.creditapp.domain.port.in.GetAffiliateUseCase;
import com.coopcredit.creditapp.domain.port.out.AffiliateRepositoryPort;

import java.util.List;

/**
 * GetAffiliateService - USE CASE (PURE APPLICATION LOGIC)
 */
public class GetAffiliateService implements GetAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public GetAffiliateService(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Affiliate getById(Long id) {
        return affiliateRepository.findById(id)
            .orElseThrow(() -> AffiliateNotFoundException.withId(id));
    }

    @Override
    public Affiliate getByDocument(String document) {
        return affiliateRepository.findByDocument(document)
            .orElseThrow(() -> AffiliateNotFoundException.withDocument(document));
    }

    @Override
    public List<Affiliate> getAll() {
        return affiliateRepository.findAll();
    }
}