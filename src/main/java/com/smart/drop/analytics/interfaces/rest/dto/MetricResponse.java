package com.smart.drop.analytics.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Detalle de una métrica registrada en el sistema de analítica")
public record MetricResponse(
        @Schema(description = "Identificador único de la métrica", example = "201")
        Integer metricId,

        @Schema(description = "Identificador del usuario", example = "2")
        Integer userId,

        @Schema(description = "Identificador del reporte asociado", example = "1")
        Integer reportId,

        @Schema(description = "Nombre de la métrica", example = "FLOW_RATE_MAX")
        String name,

        @Schema(description = "Valor cuantitativo", example = "4.25")
        BigDecimal value,

        @Schema(description = "Unidad física de medida", example = "L/min")
        String unit,

        @Schema(description = "Fecha y hora del registro", example = "2026-11-13T23:30:00")
        LocalDateTime recordedAt
) {
}
