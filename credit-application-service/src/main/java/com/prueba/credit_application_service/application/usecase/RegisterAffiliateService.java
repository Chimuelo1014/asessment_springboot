package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.DuplicateDocumentException;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.enums.AffiliateStatus;
import com.prueba.credit_application_service.domain.port.in.RegisterAffiliateUseCase;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;

import java.time.LocalDate;

/**
 * RegisterAffiliateService - USE CASE (PURE APPLICATION LOGIC)
 * NO Spring annotations in the class itself
 * Dependencies injected via constructor (DI by framework)
 */
public class RegisterAffiliateService implements RegisterAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    // Constructor injection (framework-agnostic)
    public RegisterAffiliateService(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Affiliate register(AffiliateRegistrationCommand command) {
        // Business validation
        if (affiliateRepository.existsByDocument(command.document())) {
            throw DuplicateDocumentException.forDocument(command.document());
        }

        // Create domain object
        Affiliate affiliate = new Affiliate();
        affiliate.setDocument(command.document());
        affiliate.setFullName(command.fullName());
        affiliate.setEmail(command.email());
        affiliate.setPhone(command.phone());
        affiliate.setMonthlySalary(command.monthlySalary());
        affiliate.setStatus(AffiliateStatus.ACTIVE);
        affiliate.setAffiliationDate(command.affiliationDate() != null ? command.affiliationDate() : LocalDate.now());

        // Persist through port
        return affiliateRepository.save(affiliate);
    }
}