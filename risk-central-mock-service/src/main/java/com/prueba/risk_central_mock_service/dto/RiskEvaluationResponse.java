package com.prueba.risk_central_mock_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RiskEvaluationResponse - CORREGIDO según enunciado
 * 
 * Formato requerido:
 * {
 *   "documento": "1017654311",
 *   "score": 642,
 *   "nivelRiesgo": "MEDIO",
 *   "detalle": "Historial crediticio moderado."
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationResponse {
    private String documento;        // Cambiado de "document"
    private Integer score;           // 300-950
    private String nivelRiesgo;      // BAJO, MEDIO, ALTO (cambiado de "riskLevel")
    private String detalle;          // Cambiado de "message"
    // REMOVIDO: recommendation, evaluationDate (no están en el enunciado)
}