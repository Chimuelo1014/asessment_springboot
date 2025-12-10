package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper;

import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.AffiliateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * AffiliateMapper - Maps between domain and entity using MapStruct
 */
@Mapper(componentModel = "spring")
public interface AffiliateMapper {

    /**
     * Convert entity to domain model
     * 
     * @param entity the affiliate entity
     * @return the affiliate domain model
     */
    Affiliate toDomain(AffiliateEntity entity);

    /**
     * Convert domain model to entity
     * 
     * @param domain the affiliate domain model
     * @return the affiliate entity
     */
    @Mapping(target = "version", ignore = true)
    AffiliateEntity toEntity(Affiliate domain);
}
