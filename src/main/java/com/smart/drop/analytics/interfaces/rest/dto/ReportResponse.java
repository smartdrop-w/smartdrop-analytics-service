package com.smart.drop.analytics.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Detalle completo de reporte analítico con métricas cuantitativas")
public record ReportResponse(
        @Schema(description = "Identificador único del reporte", example = "1")
        Integer reportId,

        @Schema(description = "Identificador del usuario", example = "2")
        Integer userId,

        @Schema(description = "Título del reporte", example = "Auditoría de Consumo Semanal - Cervecería")
        String title,

        @Schema(description = "Contenido analítico y diagnóstico", example = "El consumo semanal se mantuvo en 840L con eficiencia de macerado al 94%.")
        String content,

        @Schema(description = "Tipo de reporte", example = "CONSUMPTION_EFFICIENCY")
        String type,

        @Schema(description = "Fecha y hora de generación", example = "2026-11-13T23:30:00")
        LocalDateTime generatedAt,

        @Schema(description = "Colección de métricas cuantitativas incluidas")
        List<MetricResponse> metrics
) {
}
