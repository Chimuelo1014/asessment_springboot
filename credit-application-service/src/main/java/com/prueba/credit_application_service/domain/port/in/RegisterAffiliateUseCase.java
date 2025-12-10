package com.prueba.credit_application_service.domain.port.in;

import com.prueba.credit_application_service.domain.model.Affiliate;

public interface RegisterAffiliateUseCase {

    Affiliate register(AffiliateRegistrationCommand command);

    record AffiliateRegistrationCommand(
            String document,
            String fullName,
            String email,
            String phone,
            Double monthlySalary,
            java.time.LocalDate affiliationDate) {
    }
}