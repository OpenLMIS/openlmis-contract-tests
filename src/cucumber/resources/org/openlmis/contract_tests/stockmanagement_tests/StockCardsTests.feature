@StockCardsTests
Feature: Stock Cards Tests

  Scenario: Storeroom manager user should be able to view stock cards
    Given I have logged in as srmanager1

    When I try to create a stock event
    """
    {
      "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
      "facilityId": "176c4276-1fb1-4507-8ad2-cdfba0f47445",
      "signature": "srmanager",

      "lineItems":[{
        "occurredDate": "2017-02-06T10:00:00.000Z",
        "quantity": 100,
        "orderableId": "23819693-0670-4c4b-b400-28e009b86b51"
      }]
    }
    """
    Then I should get response of the event created

    Given I have got stock card id

    When I try to get stock card with card id
    Then I should get a stock card with 100 stockOnHand

    When I try to create a stock event
    """
    {
      "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
      "facilityId": "176c4276-1fb1-4507-8ad2-cdfba0f47445",
      "signature": "srmanager",

      "lineItems":[{
        "occurredDate": "2017-02-09T10:00:00.000Z",
        "orderableId": "23819693-0670-4c4b-b400-28e009b86b51",
        "quantity": 50
      }]
    }
    """
    Then I should get response of the event created

    When I try to get stock card with card id
    Then I should get a stock card with 50 stockOnHand
    And I logout

    Given I have logged in as srmanager2

    When I try to get stock card with card id
    Then I should get response of incorrect user permission of view cards
    And I logout