@EmergencyRequisitionTests
Feature: Emergency Requisition Tests


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

    When I try to delete initiated requisition
    Then I should get response of deleted requisition

    When I try to get requisition with id
    Then I should get response of not found
    And I logout


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


  Scenario: Program Supervisor user should be able to approve emergency requisition
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

    When I try to add products to requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 15                    | 15               | 6                 | 7                 | test                         | 30                    | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 15                    | 15               | 6                 | 7                 | test                         | 30                    |

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
      | 10               |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 10               |

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "APPROVED" status
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


  Scenario: Program Supervisor user should be able to approve second emergency requisition in the same period
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
    And I logout

    When I have logged in as psupervisor
    And I try update fields in requisition:
      | approvedQuantity |
      | 1                |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 1                |

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


  Scenario: It should not be possible to update supervisory node in existing emergency requisition
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
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    |

    When I try to submit a requisition
    And I try to get requisition with id
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    Given I have logged in as smanager1
    When I try to authorize a requisition
    And I try to get requisition with id
    Then I should get a requisition with "AUTHORIZED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout

    Given I have logged in as psupervisor
    When I try update fields in requisition:
      | supervisoryNode                      |
      | 9f470265-0770-4dd1-bd5a-cf8fe3734d79 |
    And I try to get requisition with id
    Then I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout


  Scenario: The supervisory node should be assigned to an emergency requisition after authorizing it
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
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    |

    When I try to submit a requisition
    And I try to get requisition with id
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    When I try to authorize a requisition
    And I try to get requisition with id
    Then I should get a requisition with "AUTHORIZED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout
