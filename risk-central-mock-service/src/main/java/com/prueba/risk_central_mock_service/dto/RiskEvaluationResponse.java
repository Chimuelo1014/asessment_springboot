package com.prueba.risk_central_mock_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CORREGIDO según enunciado
 * Formato: { "documento", "score", "nivelRiesgo", "detalle" }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationResponse {
    private String documento;
    private Integer score;        // 300-950
    private String nivelRiesgo;   // BAJO, MEDIO, ALTO
    private String detalle;
}