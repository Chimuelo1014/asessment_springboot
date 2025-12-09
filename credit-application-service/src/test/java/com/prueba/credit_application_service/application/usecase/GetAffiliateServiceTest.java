package com.prueba.credit_application_service.application.usecase;

import com.prueba.credit_application_service.domain.exception.AffiliateNotFoundException;
import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.enums.AffiliateStatus;
import com.prueba.credit_application_service.domain.port.out.AffiliateRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAffiliateServiceTest {

    @Mock
    private AffiliateRepositoryPort affiliateRepository;

    private GetAffiliateService getAffiliateService;

    @BeforeEach
    void setUp() {
        getAffiliateService = new GetAffiliateService(affiliateRepository);
    }

    @Test
    void shouldGetAffiliateById() {
        // Given
        Affiliate affiliate = createAffiliate(1L, "123456789", "John Doe");
        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(affiliate));

        // When
        Affiliate result = getAffiliateService.getById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDocument()).isEqualTo("123456789");
        verify(affiliateRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAffiliateNotFoundById() {
        // Given
        when(affiliateRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> getAffiliateService.getById(999L))
            .isInstanceOf(AffiliateNotFoundException.class)
            .hasMessageContaining("999");
    }

    @Test
    void shouldGetAffiliateByDocument() {
        // Given
        Affiliate affiliate = createAffiliate(1L, "123456789", "John Doe");
        when(affiliateRepository.findByDocument("123456789")).thenReturn(Optional.of(affiliate));

        // When
        Affiliate result = getAffiliateService.getByDocument("123456789");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDocument()).isEqualTo("123456789");
        verify(affiliateRepository).findByDocument("123456789");
    }

    @Test
    void shouldThrowExceptionWhenAffiliateNotFoundByDocument() {
        // Given
        when(affiliateRepository.findByDocument("999999999")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> getAffiliateService.getByDocument("999999999"))
            .isInstanceOf(AffiliateNotFoundException.class)
            .hasMessageContaining("999999999");
    }

    @Test
    void shouldGetAllAffiliates() {
        // Given
        List<Affiliate> affiliates = Arrays.asList(
            createAffiliate(1L, "111111111", "John Doe"),
            createAffiliate(2L, "222222222", "Jane Smith"),
            createAffiliate(3L, "333333333", "Bob Johnson")
        );
        when(affiliateRepository.findAll()).thenReturn(affiliates);

        // When
        List<Affiliate> result = getAffiliateService.getAll();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result).extracting(Affiliate::getDocument)
            .containsExactly("111111111", "222222222", "333333333");
        verify(affiliateRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoAffiliatesExist() {
        // Given
        when(affiliateRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Affiliate> result = getAffiliateService.getAll();

        // Then
        assertThat(result).isEmpty();
        verify(affiliateRepository).findAll();
    }

    private Affiliate createAffiliate(Long id, String document, String fullName) {
        Affiliate affiliate = new Affiliate();
        affiliate.setId(id);
        affiliate.setDocument(document);
        affiliate.setFullName(fullName);
        affiliate.setEmail(fullName.toLowerCase().replace(" ", ".") + "@example.com");
        affiliate.setMonthlySalary(5000.0);
        affiliate.setStatus(AffiliateStatus.ACTIVE);
        affiliate.setAffiliationDate(LocalDate.now().minusYears(1));
        return affiliate;
    }
}