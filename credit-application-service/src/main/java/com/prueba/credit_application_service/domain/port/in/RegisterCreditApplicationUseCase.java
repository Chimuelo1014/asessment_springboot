package com.prueba.credit_application_service.domain.port.in;

import com.prueba.credit_application_service.domain.model.CreditApplication;

public interface RegisterCreditApplicationUseCase {

    CreditApplication register(CreditApplicationCommand command);

    record CreditApplicationCommand(
            Long affiliateId,
            Double requestedAmount,
            Integer termMonths,
            Double interestRate) { // NUEVO campo
    }
}