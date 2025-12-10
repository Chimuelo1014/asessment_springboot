package com.prueba.credit_application_service.infrastructure.adapter.in.web.controller;

import com.prueba.credit_application_service.application.service.AnalyticsService;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.response.analytics.ApprovalRateDTO;
import com.prueba.credit_application_service.infrastructure.adapter.in.web.dto.response.analytics.MonthlyDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Endpoints for dashboard analytics")
@PreAuthorize("hasRole('ADMIN')")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/monthly")
    @Operation(summary = "Get applications per month")
    public ResponseEntity<List<MonthlyDataDTO>> getMonthlyApplications() {
        return ResponseEntity.ok(analyticsService.getMonthlyApplications());
    }

    @GetMapping("/status")
    @Operation(summary = "Get distribution by status")
    public ResponseEntity<Map<String, Long>> getStatusDistribution() {
        return ResponseEntity.ok(analyticsService.getStatusDistribution());
    }

    @GetMapping("/approval-rate")
    @Operation(summary = "Get approval rate metrics")
    public ResponseEntity<ApprovalRateDTO> getApprovalRate() {
        return ResponseEntity.ok(analyticsService.getApprovalRate());
    }
}
