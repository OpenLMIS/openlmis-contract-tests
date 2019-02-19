@RegularRequisitionTests
Feature: Regular Requisition Tests

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
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 7                     | 3                | 3                 | 4                 | test                         | 10                    |

    When I try to delete requisition
    Then I should get response of deleted requisition

    When I try to get requisition with id
    Then I should get response of not found
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
    And I should get a requisition without supervisoryNode

    When I try update fields in requisition:
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

    When I have logged in as psupervisor
    When I try update fields in requisition:
      | approvedQuantity | supervisoryNode                      |
      | 4                | 9f470265-0770-4dd1-bd5a-cf8fe3734d79 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 4                |
    And I should get a requisition with "fb38bd1c-beeb-4527-8345-900900329c10" supervisoryNode

    When I try to reject authorized requisition
    And I try to get requisition with id
    Then I should get a requisition with "REJECTED" status
    And I logout

    When I have logged in as srmanager1
    When I try to submit a requisition
    And I try to get requisition with id
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager1
    When I try to authorize a requisition
    And I try to get requisition with id
    Then I should get a requisition with "AUTHORIZED" status
    And I logout

    When I have logged in as psupervisor
    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "APPROVED" status
    And I logout

    When I have logged in as wclerk1
    And I try to convert requisition with:
      | supplyingDepotId                     |
      | 19121381-9f3d-4e77-b9e5-d3f59fc1639e |
    Then I should get response of order created
    And I logout

    When I have logged in as srmanager1
    And I try to get requisition with id
    Then I should get a requisition with "RELEASED" status
    And I logout


  Scenario: Calculated fields should be calculated properly and not change after status changes
    Given I have logged in as administrator

    When I try get a requisition templates
    Then I should get response with requisition template for a program dce17f2e-af3e-40ad-8e00-3496adef44c3 and facility type ac1d268b-ce10-455f-bf87-9c667da8f060
    And I try to update columns:
      |                      | isDisplayed |
      | maximumStockQuantity | true        |
      | total                | true        |
    And I should get response that template has been updated

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 04ec3c83-a086-4792-b7a3-c46559b7f6cd | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | 04ec3c83-a086-4792-b7a3-c46559b7f6cd | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 2                     | 3                | 1                 | 2                 | test                         | 5                     | 2017-08-15                      |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 2                     | 3                | 1                 | 2                 | test                         | 5                     | 5     |

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 20                    | 5                | 21                | 22                | test                         | 25                    | 2017-08-15                      |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 20                    | 5                | 21                | 22                | test                         | 25                    | 25    |

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 15                    | 15               | 6                 | 7                 | test                         | 30                    | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 15                    | 15               | 6                 | 7                 | test                         | 30                    | 30    |

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 12                    | 20               | 8                 | 9                 | test                         | 32                    | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 12                    | 20               | 8                 | 9                 | test                         | 32                    | 32    |

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 1                     | 1                | 16                | 17                | test                         | 2                     | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 1                     | 1                | 16                | 17                | test                         | 2                     | 2     |

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 2                     | 2                | 2                 | 2                 | test                         | 4                     | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 2                     | 2                | 2                 | 2                 | test                         | 4                     | 4     |

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | total |
      | 9                     | 2                | 0                 | 5                 | test                         | 11                    | 11    |

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity | datePhysicalStockCountCompleted |
      | 9                     | 2                | 0                 | 432               | test                         | 11                    | 2017-08-15 |
    Then I try to get requisition with id
    And I should get updated requisition with proper total cost
    And I should get a updated requisition with:
      | maxPeriodsOfStock | averageConsumption | maximumStockQuantity |
      | 3.0               | 11                 | 33                   |

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 9                     | 2                | 0                 | 321               | test                         | 11                    |
    And I try to get requisition with id
    Then I should get updated requisition with proper total cost

    When I try to submit a requisition
    And I try to get requisition with id
    Then I should get a requisition with "SUBMITTED" status
    And I should get a updated requisition with:
      | maximumStockQuantity |
      | 33                   |
    And I logout

    When I have logged in as smanager1
    When I try to authorize a requisition
    And I try to get requisition with id
    Then I should get a requisition with "AUTHORIZED" status
    And I should get updated requisition with proper total cost
    And I should get a updated requisition with:
      | maximumStockQuantity |
      | 33                   |
    And I logout

    When I have logged in as psupervisor
    And I try update fields in requisition:
      | approvedQuantity |
      | 430              |
    Then I try to get requisition with id
    And I should get updated requisition with proper total cost
    And I should get a updated requisition with:
      | maximumStockQuantity |
      | 33                   |

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "APPROVED" status
    And I should get updated requisition with proper total cost
    And I should get a updated requisition with:
      | maximumStockQuantity |
      | 33                   |
    And I logout

    When I have logged in as wclerk1
    And I try to convert requisition with:
      | supplyingDepotId                     |
      | 19121381-9f3d-4e77-b9e5-d3f59fc1639e |
    Then I logout

    When I have logged in as srmanager1
    And I try to get requisition with id
    Then I should get a requisition with "RELEASED" status
    And I should get updated requisition with proper total cost
    And I should get a updated requisition with:
      | maximumStockQuantity |
      | 33                   |
    And I logout


  Scenario Outline: Average consumption should be calculated properly (Number of periods to average: <periods>)
    Given I have logged in as administrator
    And I try get a requisition templates
    Then I should get response with requisition template for a program dce17f2e-af3e-40ad-8e00-3496adef44c3 and facility type ac1d268b-ce10-455f-bf87-9c667da8f060
    And I try to update a requisition template:
      | numberOfPeriodsToAverage |
      | <periods>                |
    And I should get response that template has been updated

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId   | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | <period_1> | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId   | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | <period_1> | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity | requestedQuantity | requestedQuantityExplanation | totalStockoutDays | datePhysicalStockCountCompleted |
      | 10               | 0                     | 10                    | 30                | we need more                 | 20                | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity | requestedQuantity | requestedQuantityExplanation | totalStockoutDays | adjustedConsumption | averageConsumption |
      | 10               | 0                     | 10                    | 30                | we need more                 | 20                | 30                  | <average_1>        |

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

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId   | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | <period_2> | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId   | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | <period_2> | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity | requestedQuantity | requestedQuantityExplanation | totalStockoutDays | datePhysicalStockCountCompleted |
      | 0                | 30                    | 20                    | 10                | we need more                 | 0                 | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity | requestedQuantity | requestedQuantityExplanation | totalStockoutDays | adjustedConsumption | averageConsumption |
      | 0                | 30                    | 20                    | 10                | we need more                 | 0                 | 20                  | <average_2>        |

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

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId   | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | <period_3> | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId   | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | <period_3> | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity | requestedQuantity | requestedQuantityExplanation | totalStockoutDays | datePhysicalStockCountCompleted |
      | 10               | 10                    | 0                     | 20                | we need more                 | 0                 | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity | requestedQuantity | requestedQuantityExplanation | totalStockoutDays | adjustedConsumption | averageConsumption |
      | 10               | 10                    | 0                     | 20                | we need more                 | 0                 | 0                   | <average_3>        |

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

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId   | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | <period_4> | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId   | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | <period_4> | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity | requestedQuantity | requestedQuantityExplanation | totalStockoutDays | datePhysicalStockCountCompleted |
      | 20               | 20                    | 15                    | 25                | we need more                 | 0                 | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity | requestedQuantity | requestedQuantityExplanation | totalStockoutDays | adjustedConsumption | averageConsumption |
      | 20               | 20                    | 15                    | 25                | we need more                 | 0                 | 15                  | <average_4>        |

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

    When I have logged in as srmanager1
    And I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId   | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | <period_5> | false     |

    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId   | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | <period_5> | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity | requestedQuantity | requestedQuantityExplanation | totalStockoutDays | datePhysicalStockCountCompleted |
      | 25               | 25                    | 50                    | 50                | we need more                 | 0                 | 2017-08-15 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity | requestedQuantity | requestedQuantityExplanation | totalStockoutDays | adjustedConsumption | averageConsumption |
      | 25               | 25                    | 50                    | 50                | we need more                 | 0                 | 50                  | <average_5>        |

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

    Examples:
      | periods | period_1                             | period_2                             | period_3                             | period_4                             | period_5                             | average_1 | average_2 | average_3 | average_4 | average_5 |
      | 2       | 61694e82-1be6-40a4-9aaa-bfbb720a0d7d | c9287c65-67fa-4958-adb6-52069f2b1379 | 2d490229-02f8-4235-9be4-1443fd8f7b4f | a91dfbdf-0d9b-4cb1-9d46-0a47b17d22d0 | f55d9dda-d440-40c0-a793-914e0fe1f579 | 21        | 25        | 10        | 8         | 33        |
      | 3       | ecd6c3c8-f80c-4c4e-af9a-d0529b55424a | c08e1295-793f-4550-933b-571e117f15d9 | 2a9fddb6-5dd3-4dde-81f3-c19629257986 | 1f3c89b2-13e4-4a72-9ef2-228302cc5a25 | 96da0fc3-28fd-4a3f-87a8-b5cdb175c45a | 32        | 34        | 17        | 12        | 22        |
      | 4       | 0e64442e-fbbe-4a25-9062-fb137040fa63 | af437fc4-ca09-4f02-9ab8-095032f1109b | 2ad974da-3037-4d34-b383-202176b2e13c | c0f0f055-8b50-4709-a02d-d6cc1850bd14 | 6be40568-80ff-47a0-9f14-db7c51fedce1 | 24        | 29        | 25        | 17        | 22        |


  Scenario: Storeroom Manager user should be able to skip initiated requisition
    Given I have logged in as srmanager3

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 5b39f0fb-5c47-4551-b983-1e30ac6df65d | bc1c47bc-3f29-4504-af07-ebbc44040a55 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 5b39f0fb-5c47-4551-b983-1e30ac6df65d | bc1c47bc-3f29-4504-af07-ebbc44040a55 | false     |
    And I should get a requisition with "INITIATED" status

    When I try to skip initiated requisition
    And I try to get requisition with id
    Then I should get a requisition with "SKIPPED" status

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 5b39f0fb-5c47-4551-b983-1e30ac6df65d | 906a53c6-e76c-4237-9a7d-344e7a6bdd04 | false     |
    Then I should get response with the initiated requisition's id
    When I try to get requisition with id
    Then I should get a updated requisition with:
      | beginningBalance |
      | 0                |

    When I try to delete requisition
    Then I should get response of deleted requisition

    When I try to get requisition with id
    Then I should get response of not found
    And I logout

  Scenario: A requisition should be automatically converted to an order after it goes through final approval
    Given I have logged in as vsrmanager1

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | 418bdc1d-c303-4bd0-b2d3-d8901150a983 | 7fc9bda8-ad8a-468d-8244-38e1918527d5 | 04ec3c83-a086-4792-b7a3-c46559b7f6cd | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | 418bdc1d-c303-4bd0-b2d3-d8901150a983 | 7fc9bda8-ad8a-468d-8244-38e1918527d5 | 04ec3c83-a086-4792-b7a3-c46559b7f6cd | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | requestedQuantity | requestedQuantityExplanation |
      | 2                 | test                         |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | requestedQuantity | requestedQuantityExplanation |
      | 2                 | test                         |

    When I try to submit a requisition
    And I try to get requisition with id
    Then I should get a requisition with "AUTHORIZED" status
    And I logout

    When I have logged in as divo1
    And I try update fields in requisition:
      | approvedQuantity |
      | 4                |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 4                |

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "RELEASED" status
    And I logout


  Scenario: Storeroom Manager user should be able to skip / unskip line items
    Given I have logged in as srmanager3

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 5b39f0fb-5c47-4551-b983-1e30ac6df65d | 906a53c6-e76c-4237-9a7d-344e7a6bdd04 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 5b39f0fb-5c47-4551-b983-1e30ac6df65d | 906a53c6-e76c-4237-9a7d-344e7a6bdd04 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays |
      | 10               | 5                     | 5                      | 2                 |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays |
      | 10               | 5                     | 5                      | 2                 |

    When I try to update fields for product id 32a04b36-4b4b-4591-b7fe-927d3569bd06:
      | skipped |
      | true    |
    Then I should get requisition response with status 200

    When I try to get requisition with id
    Then I should get updated requisition with product id 32a04b36-4b4b-4591-b7fe-927d3569bd06:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays | skipped | stockOnHand |
      | 10               | 5                     | 5                      | 2                 | true    | null        |
    And I should get updated requisition with product id f573f7d1-b90b-4c3c-8d8b-b8865315d32d:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays | skipped | stockOnHand |
      | 10               | 5                     | 5                      | 2                 | false   | 10          |

    When I try to submit a requisition
    And I try to get requisition with id
    Then I should get a requisition with "SUBMITTED" status
    And I should get updated requisition with product id 32a04b36-4b4b-4591-b7fe-927d3569bd06:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays | skipped |
      | 10               | 5                     | 5                      | 2                 | true    |
    And I logout

    When I have logged in as smanager3
    And I try to update fields for product id 32a04b36-4b4b-4591-b7fe-927d3569bd06:
      | skipped  |
      | false    |
    Then I should get requisition response with status 200

    When I try to get requisition with id
    Then I should get updated requisition with product id 32a04b36-4b4b-4591-b7fe-927d3569bd06:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays | skipped |
      | 10               | 5                     | 5                      | 2                 | false   |

    When I try to update fields for product id f573f7d1-b90b-4c3c-8d8b-b8865315d32d:
      | skipped |
      | true    |
    Then I should get requisition response with status 200

    When I try to get requisition with id
    Then I should get updated requisition with product id f573f7d1-b90b-4c3c-8d8b-b8865315d32d:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays | skipped |
      | 10               | 5                     | 5                      | 2                 | true    |

    When I try to authorize a requisition
    And I try to get requisition with id
    Then I should get a requisition with "AUTHORIZED" status
    And I should get updated requisition with product id f573f7d1-b90b-4c3c-8d8b-b8865315d32d:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays | skipped |
      | 10               | null                  | null                   | null              | true    |
    And I should get updated requisition with product id 32a04b36-4b4b-4591-b7fe-927d3569bd06:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays | skipped |
      | 10               | 5                     | 5                      | 2                 | false   |
    And I logout

    When I have logged in as dsrmanager
    And I try update fields in requisition:
      | skipped |
      | true    |

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "RELEASED_WITHOUT_ORDER" status
    And I should get updated requisition with product id f573f7d1-b90b-4c3c-8d8b-b8865315d32d:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays | skipped |
      | 10               | null                  | null                   | null              | true    |
    And I should get updated requisition with product id 32a04b36-4b4b-4591-b7fe-927d3569bd06:
      | beginningBalance | totalReceivedQuantity | totalConsumedQuantity  | totalStockoutDays | skipped |
      | 10               | 5                     | 5                      | 2                 | false   |
    And I logout


  Scenario: Storeroom Manager user should be not able to skip / unskip line items when column is disabled
    Given I have logged in as administrator
    When I try get a requisition templates
    Then I should get response with requisition template for a program dce17f2e-af3e-40ad-8e00-3496adef44c3 and facility type ac1d268b-ce10-455f-bf87-9c667da8f060
    When I try to update columns:
      |         | isDisplayed | source               |
      | skipped | false       | PREVIOUS_REQUISITION |
    Then I should get response that template has been updated

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | f23ce0ab-2ebd-4d19-ba6e-aa9bdd34093b | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 176c4276-1fb1-4507-8ad2-cdfba0f47445 | f23ce0ab-2ebd-4d19-ba6e-aa9bdd34093b | false     |
    And I should get a requisition with "INITIATED" status

    When I try to update fields for product id d602d0c6-4052-456c-8ccd-61b4ad77bece:
      | skipped |
      | true    |
    Then I should get requisition response with status 400

    When I try to delete requisition
    Then I should get response of deleted requisition

    When I try to get requisition with id
    Then I should get response of not found
    And I logout


  Scenario: It should be possible to go through 2-way approval process
    Given I have logged in as srmanager4

    When I try to initiate a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 7938919f-6f61-4d1a-a4dc-923c31e9cd45 | 7880be4f-6582-472a-9ea5-a6baed71e6e5 | false     |
    Then I should get response with the initiated requisition's id

    When I try to get requisition with id
    Then I should get a requisition with:
      | programId                            | facilityId                           | periodId                             | emergency |
      | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 7938919f-6f61-4d1a-a4dc-923c31e9cd45 | 7880be4f-6582-472a-9ea5-a6baed71e6e5 | false     |
    And I should get a requisition with "INITIATED" status

    When I try update fields in requisition:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 2                     | 3                | 1                 | 2                 | test                         | 5                     |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | totalReceivedQuantity | beginningBalance | totalStockoutDays | requestedQuantity | requestedQuantityExplanation | totalConsumedQuantity |
      | 2                     | 3                | 1                 | 2                 | test                         | 5                     |

    When I try to submit a requisition
    And I try to get requisition with id
    Then I should get a requisition with "SUBMITTED" status
    And I logout

    When I have logged in as smanager4
    And I try to authorize a requisition
    And I try to get requisition with id
    Then I should get a requisition with "AUTHORIZED" status
    And I logout

    When I have logged in as dsrmanager
    And I try update fields in requisition:
      | approvedQuantity |
      | 4                |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 4                |

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "IN_APPROVAL" status
    And I logout

    When I have logged in as psupervisor
    And I try update fields in requisition:
      | approvedQuantity |
      | 8                |
    And I try to get requisition with id
    Then I should get a updated requisition with:
      | approvedQuantity |
      | 8                |

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "APPROVED" status
    And I logout

    When I have logged in as wclerk1
    And I try to convert requisition with:
      | supplyingDepotId                     |
      | 19121381-9f3d-4e77-b9e5-d3f59fc1639e |
    Then I logout

    When I have logged in as srmanager4
    And I try to get requisition with id
    Then I should get a requisition with "RELEASED" status
    And I logout
