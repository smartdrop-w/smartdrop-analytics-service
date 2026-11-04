package com.smart.drop.analytics.interfaces.rest;

import com.smart.drop.analytics.domain.model.strategies.AlertEvaluationResult;
import com.smart.drop.analytics.domain.model.strategies.AlertStrategyResolver;
import com.smart.drop.analytics.interfaces.rest.dto.AlertEvaluationRequest;
import com.smart.drop.analytics.interfaces.rest.dto.AlertEvaluationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para evaluación dinámica de reglas y alertas algorítmicas mediante el Patrón Strategy.
 */
@Tag(name = "Alert Evaluation (Strategy Pattern)", description = "Evaluación dinámica de fugas nocturnas y excursiones térmicas según el dominio del líquido")
@RestController
@RequestMapping("/api/v1/analytics/alerts")
public class AlertEvaluationController {

    private final AlertStrategyResolver alertStrategyResolver;

    public AlertEvaluationController(AlertStrategyResolver alertStrategyResolver) {
        this.alertStrategyResolver = alertStrategyResolver;
    }

    @Operation(summary = "Evaluar métrica física contra reglas de alerta", description = "Despacha dinámicamente la estrategia adecuada (NightLeakDetectionStrategy para WATER, ThermalExcursionStrategy para BEER_WORT) retornando diagnóstico y severidad.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evaluación algorítmica completada", content = @Content(schema = @Schema(implementation = AlertEvaluationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de métrica o líquido inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/evaluate")
    public ResponseEntity<AlertEvaluationResponse> evaluate(@Valid @RequestBody AlertEvaluationRequest request) {
        AlertEvaluationResult result = alertStrategyResolver.evaluateMetric(
                request.liquidType(),
                request.metricValue(),
                request.threshold(),
                request.hourOfDay()
        );

        AlertEvaluationResponse response = new AlertEvaluationResponse(
                result.alertTriggered(),
                result.severity(),
                result.strategyName(),
                result.message(),
                result.recommendedAction(),
                result.evaluatedAt()
        );

        return ResponseEntity.ok(response);
    }
}
