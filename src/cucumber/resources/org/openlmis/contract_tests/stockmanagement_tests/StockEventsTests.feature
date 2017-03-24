@StockEventsTests
Feature: Stock Events Tests

  Scenario: Storeroom manager user should be able to create stock event
    Given I have logged in as srmanager1

    When I try to create a stock event
    """
    {
      "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
      "facilityId": "176c4276-1fb1-4507-8ad2-cdfba0f47445",
      "orderableId": "d602d0c6-4052-456c-8ccd-61b4ad77bece",

      "occurredDate": "2017-02-06T17:55:32+08:00",

      "quantity": 123,

      "reasonId":"e3fc3cf3-da18-44b0-a220-77c985202e06",

      "signature": "manager 1",
      "documentNumber":"DN.1"
    }
    """
    Then I should get response of the event created

    When I try to create a stock event
    """
    {
      "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
      "facilityId": "176c4276-1fb1-4507-8ad2-cdfba0f47445",
      "orderableId": "11111111-2222-3333-4444-000000000000",

      "occurredDate": "2017-02-06T17:55:32+08:00",

      "quantity": 123,

      "reasonId":"e3fc3cf3-da18-44b0-a220-77c985202e06",

      "signature": "manager 1",
      "documentNumber":"DN.1"
    }
    """

    Then I should get response of incorrect body with orderable

    When I try to create a stock event
    """
    {
      "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
      "facilityId": "176c4276-1fb1-4507-8ad2-cdfba0f47445",
      "orderableId": "d602d0c6-4052-456c-8ccd-61b4ad77bece",

      "occurredDate": "2017-02-06T17:55:32+08:00",

      "quantity": 500,

      "reasonId": "c1fc3cf3-da18-44b0-a220-77c985202e06",

      "signature": "manager 1",
      "documentNumber":"DN.1"
    }
    """
    Then I should get response of incorrect body with quantity exceed stock on hand
    And I logout

  Scenario: User without create event permission should not be able to create event
    Given I have logged in as wclerk1

    When I try to create a stock event
    """
    {
      "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
      "facilityId": "176c4276-1fb1-4507-8ad2-cdfba0f47445",
      "orderableId": "d602d0c6-4052-456c-8ccd-61b4ad77bece",

      "occurredDate": "2017-02-06T17:55:32+08:00",

      "quantity": 123,

      "reasonId":"e3fc3cf3-da18-44b0-a220-77c985202e06",

      "signature": "manager 1",
      "documentNumber":"DN.1"
    }
    """
    Then I should get response of incorrect user permission of create events