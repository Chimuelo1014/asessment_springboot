package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.CreditApplicationNotFoundException;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.CreditApplication;
import com.prueba.credit_application_service.domain.model.enums.AffiliateStatus;
import com.prueba.credit_application_service.domain.model.enums.CreditApplicationStatus;
import com.prueba.credit_application_service.domain.port.out.CreditApplicationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCreditApplicationServiceTest {

    @Mock
    private CreditApplicationRepositoryPort creditApplicationRepository;

    private GetCreditApplicationService getCreditApplicationService;

    @BeforeEach
    void setUp() {
        getCreditApplicationService = new GetCreditApplicationService(creditApplicationRepository);
    }

    @Test
    void shouldGetApplicationById() {
        // Given
        CreditApplication application = createCreditApplication(1L, CreditApplicationStatus.PENDING);
        when(creditApplicationRepository.findById(1L)).thenReturn(Optional.of(application));

        // When
        CreditApplication result = getCreditApplicationService.getById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(creditApplicationRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenApplicationNotFound() {
        // Given
        when(creditApplicationRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> getCreditApplicationService.getById(999L))
            .isInstanceOf(CreditApplicationNotFoundException.class)
            .hasMessageContaining("999");
    }

    @Test
    void shouldGetAllApplications() {
        // Given
        List<CreditApplication> applications = Arrays.asList(
            createCreditApplication(1L, CreditApplicationStatus.PENDING),
            createCreditApplication(2L, CreditApplicationStatus.APPROVED),
            createCreditApplication(3L, CreditApplicationStatus.REJECTED)
        );
        when(creditApplicationRepository.findAll()).thenReturn(applications);

        // When
        List<CreditApplication> result = getCreditApplicationService.getAll();

        // Then
        assertThat(result).hasSize(3);
        verify(creditApplicationRepository).findAll();
    }

    @Test
    void shouldGetApplicationsByAffiliateId() {
        // Given
        List<CreditApplication> applications = Arrays.asList(
            createCreditApplication(1L, CreditApplicationStatus.PENDING),
            createCreditApplication(2L, CreditApplicationStatus.APPROVED)
        );
        when(creditApplicationRepository.findByAffiliateId(1L)).thenReturn(applications);

        // When
        List<CreditApplication> result = getCreditApplicationService.getByAffiliateId(1L);

        // Then
        assertThat(result).hasSize(2);
        verify(creditApplicationRepository).findByAffiliateId(1L);
    }

    @Test
    void shouldGetApplicationsByStatus() {
        // Given
        List<CreditApplication> pendingApps = Arrays.asList(
            createCreditApplication(1L, CreditApplicationStatus.PENDING),
            createCreditApplication(2L, CreditApplicationStatus.PENDING)
        );
        when(creditApplicationRepository.findByStatus(CreditApplicationStatus.PENDING))
            .thenReturn(pendingApps);

        // When
        List<CreditApplication> result = getCreditApplicationService
            .getByStatus(CreditApplicationStatus.PENDING);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(app -> 
            app.getStatus() == CreditApplicationStatus.PENDING);
        verify(creditApplicationRepository).findByStatus(CreditApplicationStatus.PENDING);
    }

    @Test
    void shouldReturnEmptyListWhenNoApplicationsForAffiliate() {
        // Given
        when(creditApplicationRepository.findByAffiliateId(999L)).thenReturn(Arrays.asList());

        // When
        List<CreditApplication> result = getCreditApplicationService.getByAffiliateId(999L);

        // Then
        assertThat(result).isEmpty();
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

    private CreditApplication createCreditApplication(Long id, CreditApplicationStatus status) {
        CreditApplication app = new CreditApplication();
        app.setId(id);
        app.setAffiliate(createAffiliate());
        app.setRequestedAmount(10000.0);
        app.setTermMonths(12);
        app.setStatus(status);
        app.setApplicationDate(LocalDateTime.now());
        return app;
    }
}