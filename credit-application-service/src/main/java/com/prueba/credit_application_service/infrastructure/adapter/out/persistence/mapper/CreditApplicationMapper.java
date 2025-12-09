package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper;

import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * CreditApplicationMapper - Maps between domain and entity using MapStruct
 */
@Mapper(componentModel = "spring", uses = { AffiliateMapper.class, RiskEvaluationMapper.class })
public interface CreditApplicationMapper {

    /**
     * Convert entity to domain model
     * 
     * @param entity the credit application entity
     * @return the credit application domain model
     */
    @Mapping(target = "affiliate", source = "affiliate")
    @Mapping(target = "riskEvaluation", source = "riskEvaluation")
    CreditApplication toDomain(CreditApplicationEntity entity);

    /**
     * Convert domain model to entity
     * 
     * @param domain the credit application domain model
     * @return the credit application entity
     */
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "affiliate", source = "affiliate")
    @Mapping(target = "riskEvaluation", ignore = true) // Risk evaluation is set separately
    CreditApplicationEntity toEntity(CreditApplication domain);
}