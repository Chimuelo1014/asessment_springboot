package com.prueba.credit_application_service.infrastructure.config;

import com.prueba.credit_application_service.application.usecase.*;
import com.prueba.credit_application_service.domain.port.in.*;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;
import com.prueba.credit_application_service.domain.port.out.CreditApplicationRepositoryPort;
import com.prueba.credit_application_service.domain.port.out.RiskEvaluationPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegisterAffiliateUseCase registerAffiliateUseCase(
            AffiliateRepositoryPort affiliateRepository) {
        return new RegisterAffiliateService(affiliateRepository);
    }

    // Removed duplicate getAffiliateUseCase bean definition

    @Bean
    public UpdateAffiliateUseCase updateAffiliateUseCase(
            AffiliateRepositoryPort affiliateRepository) {
        return new UpdateAffiliateService(affiliateRepository);
    }

    @Bean
    public RegisterCreditApplicationUseCase registerCreditApplicationUseCase(
            AffiliateRepositoryPort affiliateRepository,
            CreditApplicationRepositoryPort creditApplicationRepository) {
        return new RegisterCreditApplicationService(
                affiliateRepository,
                creditApplicationRepository);
    }

    @Bean
    public EvaluateCreditApplicationUseCase evaluateCreditApplicationUseCase(
            CreditApplicationRepositoryPort creditApplicationRepository,
            RiskEvaluationPort riskEvaluationPort) {
        return new EvaluateCreditApplicationService(
                creditApplicationRepository,
                riskEvaluationPort);
    }

    @Bean
    public GetCreditApplicationUseCase getCreditApplicationUseCase(
            CreditApplicationRepositoryPort creditApplicationRepository) {
        return new GetCreditApplicationService(creditApplicationRepository);
    }
}