package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.CreditApplicationNotFoundException;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.model.RiskEvaluation;
import com.prueba.credit_application_service.domain.model.enums.AffiliateStatus;
import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import com.prueba.credit_application_service.domain.model.enums.RiskLevel;
import com.prueba.credit_application_service.domain.port.out.CreditApplicationRepositoryPort;
import com.prueba.credit_application_service.domain.port.out.RiskEvaluationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluateCreditApplicationServiceTest {

    @Mock
    private CreditApplicationRepositoryPort creditApplicationRepository;

    @Mock
    private RiskEvaluationPort riskEvaluationPort;

    private EvaluateCreditApplicationService evaluateService;

    @BeforeEach
    void setUp() {
        evaluateService = new EvaluateCreditApplicationService(
            creditApplicationRepository,
            riskEvaluationPort
        );
    }

    @Test
    void shouldEvaluateAndAutoApproveForLowRisk() {
        // Given
        Affiliate affiliate = createAffiliate();
        CreditApplication application = createCreditApplication(affiliate);
        RiskEvaluation riskEvaluation = createRiskEvaluation(750, RiskLevel.LOW, "APPROVED");

        when(creditApplicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(riskEvaluationPort.evaluateRisk(any(), any(), any(), any())).thenReturn(riskEvaluation);
        when(creditApplicationRepository.save(any(CreditApplication.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CreditApplication result = evaluateService.evaluate(1L);

        // Then
        assertThat(result.getStatus()).isEqualTo(CreditApplicationStatus.APPROVED);
        assertThat(result.getRiskEvaluation()).isNotNull();
        verify(riskEvaluationPort).evaluateRisk(any(), any(), any(), any());
        verify(creditApplicationRepository).save(any(CreditApplication.class));
    }

    @Test
    void shouldKeepUnderReviewForMediumRisk() {
        // Given
        Affiliate affiliate = createAffiliate();
        CreditApplication application = createCreditApplication(affiliate);
        RiskEvaluation riskEvaluation = createRiskEvaluation(600, RiskLevel.MEDIUM, "REVIEW");

        when(creditApplicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(riskEvaluationPort.evaluateRisk(any(), any(), any(), any())).thenReturn(riskEvaluation);
        when(creditApplicationRepository.save(any(CreditApplication.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CreditApplication result = evaluateService.evaluate(1L);

        // Then
        assertThat(result.getStatus()).isEqualTo(CreditApplicationStatus.UNDER_REVIEW);
    }

    @Test
    void shouldThrowExceptionWhenApplicationNotFound() {
        // Given
        when(creditApplicationRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> evaluateService.evaluate(999L))
            .isInstanceOf(CreditApplicationNotFoundException.class);
    }

    private Affiliate createAffiliate() {
        Affiliate affiliate = new Affiliate();
        affiliate.setId(1L);
        affiliate.setDocument("123456789");
        affiliate.setFullName("John Doe");
        affiliate.setEmail("john@example.com");
        affiliate.setMonthlySalary(5000.0);
        affiliate.setStatus(AffiliateStatus.ACTIVE);
        affiliate.setAffiliationDate(LocalDate.now().minusYears(1));
        return affiliate;
    }

    private CreditApplication createCreditApplication(Affiliate affiliate) {
        CreditApplication app = new CreditApplication();
        app.setId(1L);
        app.setAffiliate(affiliate);
        app.setRequestedAmount(10000.0);
        app.setTermMonths(12);
        app.setStatus(CreditApplicationStatus.PENDING);
        app.setApplicationDate(LocalDateTime.now());
        return app;
    }

    private RiskEvaluation createRiskEvaluation(int score, RiskLevel level, String recommendation) {
        RiskEvaluation risk = new RiskEvaluation();
        risk.setScore(score);
        risk.setRiskLevel(level);
        risk.setRecommendation(recommendation);
        risk.setEvaluationMessage("Test evaluation");
        risk.setEvaluationDate(LocalDateTime.now());
        return risk;
    }
}