package com.smart.drop.analytics.interfaces.rest;

import com.smart.drop.analytics.application.services.ReportService;
import com.smart.drop.analytics.domain.model.entities.Metric;
import com.smart.drop.analytics.domain.model.entities.Report;
import com.smart.drop.analytics.interfaces.rest.dto.CreateMetricRequest;
import com.smart.drop.analytics.interfaces.rest.dto.CreateReportRequest;
import com.smart.drop.analytics.interfaces.rest.dto.MetricResponse;
import com.smart.drop.analytics.interfaces.rest.dto.ReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para generación y consulta de reportes analíticos y sus métricas asociadas.
 */
@Tag(name = "Analytics Reports & Metrics", description = "Endpoints para generación de reportes analíticos y gestión de sus métricas cuantitativas")
@RestController
@RequestMapping("/api/v1/analytics/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Crear nuevo reporte analítico", description = "Genera un reporte analítico con su contenido, tipología y colección inicial de métricas físicas.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente", content = @Content(schema = @Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de reporte inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ReportResponse> create(@Valid @RequestBody CreateReportRequest request) {
        Report created = reportService.createReport(
                request.userId(),
                request.title(),
                request.content(),
                request.type(),
                request.metrics() == null
                        ? List.of()
                        : request.metrics().stream()
                        .map(metric -> new ReportService.MetricDraft(metric.name(), metric.value(), metric.unit()))
                        .toList()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Operation(summary = "Obtener reporte por ID", description = "Recupera un reporte analítico completo junto a sus métricas vinculadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte encontrado", content = @Content(schema = @Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getById(@PathVariable Integer reportId) {
        Report report = reportService.getReportById(reportId);
        return ResponseEntity.ok(toResponse(report));
    }

    @Operation(summary = "Listar reportes analíticos", description = "Retorna todos los reportes, con opción de filtrar por el identificador de usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colección de reportes recuperada")
    })
    @GetMapping
    public ResponseEntity<List<ReportResponse>> getAll(@RequestParam(required = false) Integer userId) {
        List<ReportResponse> data = (userId == null ? reportService.getAllReports() : reportService.getReportsByUserId(userId))
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Listar métricas de un reporte", description = "Recupera todas las métricas cuantitativas asignadas a un reporte específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Métricas recuperadas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/{reportId}/metrics")
    public ResponseEntity<List<MetricResponse>> getMetricsByReportId(@PathVariable Integer reportId) {
        List<MetricResponse> metrics = reportService.getMetricsByReportId(reportId).stream()
                .map(this::toMetricResponse)
                .toList();
        return ResponseEntity.ok(metrics);
    }

    @Operation(summary = "Agregar métrica a reporte existente", description = "Añade una nueva métrica cuantitativa (valor y unidad) al reporte especificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Métrica agregada exitosamente", content = @Content(schema = @Schema(implementation = MetricResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de métrica inválidos"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @PostMapping("/{reportId}/metrics")
    public ResponseEntity<MetricResponse> addMetricToReport(@PathVariable Integer reportId,
                                                            @Valid @RequestBody CreateMetricRequest request) {
        Metric metric = reportService.addMetricToReport(reportId, new ReportService.MetricDraft(request.name(), request.value(), request.unit()));
        return ResponseEntity.status(HttpStatus.CREATED).body(toMetricResponse(metric));
    }

    private ReportResponse toResponse(Report report) {
        return new ReportResponse(
                report.reportId(),
                report.userId(),
                report.title(),
                report.content(),
                report.type(),
                report.generatedAt(),
                report.metrics().stream().map(this::toMetricResponse).toList()
        );
    }

    private MetricResponse toMetricResponse(Metric metric) {
        return new MetricResponse(
                metric.metricId(),
                metric.userId(),
                metric.reportId(),
                metric.name(),
                metric.value(),
                metric.unit(),
                metric.recordedAt()
        );
    }
}
