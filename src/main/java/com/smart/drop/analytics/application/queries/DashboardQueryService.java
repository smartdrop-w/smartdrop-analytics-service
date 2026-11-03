package com.smart.drop.analytics.application.queries;

import com.smart.drop.analytics.domain.model.repositories.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardQueryService {

    private final ReportRepository reportRepository;

    public DashboardQueryService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public DashboardReadModel getDashboardByUserId(Integer userId) {
        long reportsCount = reportRepository.countReportsByUserId(userId);
        long metricsCount = reportRepository.countMetricsByUserId(userId);
        BigDecimal averageMetricValue = reportRepository.findAverageMetricValueByUserId(userId);
        LocalDateTime lastReportGeneratedAt = reportRepository.findLatestReportGeneratedAtByUserId(userId);

        List<DashboardReadModel.MetricCard> recentMetrics = reportRepository.findTop10MetricsByUserId(userId).stream()
                .map(metric -> new DashboardReadModel.MetricCard(
                        metric.metricId(),
                        metric.reportId(),
                        metric.name(),
                        metric.value(),
                        metric.unit(),
                        metric.recordedAt()
                ))
                .toList();

        List<DashboardReadModel.ReportCard> recentReports = reportRepository.findTop10ReportsByUserId(userId).stream()
                .map(report -> new DashboardReadModel.ReportCard(
                        report.reportId(),
                        report.title(),
                        report.type(),
                        report.generatedAt(),
                        report.metrics().size()
                ))
                .toList();

        return new DashboardReadModel(
                userId,
                (int) reportsCount,
                (int) metricsCount,
                averageMetricValue != null ? averageMetricValue : BigDecimal.ZERO,
                lastReportGeneratedAt,
                recentMetrics,
                recentReports
        );
    }
}
