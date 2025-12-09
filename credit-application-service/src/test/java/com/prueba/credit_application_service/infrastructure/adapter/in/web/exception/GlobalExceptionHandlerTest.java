package com.prueba.credit_application_service.infrastructure.adapter.in.web.exception;

import com.prueba.credit_application_service.domain.exception.AffiliateNotFoundException;
import com.prueba.credit_application_service.domain.exception.DuplicateDocumentException;
import com.prueba.credit_application_service.domain.exception.InactiveAffiliateException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleNotFoundException() {
        // Given
        AffiliateNotFoundException exception = 
            AffiliateNotFoundException.withId(999L);

        // When
        ResponseEntity<ProblemDetail> response = handler.handleNotFoundException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetail()).contains("999");
    }

    @Test
    void shouldHandleDuplicateException() {
        // Given
        DuplicateDocumentException exception = 
            DuplicateDocumentException.forDocument("123456789");

        // When
        ResponseEntity<ProblemDetail> response = handler.handleDuplicateException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldHandleBusinessRuleException() {
        // Given
        InactiveAffiliateException exception = 
            InactiveAffiliateException.forAffiliate(1L);

        // When
        ResponseEntity<ProblemDetail> response = handler.handleBusinessRuleExceptions(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }
}