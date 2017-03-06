@StockReasonsTests
Feature: Stock Reasons Tests

  Scenario: Administrator user should be able to get available reason types and reason categories
    Given I have logged in as administrator

    When I try to get stock card line item reason types
    Then I should get response of stock card line item reason types

    When I try to get stock card line item reason categories
    Then I should get response of stock card line item reason categories

    And I logout

  Scenario: Administrator user should be able to assign reasons to program and facility type
    Given I have logged in as administrator

    When I try to create a new stock card line item reason
    """
    {
      "name": "test_reason_1",
      "isFreeTextAllowed": true,
      "reasonCategory": "AD_HOC",
      "reasonType": "CREDIT",
      "description": "test description"
    }
    """
    Then I should get response of reason created

    When I try to get all stock card line item reasons
    Then I should get response of all reasons that contains created reason

    When I try to update a stock card line item reason
    """
    {
      "name": "update_reason_1",
      "isFreeTextAllowed": true,
      "reasonCategory": "AD_HOC",
      "reasonType": "CREDIT",
      "description": "test description"
    }
    """
    And I try to get all stock card line item reasons
    Then I should get response of all reasons that contains updated reason name update_reason_1

    When I try to assign created reason to program and facility type combination
    And I try to get all valid reason assignments
    Then I should get response of all valid reason assignments that contains newly assignment

    When I try to detach created reason to program and facility type combination
    And I try to get all valid reason assignments
    Then I should get response of all valid reason assignments that not contains detached assignment

    And I logout

