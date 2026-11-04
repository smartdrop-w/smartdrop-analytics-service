package com.smart.drop.analytics.interfaces.rest;

import com.smart.drop.analytics.application.services.ReportService;
import com.smart.drop.analytics.domain.model.entities.Metric;
import com.smart.drop.analytics.domain.model.entities.Report;
import com.smart.drop.analytics.domain.model.repositories.ReportRepository;
import com.smart.drop.analytics.domain.model.strategies.AlertStrategyResolver;
import com.smart.drop.analytics.domain.model.strategies.NightLeakDetectionStrategy;
import com.smart.drop.analytics.domain.model.strategies.ThermalExcursionStrategy;
import com.smart.drop.analytics.interfaces.rest.dto.AlertEvaluationRequest;
import com.smart.drop.analytics.interfaces.rest.dto.AlertEvaluationResponse;
import com.smart.drop.analytics.interfaces.rest.dto.CreateMetricRequest;
import com.smart.drop.analytics.interfaces.rest.dto.MetricResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - AlertEvaluationController y Métricas de Reportes")
class AlertEvaluationAndReportMetricsTest {

    private AlertStrategyResolver alertStrategyResolver;
    private AlertEvaluationController alertEvaluationController;

    @Mock
    private ReportRepository reportRepository;

    private ReportService reportService;
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        alertStrategyResolver = new AlertStrategyResolver(List.of(
                new NightLeakDetectionStrategy(),
                new ThermalExcursionStrategy()
        ));
        alertEvaluationController = new AlertEvaluationController(alertStrategyResolver);

        reportService = new ReportService(reportRepository);
        reportController = new ReportController(reportService);
    }

    @Test
    @DisplayName("Debe evaluar fuga nocturna para WATER a las 3:00h y emitir alerta CRITICAL")
    void shouldEvaluateWaterNightLeakAlert() {
        // Arrange: Descenso de 8.5 cm/h supera umbral de 5.0 a las 3:00 AM
        AlertEvaluationRequest request = new AlertEvaluationRequest("WATER", 8.5, 5.0, 3);

        // Act
        ResponseEntity<AlertEvaluationResponse> response = alertEvaluationController.evaluate(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AlertEvaluationResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.alertTriggered()).isTrue();
        assertThat(body.severity()).isEqualTo("CRITICAL");
        assertThat(body.strategyName()).isEqualTo("NightLeakDetectionStrategy");
        assertThat(body.recommendedAction()).contains("Cerrar inmediatamente la válvula matriz");
    }

    @Test
    @DisplayName("Debe evaluar excursión térmica para BEER_WORT y alertar si la temperatura supera el umbral")
    void shouldEvaluateThermalExcursionAlert() {
        // Arrange: 24.5 °C en fermentación supera umbral de 20.0 °C
        AlertEvaluationRequest request = new AlertEvaluationRequest("BEER_WORT", 24.5, 20.0, 14);

        // Act
        ResponseEntity<AlertEvaluationResponse> response = alertEvaluationController.evaluate(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AlertEvaluationResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.alertTriggered()).isTrue();
        assertThat(body.strategyName()).isEqualTo("ThermalExcursionStrategy");
    }

    @Test
    @DisplayName("Debe obtener métricas de un reporte mediante GET /reports/{reportId}/metrics")
    void shouldGetMetricsForReport() {
        // Arrange
        Report report = new Report(1, 2, "Reporte 1", "Contenido", "DAILY", LocalDateTime.now(), List.of());
        when(reportRepository.findReportById(1)).thenReturn(Optional.of(report));
        Metric metric = new Metric(10, 2, 1, "FLOW_RATE", BigDecimal.valueOf(4.2), "L/min", LocalDateTime.now());
        when(reportRepository.findMetricsByReportId(1)).thenReturn(List.of(metric));

        // Act
        ResponseEntity<List<MetricResponse>> response = reportController.getMetricsByReportId(1);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).name()).isEqualTo("FLOW_RATE");
    }

    @Test
    @DisplayName("Debe agregar métrica a reporte existente mediante POST /reports/{reportId}/metrics")
    void shouldAddMetricToReport() {
        // Arrange
        Report report = new Report(1, 2, "Reporte 1", "Contenido", "DAILY", LocalDateTime.now(), List.of());
        when(reportRepository.findReportById(1)).thenReturn(Optional.of(report));
        Metric savedMetric = new Metric(20, 2, 1, "TEMPERATURE", BigDecimal.valueOf(18.5), "CELSIUS", LocalDateTime.now());
        when(reportRepository.saveMetrics(any())).thenReturn(List.of(savedMetric));

        CreateMetricRequest request = new CreateMetricRequest("TEMPERATURE", BigDecimal.valueOf(18.5), "CELSIUS");

        // Act
        ResponseEntity<MetricResponse> response = reportController.addMetricToReport(1, request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("TEMPERATURE");
        assertThat(response.getBody().value()).isEqualTo(BigDecimal.valueOf(18.5));
    }
}
