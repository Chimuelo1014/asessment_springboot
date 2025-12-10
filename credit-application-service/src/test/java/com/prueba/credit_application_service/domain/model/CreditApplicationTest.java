package com.prueba.credit_application_service.domain.model;

import org.junit.jupiter.api.Test;
import org.assertj.core.data.Offset;

import static org.assertj.core.api.Assertions.assertThat;

class CreditApplicationTest {

    @Test
    void shouldCalculateMonthlyPaymentCorrectly() {
        // Given
        CreditApplication app = new CreditApplication();
        app.setRequestedAmount(10000000.0);
        app.setTermMonths(36);
        app.setInterestRate(12.5);

        // When
        Double payment = app.calculateMonthlyPayment();

        // Then
        // Mathematical result for 10M, 36 months, 12.5% annual is approx 334,536
        assertThat(payment).isCloseTo(334536.26, Offset.offset(100.0));
    }

    @Test
    void shouldCalculateWithoutInterest() {
        CreditApplication app = new CreditApplication();
        app.setRequestedAmount(12000000.0);
        app.setTermMonths(24);
        app.setInterestRate(0.0);

        Double payment = app.calculateMonthlyPayment();

        assertThat(payment).isEqualTo(500000.0);
    }
}
