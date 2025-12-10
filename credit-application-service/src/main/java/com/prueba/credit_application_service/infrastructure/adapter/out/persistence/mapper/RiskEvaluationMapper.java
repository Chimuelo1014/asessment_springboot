package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper;

import com.prueba.credit_application_service.domain.model.RiskEvaluation;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.RiskEvaluationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * RiskEvaluationMapper - Maps between domain and entity using MapStruct
 */
@Mapper(componentModel = "spring")
public interface RiskEvaluationMapper {

    /**
     * Convert entity to domain model
     * 
     * @param entity the risk evaluation entity
     * @return the risk evaluation domain model
     */
    RiskEvaluation toDomain(RiskEvaluationEntity entity);

    /**
     * Convert domain model to entity
     * 
     * @param domain the risk evaluation domain model
     * @return the risk evaluation entity
     */
    @Mapping(target = "creditApplication.version", ignore = true)
    @Mapping(target = "creditApplication.affiliate.version", ignore = true)
    RiskEvaluationEntity toEntity(RiskEvaluation domain);
}
