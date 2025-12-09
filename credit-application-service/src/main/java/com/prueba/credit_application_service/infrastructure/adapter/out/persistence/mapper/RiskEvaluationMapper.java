package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper;

import com.prueba.credit_application_service.domain.model.RiskEvaluation;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.RiskEvaluationEntity;
import org.springframework.stereotype.Component;

/**
 * RiskEvaluationMapper - Maps between domain and entity
 */
@Component
public class RiskEvaluationMapper {

    public RiskEvaluation toDomain(RiskEvaluationEntity entity) {
        if (entity == null)
            return null;

        RiskEvaluation domain = new RiskEvaluation();
        domain.setId(entity.getId());
        domain.setScore(entity.getScore());
        domain.setRiskLevel(entity.getRiskLevel());
        domain.setRecommendation(entity.getRecommendation());
        domain.setEvaluationMessage(entity.getEvaluationMessage());
        domain.setEvaluationDate(entity.getEvaluationDate());

        return domain;
    }

    public RiskEvaluationEntity toEntity(RiskEvaluation domain) {
        if (domain == null)
            return null;

        RiskEvaluationEntity entity = new RiskEvaluationEntity();
        entity.setId(domain.getId());
        entity.setScore(domain.getScore());
        entity.setRiskLevel(domain.getRiskLevel());
        entity.setRecommendation(domain.getRecommendation());
        entity.setEvaluationMessage(domain.getEvaluationMessage());
        entity.setEvaluationDate(domain.getEvaluationDate());

        return entity;
    }
}
