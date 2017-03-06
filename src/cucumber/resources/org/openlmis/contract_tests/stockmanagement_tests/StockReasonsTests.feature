@StockReasonsTests
Feature: Stock Reasons Tests

  Scenario: Administrator user should be able to configure stock card line item reasons
    Given I have logged in as administrator

    When I try to get stock card line item reason types
    Then I should get response of stock card line item reason types

    When I try to get stock card line item reason categories
    Then I should get response of stock card line item reason categories
