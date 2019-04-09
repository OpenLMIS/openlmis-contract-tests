@SplitRequisitionTests
Feature: Splitting requisitions for multiple suppliers

  The goal is to verify that OpenLMIS enables to split products within a requisition by supply partner
  so that each partner only supplies the products they are configured by the administrator to supply.

  Scenario: District Store Manager should be able to approve a requisition which will be split
    Given I have logged in as dsrmanager

    When I try to find requisitions for approval with request parameters:
      | programId                            |
      | ef9a2b39-e7ce-4ebe-930c-f5c6d15b578a |
    And I try to find a requisition from the response with parameters:
      | facility.id                          | processingPeriod.id                  |
      | bfe3eac5-9961-4d44-a492-431865bbcb23 | 04ec3c83-a086-4792-b7a3-c46559b7f6cd |
    Then I should get the requisition

    When I try to get requisition with id
    Then the requisition should have line items with values:
      | orderable.id                         | approvedQuantity |
      | d059457a-b7ec-4353-89c6-dde0fc7a0c4b | 40               |
      | 3fecd8a4-d130-4e82-95ff-7d7620e24d8d | 40               |
      | 65df9af9-dd9d-4df4-8157-bce65b4a42dd | 40               |
      | 952aabc6-ffa8-4e9a-ac4d-82b843704a82 | 25               |
      | fd77b03e-5578-429b-9802-6d7e4d8cb91d | 5                |
      | dc576f79-8d1c-4f4c-a94f-1ddda1065c85 | 37               |
      | 81dd320e-6d28-446a-8613-8bb899d6c206 | 10               |
      | e1e51f5a-1bda-4777-a074-d865206e546f | 10               |
      | 7f0aac5f-582a-4a6a-a8e1-dd534200f257 | 60               |
      | dc6e49d4-6fd8-49d0-ac4f-44d055c4b49d | 65               |
      | f7169041-733a-4279-bcbb-aee248e1b9dd | 45               |
      | a4460709-c908-4b94-b3c0-faf5eeefcc03 | 40               |

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "IN_APPROVAL" status
    And I logout

  Scenario: Program Supervisor should be able to approve split requisition
    Given I have logged in as psupervisor

    When I try to find requisitions for approval with request parameters:
      | programId                            |
      | ef9a2b39-e7ce-4ebe-930c-f5c6d15b578a |
    And I try to find a requisition from the response with parameters:
      | facility.id                          | processingPeriod.id                  |
      | bfe3eac5-9961-4d44-a492-431865bbcb23 | 04ec3c83-a086-4792-b7a3-c46559b7f6cd |
    Then I should get the requisition

    When I try to get requisition with id
    Then the requisition should have line items with values:
      | orderable.id                         | approvedQuantity | remarks                              |
      | d059457a-b7ec-4353-89c6-dde0fc7a0c4b | 40               |                                      |
      | 3fecd8a4-d130-4e82-95ff-7d7620e24d8d |                  | Supplied by other warehouse/partner. |
      | 65df9af9-dd9d-4df4-8157-bce65b4a42dd | 40               |                                      |
      | 952aabc6-ffa8-4e9a-ac4d-82b843704a82 |                  | Supplied by other warehouse/partner. |
      | fd77b03e-5578-429b-9802-6d7e4d8cb91d | 5                |                                      |
      | dc576f79-8d1c-4f4c-a94f-1ddda1065c85 |                  | Supplied by other warehouse/partner. |
      | 81dd320e-6d28-446a-8613-8bb899d6c206 | 10               |                                      |
      | e1e51f5a-1bda-4777-a074-d865206e546f |                  | Supplied by other warehouse/partner. |
      | 7f0aac5f-582a-4a6a-a8e1-dd534200f257 | 60               |                                      |
      | dc6e49d4-6fd8-49d0-ac4f-44d055c4b49d |                  | Supplied by other warehouse/partner. |
      | f7169041-733a-4279-bcbb-aee248e1b9dd | 45               |                                      |
      | a4460709-c908-4b94-b3c0-faf5eeefcc03 |                  | Supplied by other warehouse/partner. |

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "APPROVED" status
    And I logout

  Scenario: Partner user should be able to see partner requisition
    Given I have logged in as chaz

    When I try to find requisitions for approval with request parameters:
      | programId                            |
      | ef9a2b39-e7ce-4ebe-930c-f5c6d15b578a |
    And I try to find a requisition from the response with parameters:
      | facility.id                          | processingPeriod.id                  |
      | bfe3eac5-9961-4d44-a492-431865bbcb23 | 04ec3c83-a086-4792-b7a3-c46559b7f6cd |
    Then I should get the requisition


    When I try to get requisition with id
    Then the requisition should have line items with values:
      | orderable.id                         | approvedQuantity |
      | 3fecd8a4-d130-4e82-95ff-7d7620e24d8d | 40               |
      | 952aabc6-ffa8-4e9a-ac4d-82b843704a82 | 25               |
      | dc576f79-8d1c-4f4c-a94f-1ddda1065c85 | 37               |
      | e1e51f5a-1bda-4777-a074-d865206e546f | 10               |
      | dc6e49d4-6fd8-49d0-ac4f-44d055c4b49d | 65               |
      | a4460709-c908-4b94-b3c0-faf5eeefcc03 | 40               |

    When I try to approve a requisition
    And I try to get requisition with id
    Then I should get a requisition with "APPROVED" status
    And I logout
