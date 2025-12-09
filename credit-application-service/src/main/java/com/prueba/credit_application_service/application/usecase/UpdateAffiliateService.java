package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.AffiliateNotFoundException;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.port.in.UpdateAffiliateUseCase;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;

/**
 * UpdateAffiliateService - USE CASE (PURE APPLICATION LOGIC)
 * NUEVO: Implementa la funcionalidad de editar afiliados requerida en el enunciado
 */
public class UpdateAffiliateService implements UpdateAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public UpdateAffiliateService(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Affiliate update(UpdateAffiliateCommand command) {
        // Obtener afiliado existente
        Affiliate affiliate = affiliateRepository.findById(command.id())
                .orElseThrow(() -> AffiliateNotFoundException.withId(command.id()));

        // Actualizar solo información básica (no documento ni estado)
        if (command.fullName() != null && !command.fullName().isBlank()) {
            affiliate.setFullName(command.fullName());
        }
        
        if (command.email() != null && !command.email().isBlank()) {
            affiliate.setEmail(command.email());
        }
        
        if (command.phone() != null) {
            affiliate.setPhone(command.phone());
        }
        
        if (command.monthlySalary() != null && command.monthlySalary() > 0) {
            affiliate.setMonthlySalary(command.monthlySalary());
        }

        // Persistir cambios
        return affiliateRepository.save(affiliate);
    }
}