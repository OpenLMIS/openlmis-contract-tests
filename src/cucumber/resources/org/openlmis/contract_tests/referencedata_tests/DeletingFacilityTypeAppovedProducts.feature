@FacilityTypeApprovedProductTests
Feature: Deleting facility type approved products

  Scenario: Administrator should be able to remove FTAP
    Given I have logged in as admin

    When I try to get first FTAP with health_center facility type
    Then I should get first returned FTAP's id

    When I try to delete created FTAP
    Then I should get response of deleted FTAP
    And I logout