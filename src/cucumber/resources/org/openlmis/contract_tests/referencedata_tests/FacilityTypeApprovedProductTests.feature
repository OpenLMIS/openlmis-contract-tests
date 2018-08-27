@FacilityTypeApprovedProductTests
Feature: Facility Type Approved Product Tests


  Scenario: Admin user should be able to create FTAP
    Given I have logged in as admin

    When I try to create a FTAP:
    | orderable.id                            | program.id                              | facilityType.id                         | maxPeriodsOfStock | minPeriodsOfStock | emergencyOrderPoint |
    | 2400e410-b8dd-4954-b1c0-80d8a8e785fc    | dce17f2e-af3e-40ad-8e00-3496adef44c3    | ac1d268b-ce10-455f-bf87-9c667da8f060    | 3                 | 1                 | 1                   |
    Then I should get response with created FTAP's id
    And I logout

  Scenario: Admin user should be able to remove FTAP
    Given I have logged in as admin

    When I try to get all FTAPs with health_center facility type
    Then I should get first returned FTAP's id

    When I try to delete created FTAP
    Then I should get response of deleted FTAP
    And I logout

  Scenario: Admin user should be able to recreate FTAP
    Given I have logged in as admin

    When I try to get all FTAPs with health_center facility type
    Then I should get first returned FTAP's id

    When I try to delete created FTAP
    Then I should get response of deleted FTAP

    When I try to create a FTAP:
    | orderable.id                            | program.id                              | facilityType.id                         | maxPeriodsOfStock | minPeriodsOfStock | emergencyOrderPoint |
    | bac90734-6e58-426e-849b-331286ec77d2    | 66032ea8-b69b-4102-a1eb-844e57143187    | 5fc213c6-1bd7-46f0-9883-57c05250ca90    | 6                 | 4                 | 4                   |
    Then I should get response with created FTAP's id
    And I logout
