package com.prueba.credit_application_service.domain.port.in;

import com.prueba.credit_application_service.domain.model.Affiliate;
import java.util.List;

public interface GetAffiliateUseCase {
    Affiliate getById(Long id);

    Affiliate getByDocument(String document);

    List<Affiliate> getAll();

    boolean existsByDocument(String document);
}
