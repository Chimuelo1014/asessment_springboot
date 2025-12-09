package com.prueba.credit_application_service.application.usecase;

import com.coopcredit.creditapp.domain.exception.CreditApplicationNotFoundException;
import com.coopcredit.creditapp.domain.model.CreditApplication;
import com.coopcredit.creditapp.domain.model.RiskEvaluation;
import com.coopcredit.creditapp.domain.model.enums.CreditApplicationStatus;
import com.coopcredit.creditapp.domain.port.in.EvaluateCreditApplicationUseCase;
import com.coopcredit.creditapp.domain.port.out.CreditApplicationRepositoryPort;
import com.coopcredit.creditapp.domain.port.out.RiskEvaluationPort;

/**
 * EvaluateCreditApplicationService - USE CASE (PURE APPLICATION LOGIC)
 */
public class EvaluateCreditApplicationService implements EvaluateCreditApplicationUseCase {

    private final CreditApplicationRepositoryPort creditApplicationRepository;
    private final RiskEvaluationPort riskEvaluationPort;

    public EvaluateCreditApplicationService(
            CreditApplicationRepositoryPort creditApplicationRepository,
            RiskEvaluationPort riskEvaluationPort) {
        this.creditApplicationRepository = creditApplicationRepository;
        this.riskEvaluationPort = riskEvaluationPort;
    }

    @Override
    public CreditApplication evaluate(Long applicationId) {
        // Get application
        CreditApplication application = creditApplicationRepository.findById(applicationId)
            .orElseThrow(() -> CreditApplicationNotFoundException.withId(applicationId));
        
        // Validate state using domain method
        if (!application.canBeEvaluated()) {
            throw new IllegalStateException(
                "Application cannot be evaluated in status: " + application.getStatus());
        }
        
        // Call external service through port
        RiskEvaluation riskEvaluation = riskEvaluationPort.evaluateRisk(
            application.getAffiliate().getDocument(),
            application.getAffiliate().getFullName(),
            application.getRequestedAmount(),
            application.getAffiliate().getMonthlySalary()
        );
        
        // Set risk evaluation
        application.setRiskEvaluation(riskEvaluation);
        application.markAsUnderReview();
        
        // Apply business rules based on risk using domain methods
        if (riskEvaluation.isApprovalRecommended() && riskEvaluation.isLowRisk()) {
            application.approve("Auto-approved based on risk evaluation");
        } else if (riskEvaluation.requiresManualReview()) {
            // Keep as UNDER_REVIEW
        } else {
            application.reject("Auto-rejected based on risk evaluation");
        }
        
        // Persist through port
        return creditApplicationRepository.save(application);
    }

    @Override
    public CreditApplication approveManually(Long applicationId, String comments) {
        CreditApplication application = creditApplicationRepository.findById(applicationId)
            .orElseThrow(() -> CreditApplicationNotFoundException.withId(applicationId));
        
        // Use domain method
        application.approve(comments);
        
        return creditApplicationRepository.save(application);
    }

    @Override
    public CreditApplication rejectManually(Long applicationId, String comments) {
        CreditApplication application = creditApplicationRepository.findById(applicationId)
            .orElseThrow(() -> CreditApplicationNotFoundException.withId(applicationId));
        
        // Use domain method
        application.reject(comments);
        
        return creditApplicationRepository.save(application);
    }
}
