package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.AffiliateNotFoundException;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.enums.AffiliateStatus;
import com.prueba.credit_application_service.domain.port.in.UpdateAffiliateUseCase.UpdateAffiliateCommand;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;
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
class UpdateAffiliateServiceTest {

    @Mock
    private AffiliateRepositoryPort affiliateRepository;

    private UpdateAffiliateService updateService;

    @BeforeEach
    void setUp() {
        updateService = new UpdateAffiliateService(affiliateRepository);
    }

    @Test
    void shouldUpdateAffiliateSuccessfully() {
        // Given
        Affiliate existing = createAffiliate();
        UpdateAffiliateCommand command = new UpdateAffiliateCommand(
            1L, "Jane Smith", "jane@example.com", "555-9999", 6000.0
        );

        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(affiliateRepository.save(any(Affiliate.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        // When
        Affiliate result = updateService.update(command);

        // Then
        assertThat(result.getFullName()).isEqualTo("Jane Smith");
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        assertThat(result.getMonthlySalary()).isEqualTo(6000.0);
        verify(affiliateRepository).save(any(Affiliate.class));
    }

    @Test
    void shouldThrowExceptionWhenAffiliateNotFound() {
        // Given
        UpdateAffiliateCommand command = new UpdateAffiliateCommand(
            999L, "Jane Smith", "jane@example.com", null, null
        );
        when(affiliateRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> updateService.update(command))
            .isInstanceOf(AffiliateNotFoundException.class);
    }

    @Test
    void shouldNotUpdateNullFields() {
        // Given
        Affiliate existing = createAffiliate();
        UpdateAffiliateCommand command = new UpdateAffiliateCommand(
            1L, null, null, null, null
        );

        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(affiliateRepository.save(any(Affiliate.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        // When
        Affiliate result = updateService.update(command);

        // Then - valores originales deben mantenerse
        assertThat(result.getFullName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
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
}