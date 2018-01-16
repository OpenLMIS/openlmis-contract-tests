@ShipmentTests
Feature: Shipment tests

  Scenario: Divo user should be able to finalize shipment
    Given I have logged in as divo1

    When I have got stock card id for programId: 418bdc1d-c303-4bd0-b2d3-d8901150a983 and facilityId: c62dea9b-6974-4101-ba39-b09914165967
    And I try to get stock card with card id
    Then I should get a stock card

    When I try to convert requisition with requisitionId: c537e925-a518-4f5b-8aef-6f07fd9aa58c and supplyingDepotId: c62dea9b-6974-4101-ba39-b09914165967
    Then I should get response of order created

    When I try to get order by supplying facility: c62dea9b-6974-4101-ba39-b09914165967
    Then I should get response of order found

    When I try to finalize shipment
    """
    {
    "notes": "some notes",
    "lineItems": [
      {
        "orderable": { "id": "7c58f053-0ca5-46ad-8209-98cff0c79b42" },
        "quantityShipped": 31
      },
      {
        "orderable": { "id": "71204042-558e-4c47-a602-7886451e5689" },
        "quantityShipped": 0
      },
      {
        "lot": { "id": "f08d3db1-9132-487c-ba58-15ea12302aee" },
        "orderable": { "id": "9c3bea84-487b-4e22-8759-3fe93f8201d9" },
        "quantityShipped": 99
      },
      {
        "lot": { "id": "9d0cd7cb-5739-491b-8d6b-890fd17ec810" },
        "orderable": { "id": "9c3bea84-487b-4e22-8759-3fe93f8201d9" },
        "quantityShipped": 38
      },
      {
        "lot": { "id": "f9f9bb7c-7636-40c1-a548-b45e918a512a" },
        "orderable": { "id": "047638fa-92ce-4adf-9bbd-3bcb3216897a" },
        "quantityShipped": 557
      },
      {
        "lot": { "id": "06f86552-1aa9-47d6-8857-6b7ad824f805" },
        "orderable": { "id": "047638fa-92ce-4adf-9bbd-3bcb3216897a" },
        "quantityShipped": 180
      },
      {
        "orderable": { "id": "047638fa-92ce-4adf-9bbd-3bcb3216897a" },
        "quantityShipped": 107
      },
      {
        "orderable": { "id": "a852f785-d82e-4afe-bc6a-04f3236caf90" },
        "quantityShipped": 26
      },
      {
        "orderable": { "id": "18223845-4d19-4408-b7c2-971c26014058" },
        "quantityShipped": 531
      },
      {
        "orderable": { "id": "b75a88c7-70fa-4bab-9785-3dd980a5ffc3" },
        "quantityShipped": 47
      }
    ]
    }
    """
    Then I should get response of shipment created

    When I have got stock card id for programId: 418bdc1d-c303-4bd0-b2d3-d8901150a983 and facilityId: c62dea9b-6974-4101-ba39-b09914165967
    And I try to get stock card with card id
    Then I should get a stock card with zero stock on hand

    And I logout