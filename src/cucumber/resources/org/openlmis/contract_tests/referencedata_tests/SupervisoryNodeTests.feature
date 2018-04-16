@SupervisoryNodeTests
Feature: Supervisory Node tests

  Scenario: Admin user should be able to create supervisory nodes
    Given I have logged in as admin

    When I try to create a supervisoryNode with:
      | facilityId                           | code  | name                |
      | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | SN001 | Supervisory Node #1 |
    Then I should get response with the created supervisoryNode's id

    When I try to get supervisoryNode with id
    Then I should get supervisoryNode with:
      | facilityId                           | code  | name                |
      | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | SN001 | Supervisory Node #1 |

  Scenario: Admin user should be able to delete supervisory nodes
    Given I have logged in as admin

    When I try to create a supervisoryNode with:
      | facilityId                           | code  | name                |
      | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | SN002 | Supervisory Node #2 |
    Then I should get response with the created supervisoryNode's id

    When I try to delete a supervisoryNode with id
    Then I should get no content response

    When I try to get supervisoryNode with id
    Then I should get not found response
