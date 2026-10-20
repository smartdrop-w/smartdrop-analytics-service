@analytics @thermal
Feature: US07 - Liquid Temperature Deviation and Excursion Warning
  As a craft brewer
  I want the system to monitor thermal telemetry against target thresholds
  So that batch fermentation temperature excursions trigger proactive alerts.

  Background:
    Given tank "FERM-02" has liquid type "BEER_FERMENTATION" with target temperature 18.0 Celsius and tolerance +/- 2.0 Celsius

  Scenario: Temperature excursion beyond safe threshold
    When temperature probe "TEMP-002" reports a reading of "24.5" Celsius
    Then the "ThermalExcursionStrategy" should classify the reading as "CRITICAL_EXCURSION"
    And a thermal warning should be recorded with difference "+4.5 C"
