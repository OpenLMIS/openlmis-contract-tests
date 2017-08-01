@PhysicalInventoriesTests
Feature: Physical inventories tests

  Scenario: Store room manager user should be able to save draft physical inventory and submit
    Given I have logged in as srmanager2

    When I try to get a draft with facilityId: e6799d64-d10d-4011-b8c2-0e4d4a3f65ce, programId: dce17f2e-af3e-40ad-8e00-3496adef44c3
    Then I should get no content

    When I try to save the draft physical inventory
    """
    {
    "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
    "facilityId": "e6799d64-d10d-4011-b8c2-0e4d4a3f65ce",
    "lineItems": [
      {
        "orderable": {
          "id": "d602d0c6-4052-456c-8ccd-61b4ad77bece"
        },
        "quantity": 100
      }
    ],
    "signature": "test"
    }
    """
    Then I should get response of draft physical inventory saved

    When I try to submit a physical inventory
    """
    {
    "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
    "facilityId": "e6799d64-d10d-4011-b8c2-0e4d4a3f65ce",
    "lineItems": [
      {
        "occurredDate": "2017-05-26T05:00:00.000Z",
        "orderableId": "d602d0c6-4052-456c-8ccd-61b4ad77bece",
        "quantity": 80
      },
      {
        "occurredDate": "2017-05-26T05:00:00.000Z",
        "orderableId": "d602d0c6-4052-456c-8ccd-61b4ad77bece",
        "lotId": "4727ecbf-df85-41ce-bbe0-bdcc3b1fe448",
        "quantity": 90
      },
      {
        "occurredDate": "2017-05-26T05:00:00.000Z",
        "orderableId": "62e16def-53ef-46b5-8bf1-8dab9b0bcec1",
        "quantity": 100
      },
      {
        "occurredDate": "2017-05-26T05:00:00.000Z",
        "orderableId": "880cf2eb-7b68-4450-a037-a0dec1a17987",
        "quantity": 70
      }
    ],
    "documentNumber": "DOC-1",
    "signature": "test"
    }
    """
    Then I should get response of physical inventory submitted

    When I try to get stock cards summaries by facilityId: e6799d64-d10d-4011-b8c2-0e4d4a3f65ce, programId: dce17f2e-af3e-40ad-8e00-3496adef44c3
    Then I should get response of all stock cards summaries
    And I logout