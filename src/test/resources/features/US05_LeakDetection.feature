@analytics @leaks
Feature: US05 - Nocturnal Water Leak Detection
  As a facility manager
  I want the analytics engine to evaluate nocturnal telemetry using the Strategy Pattern
  So that abnormal baseline consumption triggers an automatic leak alert.

  Background:
    Given the nocturnal monitoring window is active between 01:00 AM and 05:00 AM

  Scenario: Continuous flow detected during nocturnal resting hours
    Given sensor "FLOW-001" reports continuous flow of "4.5" liters/minute for 3 consecutive intervals
    When the "NocturnalLeakDetectionStrategy" evaluates the telemetry buffer
    Then an alert of severity "HIGH" and type "NOCTURNAL_LEAK" should be emitted
    And an Outbox event should be registered for delivery
