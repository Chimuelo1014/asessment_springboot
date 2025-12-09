package com.prueba.credit_application_service.infrastructure.adapter.out.persistence.mapper;

import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.model.enums.AffiliateStatus;
import com.prueba.credit_application_service.infrastructure.adapter.out.persistence.entity.AffiliateEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class AffiliateMapperTest {

    private AffiliateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AffiliateMapper();
    }

    @Test
    void shouldMapEntityToDomain() {
        // Given
        AffiliateEntity entity = new AffiliateEntity();
        entity.setId(1L);
        entity.setDocument("123456789");
        entity.setFullName("John Doe");
        entity.setEmail("john@example.com");
        entity.setPhone("555-1234");
        entity.setMonthlySalary(5000.0);
        entity.setStatus(AffiliateStatus.ACTIVE);
        entity.setAffiliationDate(LocalDate.now());

        // When
        Affiliate domain = mapper.toDomain(entity);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getDocument()).isEqualTo("123456789");
        assertThat(domain.getFullName()).isEqualTo("John Doe");
        assertThat(domain.getStatus()).isEqualTo(AffiliateStatus.ACTIVE);
    }

    @Test
    void shouldMapDomainToEntity() {
        // Given
        Affiliate domain = new Affiliate();
        domain.setId(1L);
        domain.setDocument("123456789");
        domain.setFullName("John Doe");
        domain.setEmail("john@example.com");
        domain.setMonthlySalary(5000.0);
        domain.setStatus(AffiliateStatus.ACTIVE);
        domain.setAffiliationDate(LocalDate.now());

        // When
        AffiliateEntity entity = mapper.toEntity(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getDocument()).isEqualTo("123456789");
    }

    @Test
    void shouldHandleNullEntity() {
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void shouldHandleNullDomain() {
        assertThat(mapper.toEntity(null)).isNull();
    }
}
