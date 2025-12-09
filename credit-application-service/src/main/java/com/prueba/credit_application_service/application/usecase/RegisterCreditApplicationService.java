package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.*;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import com.prueba.credit_application_service.domain.port.in.RegisterCreditApplicationUseCase;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;
import com.prueba.credit_application_service.domain.port.out.CreditApplicationRepositoryPort;

import java.time.LocalDateTime;

public class RegisterCreditApplicationService implements RegisterCreditApplicationUseCase {

    private final AffiliateRepositoryPort affiliateRepository;
    private final CreditApplicationRepositoryPort creditApplicationRepository;

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
        Affiliate affiliate = affiliateRepository.findById(command.affiliateId())
                .orElseThrow(() -> AffiliateNotFoundException.withId(command.affiliateId()));

        validateCreditApplication(affiliate, command);

        CreditApplication application = new CreditApplication();
        application.setAffiliate(affiliate);
        application.setRequestedAmount(command.requestedAmount());
        application.setTermMonths(command.termMonths());
        application.setInterestRate(command.interestRate()); // NUEVO
        application.setStatus(CreditApplicationStatus.PENDING);
        application.setApplicationDate(LocalDateTime.now());

        if (!application.isPaymentAffordable(affiliate.getMonthlySalary(), MAX_DEBT_RATIO)) {
            Double ratio = application.calculateMonthlyPayment() / affiliate.getMonthlySalary();
            throw UnaffordablePaymentException.forRatio(ratio, MAX_DEBT_RATIO);
        }

        return creditApplicationRepository.save(application);
    }

    private void validateCreditApplication(Affiliate affiliate, CreditApplicationCommand command) {
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