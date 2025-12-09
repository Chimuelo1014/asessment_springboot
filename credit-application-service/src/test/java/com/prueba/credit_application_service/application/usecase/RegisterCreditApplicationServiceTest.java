package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.*;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.model.enums.AffiliateStatus;
import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import com.prueba.credit_application_service.domain.port.in.RegisterCreditApplicationUseCase.CreditApplicationCommand;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;
import com.prueba.credit_application_service.domain.port.out.CreditApplicationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterCreditApplicationServiceTest {

    @Mock
    private AffiliateRepositoryPort affiliateRepository;

    @Mock
    private CreditApplicationRepositoryPort creditApplicationRepository;

    private RegisterCreditApplicationService registerService;

    @BeforeEach
    void setUp() {
        registerService = new RegisterCreditApplicationService(
            affiliateRepository, 
            creditApplicationRepository
        );
    }

    @Test
    void shouldRegisterCreditApplicationSuccessfully() {
        // Given
        Affiliate affiliate = createActiveAffiliate();
        CreditApplicationCommand command = new CreditApplicationCommand(1L, 15000.0, 24);

        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(affiliate));
        when(creditApplicationRepository.save(any(CreditApplication.class)))
            .thenAnswer(invocation -> {
                CreditApplication app = invocation.getArgument(0);
                app.setId(1L);
                return app;
            });

        // When
        CreditApplication result = registerService.register(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(CreditApplicationStatus.PENDING);
        assertThat(result.getRequestedAmount()).isEqualTo(15000.0);
        verify(affiliateRepository).findById(1L);
        verify(creditApplicationRepository).save(any(CreditApplication.class));
    }

    @Test
    void shouldThrowExceptionWhenAffiliateNotFound() {
        // Given
        CreditApplicationCommand command = new CreditApplicationCommand(999L, 15000.0, 24);
        when(affiliateRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> registerService.register(command))
            .isInstanceOf(AffiliateNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenAffiliateIsInactive() {
        // Given
        Affiliate affiliate = createActiveAffiliate();
        affiliate.setStatus(AffiliateStatus.INACTIVE);
        CreditApplicationCommand command = new CreditApplicationCommand(1L, 15000.0, 24);

        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(affiliate));

        // When & Then
        assertThatThrownBy(() -> registerService.register(command))
            .isInstanceOf(InactiveAffiliateException.class);
    }

    @Test
    void shouldThrowExceptionWhenInsufficientSeniority() {
        // Given
        Affiliate affiliate = createActiveAffiliate();
        affiliate.setAffiliationDate(LocalDate.now().minusMonths(3)); // Less than 6 months
        CreditApplicationCommand command = new CreditApplicationCommand(1L, 15000.0, 24);

        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(affiliate));

        // When & Then
        assertThatThrownBy(() -> registerService.register(command))
            .isInstanceOf(InsufficientSeniorityException.class);
    }

    @Test
    void shouldThrowExceptionWhenExcessiveAmount() {
        // Given
        Affiliate affiliate = createActiveAffiliate();
        affiliate.setMonthlySalary(5000.0); // Max = 25,000
        CreditApplicationCommand command = new CreditApplicationCommand(1L, 30000.0, 24);

        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(affiliate));

        // When & Then
        assertThatThrownBy(() -> registerService.register(command))
            .isInstanceOf(ExcessiveCreditAmountException.class);
    }

    @Test
void shouldThrowExceptionWhenUnaffordablePayment() {
        // Given
        Affiliate affiliate = createActiveAffiliate();
        affiliate.setMonthlySalary(3000.0);

        // Salario = 3000
        // Máx crédito aproximado = 15000 (según tu dominio)
        // Pago mensual = 15000 / 12 = 1250
        // Ratio = 1250 / 3000 = 0.416 > 0.40 ✅

        CreditApplicationCommand command = new CreditApplicationCommand(1L, 15000.0, 12);

        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(affiliate));

        // When & Then
        assertThatThrownBy(() -> registerService.register(command))
            .isInstanceOf(UnaffordablePaymentException.class);
    }


    @Test
    void shouldAcceptMaximumDebtRatio() {
        // Given
        Affiliate affiliate = createActiveAffiliate();
        affiliate.setMonthlySalary(10000.0);
        // Monthly payment = 48000/12 = 4000
        // Ratio = 4000/10000 = 0.40 (exactly at limit)
        CreditApplicationCommand command = new CreditApplicationCommand(1L, 48000.0, 12);

        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(affiliate));
        when(creditApplicationRepository.save(any(CreditApplication.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        CreditApplication result = registerService.register(command);

        // Then
        assertThat(result).isNotNull();
        verify(creditApplicationRepository).save(any(CreditApplication.class));
    }

    private Affiliate createActiveAffiliate() {
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
}