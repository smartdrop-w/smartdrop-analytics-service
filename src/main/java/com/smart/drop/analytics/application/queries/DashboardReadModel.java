package com.smart.drop.analytics.application.queries;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo de lectura optimizado para el panel analítico (CQRS Read Model).
 */
@Schema(description = "Resumen consolidado para el panel de control analítico del cliente")
public record DashboardReadModel(
        @Schema(description = "Identificador del usuario", example = "2")
        Integer userId,

        @Schema(description = "Total de reportes generados", example = "8")
        long totalReports,

        @Schema(description = "Total de métricas físicas registradas", example = "34")
        long totalMetrics,

        @Schema(description = "Promedio general de valor de métricas", example = "18.42")
        BigDecimal averageMetricValue,

        @Schema(description = "Fecha del reporte más reciente", example = "2026-11-13T23:30:00")
        LocalDateTime lastReportGeneratedAt,

        @Schema(description = "Tarjetas de métricas recientes")
        List<MetricCard> recentMetrics,

        @Schema(description = "Tarjetas de reportes recientes")
        List<ReportCard> recentReports
) {
    @Schema(description = "Tarjeta resumida de métrica")
    public record MetricCard(
            @Schema(description = "ID de la métrica", example = "201")
            Integer metricId,

            @Schema(description = "ID del reporte", example = "1")
            Integer reportId,

            @Schema(description = "Nombre", example = "FLOW_RATE_MAX")
            String name,

            @Schema(description = "Valor", example = "4.25")
            BigDecimal value,

            @Schema(description = "Unidad", example = "L/min")
            String unit,

            @Schema(description = "Fecha de registro", example = "2026-11-13T23:30:00")
            LocalDateTime recordedAt
    ) {
    }

    @Schema(description = "Tarjeta resumida de reporte")
    public record ReportCard(
            @Schema(description = "ID del reporte", example = "1")
            Integer reportId,

            @Schema(description = "Título", example = "Auditoría Semanal")
            String title,

            @Schema(description = "Tipo", example = "CONSUMPTION_EFFICIENCY")
            String type,

            @Schema(description = "Fecha", example = "2026-11-13T23:30:00")
            LocalDateTime generatedAt,

            @Schema(description = "Cantidad de métricas", example = "5")
            int metricCount
    ) {
    }
}
