package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper;

import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * CreditApplicationMapper - Maps between domain and entity
 */
@Component
public class CreditApplicationMapper {

    private final AffiliateMapper affiliateMapper;
    private final RiskEvaluationMapper riskEvaluationMapper;

    public CreditApplicationMapper(@Lazy AffiliateMapper affiliateMapper,
            @Lazy RiskEvaluationMapper riskEvaluationMapper) {
        this.affiliateMapper = affiliateMapper;
        this.riskEvaluationMapper = riskEvaluationMapper;
    }

    public CreditApplication toDomain(CreditApplicationEntity entity) {
        if (entity == null)
            return null;

        CreditApplication domain = new CreditApplication();
        domain.setId(entity.getId());
        domain.setRequestedAmount(entity.getRequestedAmount());
        domain.setTermMonths(entity.getTermMonths());
        domain.setStatus(entity.getStatus());
        domain.setApplicationDate(entity.getApplicationDate());
        domain.setEvaluationDate(entity.getEvaluationDate());
        domain.setAnalystComments(entity.getAnalystComments());

        // Map affiliate
        if (entity.getAffiliate() != null) {
            domain.setAffiliate(affiliateMapper.toDomain(entity.getAffiliate()));
        }

        // Map risk evaluation
        if (entity.getRiskEvaluation() != null) {
            domain.setRiskEvaluation(riskEvaluationMapper.toDomain(entity.getRiskEvaluation()));
        }

        return domain;
    }

    public CreditApplicationEntity toEntity(CreditApplication domain) {
        if (domain == null)
            return null;

        CreditApplicationEntity entity = new CreditApplicationEntity();
        entity.setId(domain.getId());
        entity.setRequestedAmount(domain.getRequestedAmount());
        entity.setTermMonths(domain.getTermMonths());
        entity.setStatus(domain.getStatus());
        entity.setApplicationDate(domain.getApplicationDate());
        entity.setEvaluationDate(domain.getEvaluationDate());
        entity.setAnalystComments(domain.getAnalystComments());

        // Map affiliate
        if (domain.getAffiliate() != null) {
            entity.setAffiliate(affiliateMapper.toEntity(domain.getAffiliate()));
        }

        return entity;
    }
}
