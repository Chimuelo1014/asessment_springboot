package com.prueba.credit_application_service.domain.port.in;

import com.prueba.credit_application_service.domain.model.Affiliate;

/**
 * UpdateAffiliateUseCase - NUEVO según enunciado
 * El enunciado requiere "Editar información básica" de afiliados
 */
public interface UpdateAffiliateUseCase {

    Affiliate update(UpdateAffiliateCommand command);

    record UpdateAffiliateCommand(
            Long id,
            String fullName,
            String email,
            String phone,
            Double monthlySalary) {
    }
}