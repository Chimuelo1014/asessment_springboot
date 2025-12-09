package com.prueba.risk_central_mock_service.service;

import com.prueba.risk_central_mock_service.dto.RiskEvaluationRequest;
import com.prueba.risk_central_mock_service.dto.RiskEvaluationResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * RiskEvaluationService - CORREGIDO según enunciado
 * 
 * Reglas:
 * - Score: 300-950 (no 300-850)
 * - ALTO RIESGO: 300-500
 * - MEDIO RIESGO: 501-700
 * - BAJO RIESGO: 701-950
 * - Mismo documento = mismo score (determinístico)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RiskEvaluationService {

    private final MeterRegistry meterRegistry;

    public RiskEvaluationResponse evaluateRisk(RiskEvaluationRequest request) {
        log.info("Evaluando riesgo para documento: {}", request.getDocumento());
        
        // CORREGIDO: Usar documento como seed para consistencia
        int seed = request.getDocumento().hashCode();
        Random random = new Random(seed);
        
        // CORREGIDO: Generar score entre 300 y 950 (no 850)
        int score = 300 + random.nextInt(651); // 651 para cubrir 300-950
        
        // CORREGIDO: Determinar nivel según enunciado
        String nivelRiesgo = determineRiskLevel(score);
        String detalle = generateDetailMessage(score, nivelRiesgo);
        
        // Métricas
        Counter.builder("risk_evaluations_total")
            .tag("nivel_riesgo", nivelRiesgo)
            .register(meterRegistry)
            .increment();
        
        log.info("Evaluación completada - Documento: {}, Score: {}, Nivel: {}", 
            request.getDocumento(), score, nivelRiesgo);
        
        // CORREGIDO: Respuesta según formato del enunciado
        return RiskEvaluationResponse.builder()
            .documento(request.getDocumento())
            .score(score)
            .nivelRiesgo(nivelRiesgo)
            .detalle(detalle)
            .build();
    }

    /**
     * CORREGIDO según enunciado:
     * - 300-500 → ALTO RIESGO
     * - 501-700 → MEDIO RIESGO  
     * - 701-950 → BAJO RIESGO
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

    /**
     * CORREGIDO: Generar mensaje de detalle según nivel
     */
    private String generateDetailMessage(int score, String nivelRiesgo) {
        return switch (nivelRiesgo) {
            case "BAJO" -> String.format("Excelente historial crediticio (Score: %d). Cliente confiable.", score);
            case "MEDIO" -> String.format("Historial crediticio moderado (Score: %d). Requiere análisis adicional.", score);
            case "ALTO" -> String.format("Historial crediticio deficiente (Score: %d). Alto riesgo de impago.", score);
            default -> "No se pudo determinar el nivel de riesgo";
        };
    }
}