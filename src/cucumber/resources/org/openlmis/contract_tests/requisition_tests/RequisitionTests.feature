@RequisitionTests
Feature: Requisition Tests


  Scenario: Program Supervisor user should be able to approve a requisition
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 2                     | 3                | 1                 | 2                 | test                         | 5                     |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 2                     | 3                | 1                 | 2                 | test                         | 5                     | 5     |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    And I try to authorize a requisition
    Then I should get a requisition with "AUTHORIZED" status
    And I logout

    When I have logged in as psupervisor
    And I try update fields in requisition:
      | approvedQuantity |
      | 4                |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 4                |

    When I try to approve a requisition
    Then I should get a requisition with "APPROVED" status
    And I logout


  Scenario: Program Supervisor user should be able to reject authorized requisition
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status
    And I should get a requisition without supervisoryNode

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 7                     | 3                | 3                 | 4                 | test                         | 10                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 7                     | 3                | 3                 | 4                 | test                         | 10                    | 10    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    And I try to authorize a requisition
    Then I should get a requisition with "AUTHORIZED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout

    When I have logged in as psupervisor
    When I try to reject authorized requisition
    Then I should get a requisition with "INITIATED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout

    
  Scenario: Storeroom Manager user should be able to delete initiated requisition
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status
    And I should get a requisition without supervisoryNode

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 7                     | 3                | 3                 | 4                 | test                         | 10                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 7                     | 3                | 3                 | 4                 | test                         | 10                    | 10    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status

    When I try to delete initiated requisition
    Then I should get response of deleted requisition

    When I try to get requisition with id
    Then I should get response of not found
    And I logout


  Scenario: Program Supervisor user should be able to approve emergency requisition
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 20                    | 5                | 21                | 22                | test                         | 25                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 20                    | 5                | 21                | 22                | test                         | 25                    | 25    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as administrator
    When I try to get period with id:
      | periodId                             |
      | 516ac930-0d28-49f5-a178-64764e22b236 |
    Then I should get response with the period id

    #Now periods in demo data are set to current year.
    #Update period to current date.
    #Period must contains current date.
    When I try update period to current date
    Then I logout

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true      |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true      |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 15                    | 15               | 6                 | 7                 | test                         | 30                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 15                    | 15               | 6                 | 7                 | test                         | 30                    | 30    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    And I try to authorize a requisition
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
    Then I should get a requisition with "APPROVED" status
    And I logout


  Scenario: Program Supervisor user should be able to reject authorized emergency requisition
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 5                     | 13               | 2                 | 7                 | test                         | 18                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 5                     | 13               | 2                 | 7                 | test                         | 18                    | 18    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as administrator
    And I try to get period with id:
      | periodId                             |
      | 516ac930-0d28-49f5-a178-64764e22b236 |
    Then I should get response with the period id

    When I try update period to current date
    Then I logout

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true      |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true      |
    And I should get a requisition with "INITIATED" status
    And I should get a requisition without supervisoryNode

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 12                    | 20               | 8                 | 9                 | test                         | 32                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 12                    | 20               | 8                 | 9                 | test                         | 32                    | 32    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    And I try to authorize a requisition
    Then I should get a requisition with "AUTHORIZED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout

    When I have logged in as psupervisor
    When I try to reject authorized requisition
    Then I should get a requisition with "INITIATED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout


  Scenario: Storeroom Manager user should be able to delete initiated emergency requisition
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 5                     | 13               | 2                 | 7                 | test                         | 18                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 5                     | 13               | 2                 | 7                 | test                         | 18                    | 18    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as administrator
    And I try to get period with id:
      | periodId                             |
      | 516ac930-0d28-49f5-a178-64764e22b236 |
    Then I should get response with the period id

    When I try update period to current date
    Then I logout

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true      |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true      |
    And I should get a requisition with "INITIATED" status
    And I should get a requisition without supervisoryNode

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 12                    | 20               | 8                 | 9                 | test                         | 32                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 12                    | 20               | 8                 | 9                 | test                         | 32                    | 32    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status

    When I try to delete initiated requisition
    Then I should get response of deleted requisition

    When I try to get requisition with id
    Then I should get response of not found
    And I logout


  Scenario: Storeroom Manager user should get failure response if date outside of period
  when he creates emergency requisition
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 20                    | 5                | 21                | 22                | test                         | 25                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 20                    | 5                | 21                | 22                | test                         | 25                    | 25    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as administrator
    And I try to get period with id:
      | periodId                             |
      | 516ac930-0d28-49f5-a178-64764e22b236 |
    Then I should get response with the period id

    When I try update period to future date
    Then I logout

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true      |
    Then I should get response of incorrect period
    And I logout


  Scenario: Program Supervisor user should be able to approve second emergency requisition in the same period
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 30                    | 5                | 0                 | 8                 | test                         | 35                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 30                    | 5                | 0                 | 8                 | test                         | 35                    | 35    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as administrator
    And I try to get period with id:
      | periodId                             |
      | 516ac930-0d28-49f5-a178-64764e22b236 |
    Then I should get response with the period id

    When I try update period to current date
    Then I logout

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true      |
    Then I should get response with the initiated requisition's id
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 1                     | 1                | 16                | 17                | test                         | 2                     |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 1                     | 1                | 16                | 17                | test                         | 2                     | 2     |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    And I try to authorize a requisition
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
    Then I should get a requisition with "APPROVED" status
    And I logout

    When I have logged in as srmanager1
    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true     |
    Then I should get response with the initiated requisition's id
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 2                     | 2                | 2                 | 2                 | test                         | 4                     |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 2                     | 2                | 2                 | 2                 | test                         | 4                     | 4     |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    And I try to authorize a requisition
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
    Then I should get a requisition with "APPROVED" status
    And I logout


  Scenario: Warehouse Manager user should be able to convert requisition to order
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    | 11    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    When I try to authorize a requisition
    Then I should get a requisition with "AUTHORIZED" status
    And I logout

    When I have logged in as psupervisor
    When I try update fields in requisition:
      | approvedQuantity |
      | 4                |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 4                |

    When I try to approve a requisition
    Then I should get a requisition with "APPROVED" status
    And I logout

    When I have logged in as wclerk1
    And I try to convert requisition to order
    Then I logout

    When I have logged in as srmanager1
    And I try to get requisition with id
    Then I should get a requisition with "RELEASED" status
    And I logout


  Scenario: The supervisory node should be assigned to a regular requisition after authorizing it
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status
    And I should get a requisition without supervisoryNode

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    | 11    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    When I try to authorize a requisition
    Then I should get a requisition with "AUTHORIZED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout


  Scenario: The supervisory node should be assigned to an emergency requisition after authorizing it
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 20                    | 5                | 21                | 22                | test                         | 25                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 20                    | 5                | 21                | 22                | test                         | 25                    | 25    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as administrator
    And I try to get period with id:
      | periodId                             |
      | 516ac930-0d28-49f5-a178-64764e22b236 |
    Then I should get response with the period id

    When I try update period to current date
    Then I logout

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true      |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | true      |
    And I should get a requisition with "INITIATED" status
    And I should get a requisition without supervisoryNode

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    | 11    |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    When I try to authorize a requisition
    Then I should get a requisition with "AUTHORIZED" status
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode
    And I logout


  Scenario: Total cost should be calculated properly and not change after status changes
    Given I have logged in as srmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 516ac930-0d28-49f5-a178-64764e22b236 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 9                     | 2                | 0                 | 432               | test                         | 11                    |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | packsToShip | pricePerPack | totalCost |
      | 9                     | 2                | 0                 | 432               | test                         | 11                    | 5           | 5.23         | 26.15     |

    When I try to submit a requisition
    Then I should get a requisition with "SUBMITTED" status
    And I should get a updated requisition with:
      | totalCost |
      | 26.15     |
    And I logout

    When I have logged in as smanager1
    When I try to authorize a requisition
    Then I should get a requisition with "AUTHORIZED" status
    And I should get a updated requisition with:
      | totalCost |
      | 26.15     |
    And I logout

    When I have logged in as psupervisor
    When I try update fields in requisition:
      | approvedQuantity |
      | 430              |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity | totalCost |
      | 430              | 26.15     |

    When I try to approve a requisition
    Then I should get a requisition with "APPROVED" status
    And I should get a updated requisition with:
      | totalCost |
      | 26.15     |
    And I logout

    When I have logged in as wclerk1
    And I try to convert requisition to order
    Then I logout

    When I have logged in as srmanager1
    And I try to get requisition with id
    Then I should get a requisition with "RELEASED" status
    And I should get a updated requisition with:
      | totalCost |
      | 26.15     |
    And I logout