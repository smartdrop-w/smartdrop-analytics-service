package com.smart.drop.analytics.bdd.steps;

import com.smart.drop.analytics.application.queries.DashboardQueryService;
import com.smart.drop.analytics.application.queries.DashboardReadModel;
import com.smart.drop.analytics.application.services.ReportService;
import com.smart.drop.analytics.domain.model.entities.Metric;
import com.smart.drop.analytics.domain.model.entities.Report;
import com.smart.drop.analytics.domain.model.strategies.AlertEvaluationResult;
import com.smart.drop.analytics.domain.model.strategies.AlertStrategyResolver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnalyticsBddSteps {

    @Autowired
    private AlertStrategyResolver alertStrategyResolver;

    @Autowired
    private ReportService reportService;

    @Autowired
    private DashboardQueryService dashboardQueryService;

    private Integer userId = 2001;
    private AlertEvaluationResult evaluationResult;
    private Report currentReport;
    private Metric attachedMetric;
    private DashboardReadModel dashboardModel;

    @Given("the analytics strategy resolver is operational")
    public void theAnalyticsStrategyResolverIsOperational() {
        assertThat(alertStrategyResolver).isNotNull();
    }

    @Given("the analytics report service is operational")
    public void theAnalyticsReportServiceIsOperational() {
        assertThat(reportService).isNotNull();
        assertThat(dashboardQueryService).isNotNull();
    }

    @Given("the user has ID {int}")
    public void theUserHasId(int id) {
        this.userId = id;
    }

    @When("an evaluation request is processed for liquid {string}, drop rate {double} cm per hour, threshold {double} cm per hour, at hour {int}")
    public void anEvaluationRequestIsProcessedForLiquid(String liquidType, double dropRate, double threshold, int hourOfDay) {
        evaluationResult = alertStrategyResolver.evaluateMetric(liquidType, dropRate, threshold, hourOfDay);
    }

    @Then("the evaluation should trigger an alert")
    public void theEvaluationShouldTriggerAnAlert() {
        assertThat(evaluationResult).isNotNull();
        assertThat(evaluationResult.alertTriggered()).isTrue();
    }

    @Then("the evaluation should not trigger an alert")
    public void theEvaluationShouldNotTriggerAnAlert() {
        assertThat(evaluationResult).isNotNull();
        assertThat(evaluationResult.alertTriggered()).isFalse();
    }

    @And("the alert severity should be {string}")
    public void theAlertSeverityShouldBe(String expectedSeverity) {
        assertThat(evaluationResult.severity()).isEqualTo(expectedSeverity);
    }

    @And("the resolved strategy name should be {string}")
    public void theResolvedStrategyNameShouldBe(String expectedStrategyName) {
        assertThat(evaluationResult.strategyName()).isEqualTo(expectedStrategyName);
    }

    @And("the recommended action should indicate valve shutoff and pipe inspection")
    public void theRecommendedActionShouldIndicateValveShutoffAndPipeInspection() {
        assertThat(evaluationResult.recommendedAction()).containsIgnoringCase("válvula matriz");
    }

    @And("the alert message should indicate regular daytime human consumption")
    public void theAlertMessageShouldIndicateRegularDaytimeHumanConsumption() {
        assertThat(evaluationResult.message()).containsIgnoringCase("consumo observado se clasifica como uso humano regular");
    }

    @When("a thermal evaluation is processed for liquid {string}, temperature {double} Celsius, maximum allowed {double} Celsius, at hour {int}")
    public void aThermalEvaluationIsProcessed(String liquidType, double temperature, double maxAllowed, int hourOfDay) {
        evaluationResult = alertStrategyResolver.evaluateMetric(liquidType, temperature, maxAllowed, hourOfDay);
    }

    @And("the recommended action should indicate glycol recirculation activation")
    public void theRecommendedActionShouldIndicateGlycolRecirculationActivation() {
        assertThat(evaluationResult.recommendedAction()).containsIgnoringCase("glicol");
    }

    @When("the analyst creates a report titled {string} with description {string}")
    public void theAnalystCreatesAReport(String title, String description) {
        currentReport = reportService.createReport(userId, title, description, "OPERATIONAL", List.of());
    }

    @Then("the report should be created successfully")
    public void theReportShouldBeCreatedSuccessfully() {
        assertThat(currentReport).isNotNull();
        assertThat(currentReport.reportId()).isNotNull();
    }

    @And("the report title should be {string}")
    public void theReportTitleShouldBe(String expectedTitle) {
        assertThat(currentReport.title()).isEqualTo(expectedTitle);
    }

    @Given("an existing report titled {string}")
    public void anExistingReportTitled(String title) {
        currentReport = reportService.createReport(userId, title, "Initial description for testing", "QUALITY", List.of());
    }

    @When("the analyst attaches a metric named {string} with value {double} and unit {string}")
    public void theAnalystAttachesAMetric(String metricName, double value, String unit) {
        ReportService.MetricDraft draft = new ReportService.MetricDraft(metricName, BigDecimal.valueOf(value), unit);
        attachedMetric = reportService.addMetricToReport(currentReport.reportId(), draft);
    }

    @Then("the metric should be recorded under the report")
    public void theMetricShouldBeRecordedUnderTheReport() {
        assertThat(attachedMetric).isNotNull();
        assertThat(attachedMetric.metricId()).isNotNull();
    }

    @And("retrieving metrics for the report should include {string} with value {double}")
    public void retrievingMetricsForTheReportShouldInclude(String metricName, double expectedValue) {
        List<Metric> metrics = reportService.getMetricsByReportId(currentReport.reportId());
        assertThat(metrics).anyMatch(m -> m.name().equals(metricName) && m.value().compareTo(BigDecimal.valueOf(expectedValue)) == 0);
    }

    @When("the user requests the operational dashboard for user ID {int}")
    public void theUserRequestsTheOperationalDashboardForUserId(int requestUserId) {
        dashboardModel = dashboardQueryService.getDashboardByUserId(requestUserId);
    }

    @Then("the dashboard summary should return valid KPIs and metrics")
    public void theDashboardSummaryShouldReturnValidKpisAndMetrics() {
        assertThat(dashboardModel).isNotNull();
        assertThat(dashboardModel.userId()).isEqualTo(userId);
    }
}
