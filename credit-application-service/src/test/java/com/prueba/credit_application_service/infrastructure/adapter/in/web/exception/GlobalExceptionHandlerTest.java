package com.prueba.credit_application_service.infrastructure.adapter.in.web.exception;

import com.prueba.credit_application_service.domain.exception.AffiliateNotFoundException;
import com.prueba.credit_application_service.domain.exception.DuplicateDocumentException;
import com.prueba.credit_application_service.domain.exception.InactiveAffiliateException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleNotFoundException() {
        // Given
        AffiliateNotFoundException exception = AffiliateNotFoundException.withId(999L);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/affiliates/999");

        // When
        ResponseEntity<ProblemDetail> response = handler.handleNotFoundException(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetail()).contains("999");
        assertThat(response.getBody().getInstance()).isNotNull();
    }

    @Test
    void shouldHandleDuplicateException() {
        // Given
        DuplicateDocumentException exception = DuplicateDocumentException.forDocument("123456789");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/affiliates");

        // When
        ResponseEntity<ProblemDetail> response = handler.handleDuplicateException(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInstance()).isNotNull();
    }

    @Test
    void shouldHandleBusinessRuleException() {
        // Given
        InactiveAffiliateException exception = InactiveAffiliateException.forAffiliate(1L);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/credit-applications");

        // When
        ResponseEntity<ProblemDetail> response = handler.handleBusinessRuleExceptions(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInstance()).isNotNull();
    }
}