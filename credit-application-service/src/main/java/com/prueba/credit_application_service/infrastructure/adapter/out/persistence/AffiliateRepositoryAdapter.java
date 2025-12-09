package com.prueba.credit_application_service.infrastructure.adapter.out.persistence;

import com.coopcredit.creditapp.domain.model.Affiliate;
import com.coopcredit.creditapp.domain.port.out.AffiliateRepositoryPort;
import com.coopcredit.creditapp.infrastructure.adapter.out.persistence.entity.AffiliateEntity;
import com.coopcredit.creditapp.infrastructure.adapter.out.persistence.mapper.AffiliateMapper;
import com.coopcredit.creditapp.infrastructure.adapter.out.persistence.repository.AffiliateJpaRepository;
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
public class AffiliateRepositoryAdapter implements AffiliateRepositoryPort {

    private final AffiliateJpaRepository jpaRepository;
    private final AffiliateMapper mapper;

    public AffiliateRepositoryAdapter(AffiliateJpaRepository jpaRepository, 
                                     AffiliateMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Affiliate save(Affiliate affiliate) {
        // Convert domain to entity
        AffiliateEntity entity = mapper.toEntity(affiliate);
        // Persist
        AffiliateEntity saved = jpaRepository.save(entity);
        // Convert back to domain
        return mapper.toDomain(saved);
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
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
