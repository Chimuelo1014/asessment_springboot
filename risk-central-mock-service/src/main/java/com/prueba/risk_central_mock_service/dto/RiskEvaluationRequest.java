package com.prueba.risk_central_mock_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationRequest {
    @NotBlank(message = "Document is required")
    private String document;
    
    private String fullName;
    private Double requestedAmount;
    private Double monthlyIncome;
}
