@ExtensionStockEventsTests
Feature: Extension Stock Events Tests

  Scenario: Storeroom manager user should get error when creating stock event
    Given I have logged in as srmanager1

    When I try to create a stock event for extentions
    """
    {
      "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
      "facilityId": "176c4276-1fb1-4507-8ad2-cdfba0f47445",

      "signature": "manager 1",
      "documentNumber":"DN.1",

      "lineItems":[{
        "occurredDate": "2017-02-06",
        "reasonId": "c1fc3cf3-da18-44b0-a220-77c985202e06",
        "quantity": 123,
        "orderableId": "d602d0c6-4052-456c-8ccd-61b4ad77bece"
      }]
    }
    """
    Then I should get response of invalid adjustment reason category

    When I try to create a stock event for extentions
    """
    {
      "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
      "facilityId": "176c4276-1fb1-4507-8ad2-cdfba0f47445",

      "signature": "manager 1",
      "documentNumber":"DN.1",

      "lineItems":[{
        "occurredDate": "2017-02-06",
        "reasonId": "279d55bd-42e3-438c-a63d-9c021b185dae",
        "reasonFreeText": "example",
        "quantity": 123,
        "orderableId": "d602d0c6-4052-456c-8ccd-61b4ad77bece"
      }]
    }
    """
    Then I should get response of free text not allowed

    When I try to create a stock event for extentions
    """
    {
      "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
      "facilityId": "176c4276-1fb1-4507-8ad2-cdfba0f47445",

      "signature": "manager 1",
      "documentNumber":"DN.1",

      "lineItems":[{
        "occurredDate": "2017-02-06",
        "reasonId": "9b4b653a-f319-4a1b-bb80-8d6b4dd6cc12",
        "quantity": 123,
        "orderableId": "d602d0c6-4052-456c-8ccd-61b4ad77bece"
      }],

      "context": {
        "unpackReasonId": "9b4b653a-f319-4a1b-bb80-8d6b4dd6cc12"
      }
    }
    """
    Then I should get response that orderable cannot be unpacked
