@storeroom_manager
Feature: Storeroom Manager
  Scenario Outline: Storeroom Manager user should be able to initiate a requisition
    Given I have logged in as admin

    When I try to initiate a requisition with "<programId>", "<facilityId>", "<periodId>", <emergency>
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with "<programId>", "<facilityId>", "<periodId>", <emergency>, "<status>"

  Examples:
  | programId                            | facilityId                           | periodId                             | emergency | status    |
  | dce17f2e-af3e-40ad-8e00-3496adef44c3 | e6799d64-d10d-4011-b8c2-0e4d4a3f65ce | 516ac930-0d28-49f5-a178-64764e22b236 | false     | INITIATED |
