@analytics @leaks @strategy
Feature: US05 - Nocturnal Water Leak Detection via Strategy Pattern
  As a facility or property manager
  I want the analytics engine to evaluate nocturnal flow and drop rates using the Strategy Pattern
  So that abnormal continuous consumption triggers a critical leak alert with mitigation instructions.

  Background:
    Given the analytics strategy resolver is operational

  Scenario: Continuous water flow drop detected during nocturnal resting hours
    When an evaluation request is processed for liquid "WATER", drop rate 8.5 cm per hour, threshold 5.0 cm per hour, at hour 3
    Then the evaluation should trigger an alert
    And the alert severity should be "CRITICAL"
    And the resolved strategy name should be "NightLeakDetectionStrategy"
    And the recommended action should indicate valve shutoff and pipe inspection

  Scenario: Normal water consumption during daytime hours is not classified as leak
    When an evaluation request is processed for liquid "WATER", drop rate 8.5 cm per hour, threshold 5.0 cm per hour, at hour 14
    Then the evaluation should not trigger an alert
    And the alert message should indicate regular daytime human consumption

  Scenario: Nocturnal monitoring within safe threshold
    When an evaluation request is processed for liquid "WATER", drop rate 3.2 cm per hour, threshold 5.0 cm per hour, at hour 2
    Then the evaluation should not trigger an alert
