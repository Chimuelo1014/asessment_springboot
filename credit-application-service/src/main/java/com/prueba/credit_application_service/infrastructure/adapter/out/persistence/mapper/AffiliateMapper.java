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
    @Mapping(target = "creditApplications", ignore = true)
    Affiliate toDomain(AffiliateEntity entity);

    /**
     * Convert domain model to entity
     * 
     * @param domain the affiliate domain model
     * @return the affiliate entity
     */
    @Mapping(target = "version", ignore = true)
    AffiliateEntity toEntity(Affiliate domain);

    /**
     * Update existing entity from domain
     * 
     * @param entity target entity
     * @param domain source domain
     */
    @Mapping(target = "id", ignore = true) // ID shouldn't change
    @Mapping(target = "creditApplications", ignore = true) // Don't touch collections
    @Mapping(target = "version", ignore = true) // Don't touch version
    void updateEntity(@org.mapstruct.MappingTarget AffiliateEntity entity, Affiliate domain);
}
