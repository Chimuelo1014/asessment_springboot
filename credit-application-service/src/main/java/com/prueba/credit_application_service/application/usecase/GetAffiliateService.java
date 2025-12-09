package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.AffiliateNotFoundException;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.port.in.GetAffiliateUseCase;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;

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