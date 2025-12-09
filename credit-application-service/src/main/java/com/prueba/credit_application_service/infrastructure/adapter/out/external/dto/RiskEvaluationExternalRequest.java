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
public class RiskEvaluationExternalRequest {
    private String documento;
    private Double monto;
    private Integer plazo;
}