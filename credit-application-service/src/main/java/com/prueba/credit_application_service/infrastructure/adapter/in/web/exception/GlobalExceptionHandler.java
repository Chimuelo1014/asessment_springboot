package com.prueba.credit_application_service.infrastructure.adapter.in.web.exception;

import com.prueba.credit_application_service.domain.exception.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler - Handles all exceptions with RFC 7807 ProblemDetail
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    
    private final MeterRegistry meterRegistry;
    
    // ==================== Domain Exceptions ====================
    
    @ExceptionHandler(AffiliateNotFoundException.class)
    public ProblemDetail handleAffiliateNotFound(AffiliateNotFoundException ex) {
        incrementErrorCounter("affiliate_not_found");
        log.error("Affiliate not found: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Affiliate Not Found");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/affiliate-not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    @ExceptionHandler(DuplicateDocumentException.class)
    public ProblemDetail handleDuplicateDocument(DuplicateDocumentException ex) {
        incrementErrorCounter("duplicate_document");
        log.error("Duplicate document: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Duplicate Document");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/duplicate-document"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    @ExceptionHandler(InactiveAffiliateException.class)
    public ProblemDetail handleInactiveAffiliate(InactiveAffiliateException ex) {
        incrementErrorCounter("inactive_affiliate");
        log.error("Inactive affiliate: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Inactive Affiliate");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/inactive-affiliate"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    @ExceptionHandler(InsufficientSeniorityException.class)
    public ProblemDetail handleInsufficientSeniority(InsufficientSeniorityException ex) {
        incrementErrorCounter("insufficient_seniority");
        log.error("Insufficient seniority: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Insufficient Seniority");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/insufficient-seniority"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    @ExceptionHandler(CreditApplicationNotFoundException.class)
    public ProblemDetail handleCreditApplicationNotFound(CreditApplicationNotFoundException ex) {
        incrementErrorCounter("credit_application_not_found");
        log.error("Credit application not found: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Credit Application Not Found");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/credit-application-not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    @ExceptionHandler(ExcessiveCreditAmountException.class)
    public ProblemDetail handleExcessiveCreditAmount(ExcessiveCreditAmountException ex) {
        incrementErrorCounter("excessive_credit_amount");
        log.error("Excessive credit amount: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Excessive Credit Amount");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/excessive-credit-amount"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    @ExceptionHandler(UnaffordablePaymentException.class)
    public ProblemDetail handleUnaffordablePayment(UnaffordablePaymentException ex) {
        incrementErrorCounter("unaffordable_payment");
        log.error("Unaffordable payment: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Unaffordable Payment");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/unaffordable-payment"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    // ==================== Validation Exceptions ====================
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        incrementErrorCounter("validation_error");
        log.error("Validation error: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/validation-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", errors);
        
        return problemDetail;
    }
    
    // ==================== Security Exceptions ====================
    
    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        incrementErrorCounter("bad_credentials");
        log.error("Bad credentials: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, "Invalid username or password");
        problemDetail.setTitle("Authentication Failed");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/bad-credentials"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
        incrementErrorCounter("authentication_failed");
        log.error("Authentication failed: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, "Authentication failed");
        problemDetail.setTitle("Authentication Failed");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/authentication-failed"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    // ==================== Generic Exceptions ====================
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        incrementErrorCounter("illegal_argument");
        log.error("Illegal argument: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Invalid Request");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/illegal-argument"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        incrementErrorCounter("illegal_state");
        log.error("Illegal state: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Invalid State");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/illegal-state"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        incrementErrorCounter("internal_server_error");
        log.error("Unexpected error: ", ex);
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.coopcredit.com/errors/internal-server-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
    
    // ==================== Metrics ====================
    
    private void incrementErrorCounter(String errorType) {
        Counter.builder("application_errors_total")
                .tag("error_type", errorType)
                .register(meterRegistry)
                .increment();
    }
}