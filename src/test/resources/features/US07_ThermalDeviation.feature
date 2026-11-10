@analytics @thermal @strategy
Feature: US07 - Liquid Temperature Deviation and Excursion Warning
  As a craft brewer
  I want the analytics system to monitor thermal telemetry in fermentation tanks
  So that batch temperature excursions trigger proactive alerts to prevent biological spoil.

  Background:
    Given the analytics strategy resolver is operational

  Scenario: Critical temperature excursion beyond safe fermentation threshold
    When a thermal evaluation is processed for liquid "BEER_WORT", temperature 25.5 Celsius, maximum allowed 21.0 Celsius, at hour 11
    Then the evaluation should trigger an alert
    And the alert severity should be "CRITICAL"
    And the resolved strategy name should be "ThermalExcursionStrategy"
    And the recommended action should indicate glycol recirculation activation

  Scenario: Moderate temperature excursion within high severity threshold
    When a thermal evaluation is processed for liquid "BEER_WORT", temperature 22.2 Celsius, maximum allowed 21.0 Celsius, at hour 15
    Then the evaluation should trigger an alert
    And the alert severity should be "HIGH"

  Scenario: Controlled fermentation temperature within tolerance
    When a thermal evaluation is processed for liquid "BEER_WORT", temperature 19.5 Celsius, maximum allowed 21.0 Celsius, at hour 10
    Then the evaluation should not trigger an alert
