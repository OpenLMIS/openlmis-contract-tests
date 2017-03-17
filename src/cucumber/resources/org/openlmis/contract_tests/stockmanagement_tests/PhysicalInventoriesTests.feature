@PhysicalInventoriesTests
Feature: Physical inventories tests

  Scenario: Store room manager user should be able to save draft physical inventory and submit
    Given I have logged in as srmanager2

    When I try to get a draft with facilityId: e6799d64-d10d-4011-b8c2-0e4d4a3f65ce, programId: dce17f2e-af3e-40ad-8e00-3496adef44c3
    Then I should get the empty draft of physical inventory

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
        "orderable": {
          "id": "d602d0c6-4052-456c-8ccd-61b4ad77bece"
        },
        "quantity": 233
      }
    ],
    "documentNumber": "DOC-1",
    "occurredDate": "2017-03-15T12:00:00.000Z",
    "signature": "test"
    }
    """
    Then I should get response of physical inventory submitted

    When I try to get stock cards summaries by facilityId: e6799d64-d10d-4011-b8c2-0e4d4a3f65ce, programId: dce17f2e-af3e-40ad-8e00-3496adef44c3
    Then I should get response of all stock cards summaries that include SOH: 233
    And I logout