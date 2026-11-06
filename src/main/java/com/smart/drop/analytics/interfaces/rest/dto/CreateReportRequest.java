package com.smart.drop.analytics.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Solicitud para generar un nuevo reporte analítico")
public record CreateReportRequest(
        @Schema(description = "Identificador del usuario", example = "2")
        @NotNull(message = "userId is required")
        Integer userId,

        @Schema(description = "Título descriptivo del reporte", example = "Auditoría de Consumo Semanal - Cervecería")
        @NotBlank(message = "title is required")
        String title,

        @Schema(description = "Contenido o resumen ejecutivo del reporte", example = "El consumo semanal se mantuvo en 840L con eficiencia de macerado al 94%.")
        String content,

        @Schema(description = "Tipo de reporte (CONSUMPTION_EFFICIENCY, LEAK_AUDIT, THERMAL_PROFILE)", example = "CONSUMPTION_EFFICIENCY")
        @NotBlank(message = "type is required")
        String type,

        @Schema(description = "Lista inicial de métricas asociadas al reporte")
        List<CreateMetricRequest> metrics
) {
}
