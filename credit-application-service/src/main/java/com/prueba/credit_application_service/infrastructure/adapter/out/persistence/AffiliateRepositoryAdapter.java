package com.prueba.credit_application_service.infrastructure.adapter.out.persistence;

import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.AffiliateEntity;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper.AffiliateMapper;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.repository.AffiliateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * AffiliateRepositoryAdapter - INFRASTRUCTURE ADAPTER
 * 
 * This adapter implements the domain port (AffiliateRepositoryPort)
 * and translates between domain objects and persistence entities.
 * 
 * It belongs to the INFRASTRUCTURE layer and can use Spring, JPA, etc.
 */
@Component
@RequiredArgsConstructor
public class AffiliateRepositoryAdapter implements AffiliateRepositoryPort {

    private final AffiliateJpaRepository jpaRepository;
    private final AffiliateMapper mapper;

    @Override
    @CacheEvict(value = "affiliates", allEntries = true)
    public Affiliate save(Affiliate affiliate) {
        AffiliateEntity entity;
        if (affiliate.getId() != null) {
            // Update scenario: Fetch existing to get Version
            entity = jpaRepository.findById(affiliate.getId())
                    .orElse(mapper.toEntity(affiliate));
            mapper.updateEntity(entity, affiliate);
        } else {
            // Create scenario
            entity = mapper.toEntity(affiliate);
        }

        // Persist
        AffiliateEntity savedEntity = jpaRepository.save(entity);
        // Convert back to domain
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Affiliate> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Affiliate> findByDocument(String document) {
        return jpaRepository.findByDocument(document)
                .map(mapper::toDomain);
    }

    @Override
    public List<Affiliate> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByDocument(String document) {
        return jpaRepository.existsByDocument(document);
    }

    @Override
    @CacheEvict(value = "affiliates", key = "#id")
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
