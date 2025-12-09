package com.prueba.credit_application_service.infrastructure.adapter.in.web.controller;

import com.coopcredit.creditapp.domain.model.Affiliate;
import com.coopcredit.creditapp.domain.port.in.GetAffiliateUseCase;
import com.coopcredit.creditapp.domain.port.in.RegisterAffiliateUseCase;
import com.coopcredit.creditapp.infrastructure.adapter.in.web.dto.AffiliateRequest;
import com.coopcredit.creditapp.infrastructure.adapter.in.web.dto.AffiliateResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AffiliateController - INFRASTRUCTURE LAYER (Web Adapter)
 * 
 * This controller:
 * 1. Receives HTTP requests
 * 2. Validates input
 * 3. Calls use cases (through PORTS)
 * 4. Converts domain objects to DTOs
 * 5. Returns HTTP responses
 * 
 * It depends on USE CASE INTERFACES (ports), NOT implementations
 */
@RestController
@RequestMapping("/api/v1/affiliates")
public class AffiliateController {

    private static final Logger log = LoggerFactory.getLogger(AffiliateController.class);
    
    // Dependencies are USE CASE INTERFACES (ports from domain)
    private final RegisterAffiliateUseCase registerUseCase;
    private final GetAffiliateUseCase getUseCase;

    public AffiliateController(RegisterAffiliateUseCase registerUseCase, 
                              GetAffiliateUseCase getUseCase) {
        this.registerUseCase = registerUseCase;
        this.getUseCase = getUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    public ResponseEntity<AffiliateResponse> createAffiliate(
            @Valid @RequestBody AffiliateRequest request) {
        
        log.info("Creating new affiliate with document: {}", request.getDocument());
        
        // Convert DTO to command (domain input)
        RegisterAffiliateUseCase.AffiliateRegistrationCommand command =
            new RegisterAffiliateUseCase.AffiliateRegistrationCommand(
                request.getDocument(),
                request.getFullName(),
                request.getEmail(),
                request.getPhone(),
                request.getMonthlySalary()
            );
        
        // Call use case through PORT
        Affiliate affiliate = registerUseCase.register(command);
        
        // Convert domain to DTO (presentation)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapToResponse(affiliate));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AFILIADO', 'ANALISTA', 'ADMIN')")
    public ResponseEntity<AffiliateResponse> getAffiliate(@PathVariable Long id) {
        log.debug("Getting affiliate: {}", id);
        
        // Call use case through PORT
        Affiliate affiliate = getUseCase.getById(id);
        
        // Convert domain to DTO
        return ResponseEntity.ok(mapToResponse(affiliate));
    }

    @GetMapping("/document/{document}")
    @PreAuthorize("hasAnyRole('AFILIADO', 'ANALISTA', 'ADMIN')")
    public ResponseEntity<AffiliateResponse> getByDocument(@PathVariable String document) {
        log.debug("Getting affiliate by document: {}", document);
        
        Affiliate affiliate = getUseCase.getByDocument(document);
        return ResponseEntity.ok(mapToResponse(affiliate));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    public ResponseEntity<List<AffiliateResponse>> getAllAffiliates() {
        log.debug("Getting all affiliates");
        
        List<Affiliate> affiliates = getUseCase.getAll();
        List<AffiliateResponse> response = affiliates.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Maps domain object to DTO
     * This is ADAPTER responsibility, keeping domain clean
     */
    private AffiliateResponse mapToResponse(Affiliate affiliate) {
        AffiliateResponse response = new AffiliateResponse();
        response.setId(affiliate.getId());
        response.setDocument(affiliate.getDocument());
        response.setFullName(affiliate.getFullName());
        response.setEmail(affiliate.getEmail());
        response.setPhone(affiliate.getPhone());
        response.setMonthlySalary(affiliate.getMonthlySalary());
        response.setStatus(affiliate.getStatus().name());
        response.setAffiliationDate(affiliate.getAffiliationDate().toString());
        return response;
    }
}
