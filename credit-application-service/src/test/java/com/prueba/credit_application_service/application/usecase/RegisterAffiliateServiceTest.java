package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.DuplicateDocumentException;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.enums.AffiliateStatus;
import com.prueba.credit_application_service.domain.port.in.RegisterAffiliateUseCase.AffiliateRegistrationCommand;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAffiliateServiceTest {

    @Mock
    private AffiliateRepositoryPort affiliateRepository;

    private RegisterAffiliateService registerAffiliateService;

    @BeforeEach
    void setUp() {
        registerAffiliateService = new RegisterAffiliateService(affiliateRepository);
    }

    @Test
    void shouldRegisterAffiliateSuccessfully() {
        // Given
        AffiliateRegistrationCommand command = new AffiliateRegistrationCommand(
                "123456789",
                "John Doe",
                "john@example.com",
                "555-1234",
                3000.0,
                null);

        when(affiliateRepository.existsByDocument("123456789")).thenReturn(false);
        when(affiliateRepository.save(any(Affiliate.class))).thenAnswer(invocation -> {
            Affiliate affiliate = invocation.getArgument(0);
            affiliate.setId(1L);
            return affiliate;
        });

        // When
        Affiliate result = registerAffiliateService.register(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDocument()).isEqualTo("123456789");
        assertThat(result.getFullName()).isEqualTo("John Doe");
        assertThat(result.getStatus()).isEqualTo(AffiliateStatus.ACTIVE);
        verify(affiliateRepository).existsByDocument("123456789");
        verify(affiliateRepository).save(any(Affiliate.class));
    }

    @Test
    void shouldThrowExceptionWhenDocumentAlreadyExists() {
        // Given
        AffiliateRegistrationCommand command = new AffiliateRegistrationCommand(
                "123456789",
                "John Doe",
                "john@example.com",
                "555-1234",
                3000.0,
                null);

        when(affiliateRepository.existsByDocument("123456789")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> registerAffiliateService.register(command))
                .isInstanceOf(DuplicateDocumentException.class)
                .hasMessageContaining("123456789");

        verify(affiliateRepository).existsByDocument("123456789");
        verify(affiliateRepository, never()).save(any(Affiliate.class));
    }
}