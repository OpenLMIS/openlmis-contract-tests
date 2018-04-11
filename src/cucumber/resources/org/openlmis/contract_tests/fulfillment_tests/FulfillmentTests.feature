@FulfillmentTests
Feature: Fulfillment Tests

  Scenario: Administrator should be able to confirm Proof of Delivery (External Order Path)
    Given I have logged in as administrator

    When I try to convert requisition with:
      | requisitionId                        | supplyingDepotId                     |
      | 17542db8-7912-4b9f-a922-0b111e1565c8 | dd343fe4-ec0c-4a58-831a-68ccf9f19316 |
    Then I should get response of order created

    When I try to get order by:
      | supplyingFacilityId                  | requestingFacilityId                 | programId                            | periodStartDate | periodEndDate |
      | dd343fe4-ec0c-4a58-831a-68ccf9f19316 | 7938919f-6f61-4d1a-a4dc-923c31e9cd45 | 10845cb9-d365-4aaa-badd-b4fa39c6a26a | 2017-04-01      | 2017-06-30    |
    Then I should get response of order found

    When I try to get shipment by order id
    Then I should get response of shipment found

    When I try to get proof of delivery by shipment id
    Then I should get response of proof of delivery found

    When I try to confirm the proof of delivery
      | orderableId                          | lotId | quantityAccepted |
      | 2400e410-b8dd-4954-b1c0-80d8a8e785fc |       | 62               |
      | 6be94307-c7f4-4782-ba83-45f496c8ac42 |       | 400              |
      | c9e65f02-f84f-4ba2-85f7-e2cb6f0989af |       | 600              |
    Then I should get response of proof of delivery confirmation success
    And I logout

  Scenario: Administrator should be able to print Proof of Delivery
    Given I have logged in as administrator

    When I try to find any proof of delivery
    Then I should get response of proof of delivery found

    When I try to print proof of delivery with id
    Then I should get correct pdf response
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
