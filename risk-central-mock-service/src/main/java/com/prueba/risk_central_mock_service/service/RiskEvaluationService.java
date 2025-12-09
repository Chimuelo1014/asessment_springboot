package com.prueba.risk_central_mock_service.service;

import com.prueba.risk_central_mock_service.dto.RiskEvaluationRequest;
import com.prueba.risk_central_mock_service.dto.RiskEvaluationResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * CORREGIDO según enunciado:
 * - Score: 300-950 (no 300-850)
 * - ALTO RIESGO: 300-500
 * - MEDIO RIESGO: 501-700
 * - BAJO RIESGO: 701-950
 * - Mismo documento = mismo score (determinístico usando hash)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RiskEvaluationService {

    private final MeterRegistry meterRegistry;

    public RiskEvaluationResponse evaluateRisk(RiskEvaluationRequest request) {
        log.info("Evaluando riesgo para documento: {}", request.getDocumento());
        
        // Usar hash del documento como seed para consistencia
        int seed = request.getDocumento().hashCode();
        Random random = new Random(seed);
        
        // Generar score entre 300 y 950 (651 posibles valores)
        int score = 300 + random.nextInt(651);
        
        // Determinar nivel según enunciado
        String nivelRiesgo = determineRiskLevel(score);
        String detalle = generateDetailMessage(score, nivelRiesgo);
        
        // Registrar métrica
        Counter.builder("risk_evaluations_total")
            .tag("nivel_riesgo", nivelRiesgo)
            .register(meterRegistry)
            .increment();
        
        log.info("Evaluación completada - Documento: {}, Score: {}, Nivel: {}", 
            request.getDocumento(), score, nivelRiesgo);
        
        return RiskEvaluationResponse.builder()
            .documento(request.getDocumento())
            .score(score)
            .nivelRiesgo(nivelRiesgo)
            .detalle(detalle)
            .build();
    }

    /**
     * CORREGIDO según enunciado:
     * - 300-500 → ALTO
     * - 501-700 → MEDIO  
     * - 701-950 → BAJO
     */
    private String determineRiskLevel(int score) {
        if (score >= 701) {
            return "BAJO";
        } else if (score >= 501) {
            return "MEDIO";
        } else {
            return "ALTO";
        }
    }

    private String generateDetailMessage(int score, String nivelRiesgo) {
        return switch (nivelRiesgo) {
            case "BAJO" -> String.format("Excelente historial crediticio (Score: %d). Cliente confiable con bajo riesgo de impago.", score);
            case "MEDIO" -> String.format("Historial crediticio moderado (Score: %d). Requiere análisis adicional antes de aprobar.", score);
            case "ALTO" -> String.format("Historial crediticio deficiente (Score: %d). Alto riesgo de impago, se recomienda rechazar.", score);
            default -> "No se pudo determinar el nivel de riesgo";
        };
    }
}