package com.prueba.credit_application_service.infrastructure.adapter.in.web.controller;

import com.coopcredit.creditapp.domain.model.CreditApplication;
import com.coopcredit.creditapp.domain.model.enums.CreditApplicationStatus;
import com.coopcredit.creditapp.domain.port.in.EvaluateCreditApplicationUseCase;
import com.coopcredit.creditapp.domain.port.in.GetCreditApplicationUseCase;
import com.coopcredit.creditapp.domain.port.in.RegisterCreditApplicationUseCase;
import com.coopcredit.creditapp.infrastructure.adapter.in.web.dto.CreditApplicationRequest;
import com.coopcredit.creditapp.infrastructure.adapter.in.web.dto.CreditApplicationResponse;
import com.coopcredit.creditapp.infrastructure.adapter.in.web.dto.RiskEvaluationResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CreditApplicationController - INFRASTRUCTURE LAYER (Web Adapter)
 * 
 * Depends on USE CASE INTERFACES (ports), NOT implementations
 */
@RestController
@RequestMapping("/api/v1/credit-applications")
public class CreditApplicationController {

    private static final Logger log = LoggerFactory.getLogger(CreditApplicationController.class);
    
    // Dependencies are USE CASE INTERFACES (domain ports)
    private final RegisterCreditApplicationUseCase registerUseCase;
    private final EvaluateCreditApplicationUseCase evaluateUseCase;
    private final GetCreditApplicationUseCase getUseCase;
    private final MeterRegistry meterRegistry;

    public CreditApplicationController(
            RegisterCreditApplicationUseCase registerUseCase,
            EvaluateCreditApplicationUseCase evaluateUseCase,
            GetCreditApplicationUseCase getUseCase,
            MeterRegistry meterRegistry) {
        this.registerUseCase = registerUseCase;
        this.evaluateUseCase = evaluateUseCase;
        this.getUseCase = getUseCase;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    @PreAuthorize("hasRole('AFILIADO')")
    public ResponseEntity<CreditApplicationResponse> createApplication(
            @Valid @RequestBody CreditApplicationRequest request) {
        
        log.info("Creating credit application for affiliate: {}", request.getAffiliateId());
        
        Counter.builder("credit_applications_created_total")
            .register(meterRegistry)
            .increment();
        
        // Convert DTO to command
        RegisterCreditApplicationUseCase.CreditApplicationCommand command =
            new RegisterCreditApplicationUseCase.CreditApplicationCommand(
                request.getAffiliateId(),
                request.getRequestedAmount(),
                request.getTermMonths()
            );
        
        // Call use case through PORT
        CreditApplication application = registerUseCase.register(command);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapToResponse(application));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    public ResponseEntity<CreditApplicationResponse> getApplication(@PathVariable Long id) {
        log.debug("Getting credit application: {}", id);
        
        CreditApplication application = getUseCase.getById(id);
        return ResponseEntity.ok(mapToResponse(application));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    public ResponseEntity<List<CreditApplicationResponse>> getAllApplications() {
        log.debug("Getting all credit applications");
        
        List<CreditApplication> applications = getUseCase.getAll();
        List<CreditApplicationResponse> response = applications.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    public ResponseEntity<List<CreditApplicationResponse>> getPendingApplications() {
        log.debug("Getting pending credit applications");
        
        List<CreditApplication> applications = getUseCase.getByStatus(
            CreditApplicationStatus.PENDING);
        List<CreditApplicationResponse> response = applications.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('AFILIADO')")
    public ResponseEntity<List<CreditApplicationResponse>> getMyApplications(
            @RequestParam Long affiliateId) {
        log.debug("Getting applications for affiliate: {}", affiliateId);
        
        List<CreditApplication> applications = getUseCase.getByAffiliateId(affiliateId);
        List<CreditApplicationResponse> response = applications.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/evaluate")
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    public ResponseEntity<CreditApplicationResponse> evaluateApplication(@PathVariable Long id) {
        log.info("Evaluating credit application: {}", id);
        
        Counter.builder("credit_applications_evaluated_total")
            .register(meterRegistry)
            .increment();
        
        CreditApplication application = evaluateUseCase.evaluate(id);
        
        return ResponseEntity.ok(mapToResponse(application));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    public ResponseEntity<CreditApplicationResponse> approveApplication(
            @PathVariable Long id,
            @RequestParam(required = false) String comments) {
        
        log.info("Manually approving application: {}", id);
        
        CreditApplication application = evaluateUseCase.approveManually(id, comments);
        
        return ResponseEntity.ok(mapToResponse(application));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    public ResponseEntity<CreditApplicationResponse> rejectApplication(
            @PathVariable Long id,
            @RequestParam(required = false) String comments) {
        
        log.info("Manually rejecting application: {}", id);
        
        CreditApplication application = evaluateUseCase.rejectManually(id, comments);
        
        return ResponseEntity.ok(mapToResponse(application));
    }

    /**
     * Maps domain object to DTO
     */
    private CreditApplicationResponse mapToResponse(CreditApplication app) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        CreditApplicationResponse response = new CreditApplicationResponse();
        response.setId(app.getId());
        response.setAffiliateId(app.getAffiliate().getId());
        response.setAffiliateName(app.getAffiliate().getFullName());
        response.setRequestedAmount(app.getRequestedAmount());
        response.setTermMonths(app.getTermMonths());
        response.setStatus(app.getStatus().name());
        response.setApplicationDate(app.getApplicationDate().format(formatter));
        response.setEvaluationDate(
            app.getEvaluationDate() != null ? app.getEvaluationDate().format(formatter) : null
        );
        response.setAnalystComments(app.getAnalystComments());
        
        if (app.getRiskEvaluation() != null) {
            RiskEvaluationResponse riskResponse = new RiskEvaluationResponse();
            riskResponse.setScore(app.getRiskEvaluation().getScore());
            riskResponse.setRiskLevel(app.getRiskEvaluation().getRiskLevel().name());
            riskResponse.setRecommendation(app.getRiskEvaluation().getRecommendation());
            riskResponse.setMessage(app.getRiskEvaluation().getEvaluationMessage());
            response.setRiskEvaluation(riskResponse);
        }
        
        return response;
    }
}
