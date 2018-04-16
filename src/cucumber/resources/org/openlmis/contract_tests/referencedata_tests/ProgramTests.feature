@ProgramTests
Feature: Program tests

  Scenario: Admin user should be able to create programs
    Given I have logged in as admin

    When I try to create a program with name: malaria, code: mlr1
    Then I should get response with the created program's id

    When I try to get program with id
    Then I should get program with name: malaria, code: mlr1
