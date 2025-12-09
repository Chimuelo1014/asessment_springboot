package com.prueba.credit_application_service.domain.port.in;

import com.prueba.credit_application_service.domain.model.CreditApplication;

public interface EvaluateCreditApplicationUseCase {

    CreditApplication evaluate(Long applicationId);

    CreditApplication approveManually(Long applicationId, String comments);

    CreditApplication rejectManually(Long applicationId, String comments);
}
