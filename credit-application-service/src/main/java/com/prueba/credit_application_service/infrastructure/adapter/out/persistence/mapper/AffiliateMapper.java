package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper;

import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.model.RiskEvaluation;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.AffiliateEntity;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.CreditApplicationEntity;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.RiskEvaluationEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * AffiliateMapper - Maps between domain and entity
 */
@Component
public class AffiliateMapper {
    
    public Affiliate toDomain(AffiliateEntity entity) {
        if (entity == null) return null;
        
        Affiliate domain = new Affiliate();
        domain.setId(entity.getId());
        domain.setDocument(entity.getDocument());
        domain.setFullName(entity.getFullName());
        domain.setEmail(entity.getEmail());
        domain.setPhone(entity.getPhone());
        domain.setMonthlySalary(entity.getMonthlySalary());
        domain.setStatus(entity.getStatus());
        domain.setAffiliationDate(entity.getAffiliationDate());
        
        // Don't map creditApplications to avoid circular reference
        // They will be loaded separately if needed
        
        return domain;
    }
    
    public AffiliateEntity toEntity(Affiliate domain) {
        if (domain == null) return null;
        
        AffiliateEntity entity = new AffiliateEntity();
        entity.setId(domain.getId());
        entity.setDocument(domain.getDocument());
        entity.setFullName(domain.getFullName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setMonthlySalary(domain.getMonthlySalary());
        entity.setStatus(domain.getStatus());
        entity.setAffiliationDate(domain.getAffiliationDate());
        
        return entity;
    }
}

/**
 * CreditApplicationMapper - Maps between domain and entity
 */
@Component
public class CreditApplicationMapper {
    
    private final AffiliateMapper affiliateMapper;
    private final RiskEvaluationMapper riskEvaluationMapper;
    
    public CreditApplicationMapper(AffiliateMapper affiliateMapper, 
                                   RiskEvaluationMapper riskEvaluationMapper) {
        this.affiliateMapper = affiliateMapper;
        this.riskEvaluationMapper = riskEvaluationMapper;
    }
    
    public CreditApplication toDomain(CreditApplicationEntity entity) {
        if (entity == null) return null;
        
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
        if (domain == null) return null;
        
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

/**
 * RiskEvaluationMapper - Maps between domain and entity
 */
@Component
public class RiskEvaluationMapper {
    
    public RiskEvaluation toDomain(RiskEvaluationEntity entity) {
        if (entity == null) return null;
        
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
        if (domain == null) return null;
        
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