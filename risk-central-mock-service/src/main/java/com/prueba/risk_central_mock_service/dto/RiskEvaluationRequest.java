package com.prueba.risk_central_mock_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RiskEvaluationRequest - CORREGIDO según enunciado
 * 
 * Formato requerido:
 * {
 *   "documento": "string",
 *   "monto": 5000000,
 *   "plazo": 36
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationRequest {
    @NotBlank(message = "Documento es requerido")
    private String documento;  // Cambiado de "document"
    
    @Positive(message = "Monto debe ser positivo")
    private Double monto;      // Cambiado de "requestedAmount"
    
    @Positive(message = "Plazo debe ser positivo")
    private Integer plazo;     // Cambiado de "monthlyIncome"
    
    // REMOVIDO: fullName (no está en el enunciado)
}