package com.smart.drop.analytics.domain.model.repositories;

import com.smart.drop.analytics.domain.model.entities.Metric;
import com.smart.drop.analytics.domain.model.entities.Report;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Domain repository for reports and metrics.
 */
public interface ReportRepository {

    Report saveReport(Report report);

    Optional<Report> findReportById(Integer reportId);

    List<Report> findReportsByUserId(Integer userId);

    List<Report> findTop10ReportsByUserId(Integer userId);

    long countReportsByUserId(Integer userId);

    LocalDateTime findLatestReportGeneratedAtByUserId(Integer userId);

    List<Report> findAllReports();

    Metric saveMetric(Metric metric);

    List<Metric> saveMetrics(List<Metric> metrics);

    List<Metric> findMetricsByUserId(Integer userId);

    List<Metric> findTop10MetricsByUserId(Integer userId);

    long countMetricsByUserId(Integer userId);

    BigDecimal findAverageMetricValueByUserId(Integer userId);

    List<Metric> findMetricsByReportId(Integer reportId);
}
