package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.AffiliateNotFoundException;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.port.in.GetAffiliateUseCase;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * GetAffiliateService - USE CASE (PURE APPLICATION LOGIC)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "affiliates")
public class GetAffiliateService implements GetAffiliateUseCase {

    private final AffiliateRepositoryPort repositoryPort;

    @Override
    @Cacheable(key = "#id")
    public Affiliate getById(Long id) {
        log.info("Getting affiliate by id: {}", id);
        return repositoryPort.findById(id)
                .orElseThrow(() -> AffiliateNotFoundException.withId(id));
    }

    @Override
    @Cacheable(key = "#document")
    public Affiliate getByDocument(String document) {
        log.info("Getting affiliate by document: {}", document);
        return repositoryPort.findByDocument(document)
                .orElseThrow(() -> AffiliateNotFoundException.withDocument(document));
    }

    @Override
    public List<Affiliate> getAll() {
        log.info("Getting all affiliates");
        return repositoryPort.findAll();
    }

    @Override
    public boolean existsByDocument(String document) {
        log.debug("Checking if affiliate exists with document: {}", document);
        return repositoryPort.existsByDocument(document);
    }
}