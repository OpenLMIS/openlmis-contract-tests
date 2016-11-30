@RequisitionTests
Feature: Requisition Tests

  Scenario: StoreInCharge user should be able to approve a requisition
    Given I have logged in as StoreInCharge

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | e6799d64-d10d-4011-b8c2-0e4d4a3f65ce | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | e6799d64-d10d-4011-b8c2-0e4d4a3f65ce | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalConsumedQuantity | totalStockoutDays | requestedQuantity |
      | 0                     | 0                 | 0                 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalConsumedQuantity | totalStockoutDays | requestedQuantity |
      | 0                     | 0                 | 0                 |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status

    When I try to authorize a requisition
    Then I should get a requisition with "AUTHORIZED" status

    When I try update fields in requisition:
      | approvedQuantity |
      | 0                |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 0                |

    When I try to approve a requisition
    Then I should get a requisition with "APPROVED" status
    And I logout


  Scenario: StoreInCharge user should be able to reject authorized requisition
    Given I have logged in as StoreInCharge

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | e6799d64-d10d-4011-b8c2-0e4d4a3f65ce | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | e6799d64-d10d-4011-b8c2-0e4d4a3f65ce | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalConsumedQuantity | totalStockoutDays | requestedQuantity |
      | 0                     | 0                 | 0                 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalConsumedQuantity | totalStockoutDays | requestedQuantity |
      | 0                     | 0                 | 0                 |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status

    When I try to authorize a requisition
    Then I should get a requisition with "AUTHORIZED" status

    When I try to reject authorized requisition
    Then I should get a requisition with "INITIATED" status
    And I logout
