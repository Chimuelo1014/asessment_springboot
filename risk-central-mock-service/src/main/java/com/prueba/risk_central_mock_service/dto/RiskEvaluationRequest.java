package com.prueba.risk_central_mock_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CORREGIDO según enunciado - campos en español
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationRequest {
    @NotBlank(message = "Documento es requerido")
    private String documento;
    
    @Positive(message = "Monto debe ser positivo")
    private Double monto;
    
    @Positive(message = "Plazo debe ser positivo")
    private Integer plazo;
}