package com.prueba.credit_application_service.domain.port.in;

import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import java.util.List;

public interface GetCreditApplicationUseCase {

    CreditApplication getById(Long id);

    List<CreditApplication> getAll();

    List<CreditApplication> getByAffiliateId(Long affiliateId);

    List<CreditApplication> getByStatus(CreditApplicationStatus status);
}
