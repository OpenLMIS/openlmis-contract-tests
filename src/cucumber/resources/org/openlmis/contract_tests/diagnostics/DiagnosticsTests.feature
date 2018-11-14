@DiagnosticsTests
Feature: User Tests


  Scenario: Administrator should be able to see system health status
    Given I have logged in as administrator

    When I try to retrieve system health status
    Then I should get response with current system health status
    And I logout


  Scenario: System health status should be un-authenticated
    Given I am not logged in

    When I try to retrieve system health status
    Then I should get response with current system health status
