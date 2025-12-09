package com.prueba.credit_application_service.infrastructure.adapter.in.web.controller;

import com.prueba.credit_application_service.domain.model.Affiliate;
import com.prueba.credit_application_service.domain.port.in.GetAffiliateUseCase;
import com.prueba.credit_application_service.domain.port.in.RegisterAffiliateUseCase;
import com.prueba.credit_application_service.domain.port.in.UpdateAffiliateUseCase;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.AffiliateRequest;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.AffiliateResponse;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.AffiliateUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/affiliates")
@Tag(name = "Affiliates", description = "Affiliate management endpoints")
public class AffiliateController {

    private static final Logger log = LoggerFactory.getLogger(AffiliateController.class);

    private final RegisterAffiliateUseCase registerUseCase;
    private final GetAffiliateUseCase getUseCase;
    private final UpdateAffiliateUseCase updateUseCase; // NUEVO

    public AffiliateController(RegisterAffiliateUseCase registerUseCase,
            GetAffiliateUseCase getUseCase,
            UpdateAffiliateUseCase updateUseCase) {
        this.registerUseCase = registerUseCase;
        this.getUseCase = getUseCase;
        this.updateUseCase = updateUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    @Operation(summary = "Create new affiliate", description = "Register a new affiliate in the system")
    public ResponseEntity<AffiliateResponse> createAffiliate(
            @Valid @RequestBody AffiliateRequest request) {

        log.info("Creating new affiliate with document: {}", request.getDocument());

        RegisterAffiliateUseCase.AffiliateRegistrationCommand command = 
            new RegisterAffiliateUseCase.AffiliateRegistrationCommand(
                request.getDocument(),
                request.getFullName(),
                request.getEmail(),
                request.getPhone(),
                request.getMonthlySalary());

        Affiliate affiliate = registerUseCase.register(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToResponse(affiliate));
    }

    @PutMapping("/{id}") // NUEVO ENDPOINT
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    @Operation(summary = "Update affiliate", description = "Update basic information of an affiliate")
    public ResponseEntity<AffiliateResponse> updateAffiliate(
            @PathVariable Long id,
            @Valid @RequestBody AffiliateUpdateRequest request) {

        log.info("Updating affiliate: {}", id);

        UpdateAffiliateUseCase.UpdateAffiliateCommand command = 
            new UpdateAffiliateUseCase.UpdateAffiliateCommand(
                id,
                request.getFullName(),
                request.getEmail(),
                request.getPhone(),
                request.getMonthlySalary());

        Affiliate affiliate = updateUseCase.update(command);

        return ResponseEntity.ok(mapToResponse(affiliate));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AFILIADO', 'ANALISTA', 'ADMIN')")
    @Operation(summary = "Get affiliate by ID")
    public ResponseEntity<AffiliateResponse> getAffiliate(@PathVariable Long id) {
        log.debug("Getting affiliate: {}", id);
        Affiliate affiliate = getUseCase.getById(id);
        return ResponseEntity.ok(mapToResponse(affiliate));
    }

    @GetMapping("/document/{document}")
    @PreAuthorize("hasAnyRole('AFILIADO', 'ANALISTA', 'ADMIN')")
    @Operation(summary = "Get affiliate by document")
    public ResponseEntity<AffiliateResponse> getByDocument(@PathVariable String document) {
        log.debug("Getting affiliate by document: {}", document);
        Affiliate affiliate = getUseCase.getByDocument(document);
        return ResponseEntity.ok(mapToResponse(affiliate));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ANALISTA', 'ADMIN')")
    @Operation(summary = "Get all affiliates")
    public ResponseEntity<List<AffiliateResponse>> getAllAffiliates() {
        log.debug("Getting all affiliates");
        List<Affiliate> affiliates = getUseCase.getAll();
        List<AffiliateResponse> response = affiliates.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

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