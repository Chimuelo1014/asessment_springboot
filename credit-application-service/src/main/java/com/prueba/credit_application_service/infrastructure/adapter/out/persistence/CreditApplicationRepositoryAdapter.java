package com.prueba.credit_application_service.infrastructure.adapter.out.persistence;

import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import com.prueba.credit_application_service.domain.port.out.CreditApplicationRepositoryPort;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.RiskEvaluationEntity;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper.CreditApplicationMapper;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper.RiskEvaluationMapper;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.repository.CreditApplicationJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CreditApplicationRepositoryAdapter - INFRASTRUCTURE ADAPTER
 */
@Component
public class CreditApplicationRepositoryAdapter implements CreditApplicationRepositoryPort {

    private final CreditApplicationJpaRepository jpaRepository;
    private final CreditApplicationMapper mapper;
    private final RiskEvaluationMapper riskMapper;

    public CreditApplicationRepositoryAdapter(
            CreditApplicationJpaRepository jpaRepository,
            CreditApplicationMapper mapper,
            RiskEvaluationMapper riskMapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.riskMapper = riskMapper;
    }

    @Override
    @Transactional
    public CreditApplication save(CreditApplication creditApplication) {
        // Convert domain to entity
        CreditApplicationEntity entity = mapper.toEntity(creditApplication);

        // Handle RiskEvaluation relationship if present
        if (creditApplication.getRiskEvaluation() != null) {
            RiskEvaluationEntity riskEntity = riskMapper.toEntity(
                    creditApplication.getRiskEvaluation());
            riskEntity.setCreditApplication(entity);
            entity.setRiskEvaluation(riskEntity);
        }

        // Persist
        CreditApplicationEntity saved = jpaRepository.save(entity);

        // Convert back to domain
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<CreditApplication> findById(Long id) {
        return jpaRepository.findByIdWithDetails(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<CreditApplication> findAll() {
        // Use version with affiliate and riskEvaluation to avoid
        // LazyInitializationException
        return jpaRepository.findAllWithDetails().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreditApplication> findByAffiliateId(Long affiliateId) {
        // Repository already has JOIN FETCH for affiliate
        return jpaRepository.findByAffiliateId(affiliateId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreditApplication> findByStatus(CreditApplicationStatus status) {
        // Use version with JOIN FETCH defined in repository
        return jpaRepository.findByStatusWithAffiliate(status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
