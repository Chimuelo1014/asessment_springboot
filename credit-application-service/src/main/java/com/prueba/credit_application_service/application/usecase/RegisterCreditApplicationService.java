package com.prueba.credit_application_service.application.usecase;

import com.coopcredit.creditapp.domain.exception.*;
import com.coopcredit.creditapp.domain.model.Affiliate;
import com.coopcredit.creditapp.domain.model.CreditApplication;
import com.coopcredit.creditapp.domain.model.enums.CreditApplicationStatus;
import com.coopcredit.creditapp.domain.port.in.RegisterCreditApplicationUseCase;
import com.coopcredit.creditapp.domain.port.out.AffiliateRepositoryPort;
import com.coopcredit.creditapp.domain.port.out.CreditApplicationRepositoryPort;

import java.time.LocalDateTime;

/**
 * RegisterCreditApplicationService - USE CASE (PURE APPLICATION LOGIC)
 */
public class RegisterCreditApplicationService implements RegisterCreditApplicationUseCase {

    private final AffiliateRepositoryPort affiliateRepository;
    private final CreditApplicationRepositoryPort creditApplicationRepository;
    
    // Business rules as constants
    private static final int MINIMUM_SENIORITY_MONTHS = 6;
    private static final double MAX_DEBT_RATIO = 0.40;

    public RegisterCreditApplicationService(
            AffiliateRepositoryPort affiliateRepository,
            CreditApplicationRepositoryPort creditApplicationRepository) {
        this.affiliateRepository = affiliateRepository;
        this.creditApplicationRepository = creditApplicationRepository;
    }

    @Override
    public CreditApplication register(CreditApplicationCommand command) {
        // Get affiliate from repository
        Affiliate affiliate = affiliateRepository.findById(command.affiliateId())
            .orElseThrow(() -> AffiliateNotFoundException.withId(command.affiliateId()));
        
        // Apply business validations using domain logic
        validateCreditApplication(affiliate, command);
        
        // Create application
        CreditApplication application = new CreditApplication();
        application.setAffiliate(affiliate);
        application.setRequestedAmount(command.requestedAmount());
        application.setTermMonths(command.termMonths());
        application.setStatus(CreditApplicationStatus.PENDING);
        application.setApplicationDate(LocalDateTime.now());
        
        // Validate payment affordability using domain method
        if (!application.isPaymentAffordable(affiliate.getMonthlySalary(), MAX_DEBT_RATIO)) {
            Double ratio = application.calculateMonthlyPayment() / affiliate.getMonthlySalary();
            throw UnaffordablePaymentException.forRatio(ratio, MAX_DEBT_RATIO);
        }
        
        // Persist through port
        return creditApplicationRepository.save(application);
    }

    private void validateCreditApplication(Affiliate affiliate, CreditApplicationCommand command) {
        // Use domain methods for validation
        if (!affiliate.isActive()) {
            throw InactiveAffiliateException.forAffiliate(affiliate.getId());
        }
        
        if (!affiliate.hasMinimumSeniority(MINIMUM_SENIORITY_MONTHS)) {
            throw InsufficientSeniorityException.forAffiliate(
                affiliate.getId(), MINIMUM_SENIORITY_MONTHS);
        }
        
        Double maxAmount = affiliate.getMaxCreditAmount();
        if (command.requestedAmount() > maxAmount) {
            throw ExcessiveCreditAmountException.forAmount(
                command.requestedAmount(), maxAmount);
        }
    }
}