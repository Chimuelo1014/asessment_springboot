package com.prueba.credit_application_service.infrastructure.adapter.out.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CORREGIDO - Campos en español según enunciado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationExternalResponse {
    private String documento;
    private Integer score;
    private String nivelRiesgo;  // BAJO, MEDIO, ALTO
    private String detalle;
}