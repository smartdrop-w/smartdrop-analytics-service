package com.smart.drop.analytics.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Resultado del diagnóstico emitido por el Patrón Strategy")
public record AlertEvaluationResponse(
        @Schema(description = "Indica si se activó una condición de alerta", example = "true")
        boolean alertTriggered,

        @Schema(description = "Nivel de severidad asignado (NONE, WARNING, CRITICAL)", example = "CRITICAL")
        String severity,

        @Schema(description = "Nombre de la estrategia algorítmica aplicada", example = "NightLeakDetectionStrategy")
        String strategyName,

        @Schema(description = "Diagnóstico detallado y causa detectada", example = "Fuga nocturna detectada a las 03:00h: descenso de 7.80 cm/h supera el umbral límite de 5.00 cm/h (+2.80 cm/h).")
        String message,

        @Schema(description = "Acción operativa recomendada", example = "Cerrar inmediatamente la válvula matriz de paso e inspeccionar tuberías empotradas y flotadores de inodoros.")
        String recommendedAction,

        @Schema(description = "Marca de tiempo de la evaluación algorítmica", example = "2026-11-13T23:45:00")
        LocalDateTime evaluatedAt
) {
}
