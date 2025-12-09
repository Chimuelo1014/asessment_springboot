package com.prueba.credit_application_service.infrastructure.config;

import com.coopcredit.creditapp.application.usecase.*;
import com.coopcredit.creditapp.domain.port.in.*;
import com.coopcredit.creditapp.domain.port.out.AffiliateRepositoryPort;
import com.coopcredit.creditapp.domain.port.out.CreditApplicationRepositoryPort;
import com.coopcredit.creditapp.domain.port.out.RiskEvaluationPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UseCaseConfig - INFRASTRUCTURE LAYER
 * 
 * This is where we wire the PURE use cases with their dependencies.
 * This is the ONLY place where Spring Framework touches the application layer.
 * 
 * The use cases themselves remain PURE and framework-agnostic.
 */
@Configuration
public class UseCaseConfig {

    /**
     * Wire RegisterAffiliateUseCase
     * The use case class is PURE, we just instantiate it here with Spring
     */
    @Bean
    public RegisterAffiliateUseCase registerAffiliateUseCase(
            AffiliateRepositoryPort affiliateRepository) {
        return new RegisterAffiliateService(affiliateRepository);
    }

    /**
     * Wire GetAffiliateUseCase
     */
    @Bean
    public GetAffiliateUseCase getAffiliateUseCase(
            AffiliateRepositoryPort affiliateRepository) {
        return new GetAffiliateService(affiliateRepository);
    }

    /**
     * Wire RegisterCreditApplicationUseCase
     */
    @Bean
    public RegisterCreditApplicationUseCase registerCreditApplicationUseCase(
            AffiliateRepositoryPort affiliateRepository,
            CreditApplicationRepositoryPort creditApplicationRepository) {
        return new RegisterCreditApplicationService(
            affiliateRepository, 
            creditApplicationRepository
        );
    }

    /**
     * Wire EvaluateCreditApplicationUseCase
     */
    @Bean
    public EvaluateCreditApplicationUseCase evaluateCreditApplicationUseCase(
            CreditApplicationRepositoryPort creditApplicationRepository,
            RiskEvaluationPort riskEvaluationPort) {
        return new EvaluateCreditApplicationService(
            creditApplicationRepository,
            riskEvaluationPort
        );
    }

    /**
     * Wire GetCreditApplicationUseCase
     */
    @Bean
    public GetCreditApplicationUseCase getCreditApplicationUseCase(
            CreditApplicationRepositoryPort creditApplicationRepository) {
        return new GetCreditApplicationService(creditApplicationRepository);
    }
}
