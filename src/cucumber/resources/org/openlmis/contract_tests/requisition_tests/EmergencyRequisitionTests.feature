@EmergencyRequisitionTests
Feature: Emergency Requisition Tests


  PrepareDatabase
  Scenario: Storeroom Manager user should be able to delete initiated emergency requisition
    When I have logged in as administrator
    And I try to get or create a period with current date and schedule 9c15bd6e-3f6b-4b91-b53a-36c199d35eac
    Then I should get response with the period id
    And I logout

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | true      |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | true      |
    And I should get a requisition with "INITIATED" status
    And I should get a requisition without supervisoryNode

    When I try to add products to requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 12                    | 20               | 8                 | 9                 | test                         | 32                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 12                    | 20               | 8                 | 9                 | test                         | 32                    |

    When I try to delete requisition
    Then I should get response of deleted requisition

    When I try to get requisition with id
    Then I should get response of not found
    And I logout


  Scenario: Program Supervisor user should be able to reject authorized emergency requisition
    When I have logged in as administrator
    And I try to get or create a period with current date and schedule 9c15bd6e-3f6b-4b91-b53a-36c199d35eac
    Then I should get response with the period id
    And I logout

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | true      |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | true      |
    And I should get a requisition with "INITIATED" status
    And I should get a requisition without supervisoryNode

    When I try to add products to requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 12                    | 20               | 8                 | 9                 | test                         | 32                    | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 12                    | 20               | 8                 | 9                 | test                         | 32                    |

    When I try to submit a requisition
    And I try to get requisition with id
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    And I try to authorize a requisition
    And I try to get requisition with id
    Then I should get a requisition with "AUTHORIZED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout

    When I have logged in as psupervisor
    When I try to reject authorized requisition
    And I try to get requisition with id
    Then I should get a requisition with "REJECTED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout

    When I have logged in as srmanager1
    And I try to delete requisition
    Then I should get response of deleted requisition

    When I try to get requisition with id
    Then I should get response of not found
    And I logout


  Scenario: Program Supervisor user should be able to approve several emergency requisitions in the same period
    When I have logged in as administrator
    And I try to get or create a period with current date and schedule 9c15bd6e-3f6b-4b91-b53a-36c199d35eac
    Then I should get response with the period id
    And I logout

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | true      |
    Then I should get response with the initiated requisition's id
    And I should get a requisition with "INITIATED" status
    And I should get a requisition without supervisoryNode

    When I try to add products to requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 1                     | 1                | 16                | 17                | test                         | 2                     | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 1                     | 1                | 16                | 17                | test                         | 2                     |

    When I try to submit a requisition
    And I try to get requisition with id
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    And I try to authorize a requisition
    And I try to get requisition with id
    Then I should get a requisition with "AUTHORIZED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout

    When I have logged in as psupervisor
    And I try update fields in requisition:
      | approvedQuantity | supervisoryNode                      |
      | 1                | 9f470265-0770-4dd1-bd5a-cf8fe3734d79 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 1                |
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "APPROVED" status
    And I logout

    When I have logged in as srmanager1
    When I try to initiate a requisition with:
      | programId                            | facilityId                           | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | true      |
    Then I should get response with the initiated requisition's id
    And I should get a requisition with "INITIATED" status

    When I try to add products to requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 2                     | 2                | 2                 | 2                 | test                         | 4                     | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 2                     | 2                | 2                 | 2                 | test                         | 4                     |

    When I try to submit a requisition
    And I try to get requisition with id
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    And I try to authorize a requisition
    And I try to get requisition with id
    Then I should get a requisition with "AUTHORIZED" status
    And I logout

    When I have logged in as psupervisor
    And I try update fields in requisition:
      | approvedQuantity |
      | 2                |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 2                |

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "APPROVED" status
    And I logout

  @PrepareDatabase
  Scenario: Storeroom Manager user should get failure response if date outside of period when he creates emergency requisition
    When I have logged in as administrator
    And I try to get or create a period with future date and schedule 9c15bd6e-3f6b-4b91-b53a-36c199d35eac
    Then I should get response with the period id

    When I try to get periods by program dce17f2e-af3e-40ad-8e00-3496adef44c3 and facility 176c4276-1fb1-4507-8ad2-cdfba0f47445
    And I try to delete current period
    Then I should get response with status 204
    And I logout

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | true      |
    Then I should get response of incorrect period
    And I logout
