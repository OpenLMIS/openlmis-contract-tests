@FulfillmentTests
Feature: Fulfillment Tests

  Scenario: Administrator should be able to confirm Proof of Delivery (External Order Path)
    Given I have logged in as administrator

    When I try to convert requisition with:
      | requisitionId                        | supplyingDepotId                     |
      | 21d138bc-f2c2-4188-adf3-a3aae381abde | 19121381-9f3d-4e77-b9e5-d3f59fc1639e |
    Then I should get response of order created

    When I try to get order by:
      | supplyingFacilityId                  | requestingFacilityId                 | programId                            | periodStartDate | periodEndDate |
      | 19121381-9f3d-4e77-b9e5-d3f59fc1639e | 7938919f-6f61-4d1a-a4dc-923c31e9cd45 | dce17f2e-af3e-40ad-8e00-3496adef44c3 | 2017-07-01      | 2017-09-30    |
    Then I should get response of order found

    When I try to get shipment by order id
    Then I should get response of shipment found

    When I try to get proof of delivery by shipment id
    Then I should get response of proof of delivery found

    When I try to confirm the proof of delivery
      | orderableId                          | lotId | quantityAccepted |
      | 7b9b66e0-f008-4c38-93ec-7a5f4868cca9 |       | 0                |
      | bfde33c3-83f1-4feb-a27c-842852f66b71 |       | 0                |
    Then I should get response of proof of delivery confirmation success
    And I logout

  Scenario: Divo user should be able to confirm Proof of Delivery (Local Order Path)
    Given I have logged in as divo1

    When I have got stock card id for
      | program                              | facility                             |
      | 418bdc1d-c303-4bd0-b2d3-d8901150a983 | c62dea9b-6974-4101-ba39-b09914165967 |
    And I try to get stock card with card id
    Then I should get a stock card

    When I try to convert requisition with:
      | requisitionId                        | supplyingDepotId                     |
      | c537e925-a518-4f5b-8aef-6f07fd9aa58c | c62dea9b-6974-4101-ba39-b09914165967 |
    Then I should get response of order created

    When I try to get order by:
      | supplyingFacilityId                  | requestingFacilityId                 | programId                            | processingPeriodId                   |
      | c62dea9b-6974-4101-ba39-b09914165967 | a205764b-3998-4c89-afac-2bc9d737c8d3 | 418bdc1d-c303-4bd0-b2d3-d8901150a983 | 516ac930-0d28-49f5-a178-64764e22b236 |
    Then I should get response of order found

    When I try to get shipment by order id
    Then I should get response of shipment not found

    When I try to finalize shipment
      | orderableId                          | lotId                                | quantityShipped |
      | 7c58f053-0ca5-46ad-8209-98cff0c79b42 |                                      | 3               |
      | 71204042-558e-4c47-a602-7886451e5689 |                                      | 0               |
      | 9c3bea84-487b-4e22-8759-3fe93f8201d9 | f08d3db1-9132-487c-ba58-15ea12302aee | 4               |
      | 9c3bea84-487b-4e22-8759-3fe93f8201d9 | 9d0cd7cb-5739-491b-8d6b-890fd17ec810 | 1               |
      | 047638fa-92ce-4adf-9bbd-3bcb3216897a | f9f9bb7c-7636-40c1-a548-b45e918a512a | 55              |
      | 047638fa-92ce-4adf-9bbd-3bcb3216897a | 06f86552-1aa9-47d6-8857-6b7ad824f805 | 18              |
      | 047638fa-92ce-4adf-9bbd-3bcb3216897a |                                      | 10              |
      | a852f785-d82e-4afe-bc6a-04f3236caf90 |                                      | 2               |
      | 18223845-4d19-4408-b7c2-971c26014058 |                                      | 53              |
      | b75a88c7-70fa-4bab-9785-3dd980a5ffc3 |                                      | 4               |
    Then I should get response of shipment created

    When I have got stock card id for
      | program                              | facility                             |
      | 418bdc1d-c303-4bd0-b2d3-d8901150a983 | c62dea9b-6974-4101-ba39-b09914165967 |
    And I try to get stock card with card id
    Then I should get a stock card with zero stock on hand

    When I try to get proof of delivery by shipment id
    Then I should get response of proof of delivery found

    When I try to confirm the proof of delivery
      | orderableId                          | lotId                                | quantityAccepted |
      | 7c58f053-0ca5-46ad-8209-98cff0c79b42 |                                      | 3                |
      | 71204042-558e-4c47-a602-7886451e5689 |                                      | 0                |
      | 9c3bea84-487b-4e22-8759-3fe93f8201d9 | f08d3db1-9132-487c-ba58-15ea12302aee | 4                |
      | 9c3bea84-487b-4e22-8759-3fe93f8201d9 | 9d0cd7cb-5739-491b-8d6b-890fd17ec810 | 1                |
      | 047638fa-92ce-4adf-9bbd-3bcb3216897a | f9f9bb7c-7636-40c1-a548-b45e918a512a | 55               |
      | 047638fa-92ce-4adf-9bbd-3bcb3216897a | 06f86552-1aa9-47d6-8857-6b7ad824f805 | 18               |
      | 047638fa-92ce-4adf-9bbd-3bcb3216897a |                                      | 10               |
      | a852f785-d82e-4afe-bc6a-04f3236caf90 |                                      | 2                |
      | 18223845-4d19-4408-b7c2-971c26014058 |                                      | 53               |
      | b75a88c7-70fa-4bab-9785-3dd980a5ffc3 |                                      | 4                |
    Then I should get response of proof of delivery confirmation success
    And I logout
