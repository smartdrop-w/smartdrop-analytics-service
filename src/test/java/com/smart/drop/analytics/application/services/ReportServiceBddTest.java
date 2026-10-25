package com.smart.drop.analytics.application.services;

import com.smart.drop.analytics.domain.model.entities.Metric;
import com.smart.drop.analytics.domain.model.entities.Report;
import com.smart.drop.analytics.domain.model.repositories.ReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas BDD y Unitarias - Microservicio Analytics: Generacion de Reportes y Metricas")
class ReportServiceBddTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("US13 Escenario 1: Generacion exitosa de reporte analitico con metricas de eficiencia")
    void shouldCreateReportWithMetricsSuccessfully() {
        // Given
        when(reportRepository.saveReport(any(Report.class))).thenAnswer(inv -> {
            Report r = inv.getArgument(0);
            return new Report(301, r.userId(), r.title(), r.content(), r.type(), LocalDateTime.now(), List.of());
        });
        when(reportRepository.saveMetrics(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<ReportService.MetricDraft> drafts = List.of(
                new ReportService.MetricDraft("efficiencyScore", new BigDecimal("89"), "%"),
                new ReportService.MetricDraft("waterSaved", new BigDecimal("2450"), "L")
        );

        // When
        Report created = reportService.createReport(
                1001,
                "Efficiency Report - Kitchen",
                "Reporte mensual de ahorro hidrico",
                "Efficiency Report",
                drafts
        );

        // Then
        assertThat(created.reportId()).isEqualTo(301);
        assertThat(created.metrics()).hasSize(2);
        assertThat(created.metrics().get(0).name()).isEqualTo("efficiencyScore");
        verify(reportRepository, times(1)).saveReport(any(Report.class));
        verify(reportRepository, times(1)).saveMetrics(anyList());
    }

    @Test
    @DisplayName("US13 Escenario 2: Consulta de reporte existente por ID adjuntando sus metricas")
    void shouldRetrieveReportWithMetricsById() {
        // Given
        Report baseReport = new Report(301, 1001, "Usage Analytics", "Detalle", "Usage", LocalDateTime.now(), List.of());
        List<Metric> metrics = List.of(Metric.create(1001, 301, "waterSaved", new BigDecimal("1200"), "L"));

        when(reportRepository.findReportById(301)).thenReturn(Optional.of(baseReport));
        when(reportRepository.findMetricsByReportId(301)).thenReturn(metrics);

        // When
        Report result = reportService.getReportById(301);

        // Then
        assertThat(result.reportId()).isEqualTo(301);
        assertThat(result.metrics()).hasSize(1);
    }

    @Test
    @DisplayName("US13 Escenario 3: Rechazo al buscar un reporte inexistente")
    void shouldThrowExceptionWhenReportNotFound() {
        // Given
        when(reportRepository.findReportById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reportService.getReportById(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Report not found: 999");
    }
}
