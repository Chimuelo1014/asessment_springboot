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
        CreditApplicationCommand command = new CreditApplicationCommand(
            1L, 15000.0, 24, 12.5); // CON tasa de interés

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
        assertThat(result.getInterestRate()).isEqualTo(12.5); // VERIFICAR
        verify(affiliateRepository).findById(1L);
        verify(creditApplicationRepository).save(any(CreditApplication.class));
    }

    @Test
    void shouldThrowExceptionWhenAffiliateNotFound() {
        // Given
        CreditApplicationCommand command = new CreditApplicationCommand(
            999L, 15000.0, 24, 12.5);
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
        CreditApplicationCommand command = new CreditApplicationCommand(
            1L, 15000.0, 24, 12.5);

        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(affiliate));

        // When & Then
        assertThatThrownBy(() -> registerService.register(command))
            .isInstanceOf(InactiveAffiliateException.class);
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