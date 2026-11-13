@analytics @reports @metrics
Feature: US06 - Analytic Reports, Metric Tracking, and Operational Dashboard
  As a facility manager or brewery analyst
  I want to generate analytical reports, attach quantitative metrics, and consult dashboard summaries
  So that operational performance and liquid consumption KPIs can be reviewed reliably.

  Background:
    Given the analytics report service is operational
    And the user has ID 2001

  Scenario: Generating a new analytical report
    When the analyst creates a report titled "Auditoria Mensual de Consumo" with description "Balance hidrico de noviembre"
    Then the report should be created successfully
    And the report title should be "Auditoria Mensual de Consumo"

  Scenario: Appending operational metrics to an existing report
    Given an existing report titled "Control de Calidad Cerveza"
    When the analyst attaches a metric named "Eficiencia Hidrica" with value 94.5 and unit "%"
    Then the metric should be recorded under the report
    And retrieving metrics for the report should include "Eficiencia Hidrica" with value 94.5

  Scenario: Consulting dashboard operational KPIs
    When the user requests the operational dashboard for user ID 2001
    Then the dashboard summary should return valid KPIs and metrics
