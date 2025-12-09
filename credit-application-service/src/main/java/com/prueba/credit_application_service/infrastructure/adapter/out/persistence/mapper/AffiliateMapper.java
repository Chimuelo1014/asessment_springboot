package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper;

import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.model.RiskEvaluation;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.AffiliateEntity;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.RiskEvaluationEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * AffiliateMapper - Maps between domain and entity
 */
@Component
public class AffiliateMapper {

    public Affiliate toDomain(AffiliateEntity entity) {
        if (entity == null)
            return null;

        Affiliate domain = new Affiliate();
        domain.setId(entity.getId());
        domain.setDocument(entity.getDocument());
        domain.setFullName(entity.getFullName());
        domain.setEmail(entity.getEmail());
        domain.setPhone(entity.getPhone());
        domain.setMonthlySalary(entity.getMonthlySalary());
        domain.setStatus(entity.getStatus());
        domain.setAffiliationDate(entity.getAffiliationDate());

        // Don't map creditApplications to avoid circular reference
        // They will be loaded separately if needed

        return domain;
    }

    public AffiliateEntity toEntity(Affiliate domain) {
        if (domain == null)
            return null;

        AffiliateEntity entity = new AffiliateEntity();
        entity.setId(domain.getId());
        entity.setDocument(domain.getDocument());
        entity.setFullName(domain.getFullName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setMonthlySalary(domain.getMonthlySalary());
        entity.setStatus(domain.getStatus());
        entity.setAffiliationDate(domain.getAffiliationDate());

        return entity;
    }
}
