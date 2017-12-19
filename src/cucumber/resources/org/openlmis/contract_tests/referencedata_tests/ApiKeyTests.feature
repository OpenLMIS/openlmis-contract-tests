@ApiKeyTests
Feature: API Key Tests


  Scenario: User should be able to create and use API key
    Given I have logged in as administrator

    When I try to create a API key
    Then I should get response with the new API key
    And I logout

    When I try to retrieve a list of programs by using API key
    Then I should get the list of programs

    When I try to retrieve a list of rights by using API key
    Then I should get an error of missing permission to retrieve the list of rights

    When I have logged in as administrator
    And I try to remove created API key
    Then I should get response of deleted API key
    And I logout

    When I try to retrieve a list of programs by using API key
    Then I should get an error of missing authorization to retrieve the list of programs

    When I try to retrieve a list of rights by using API key
    Then I should get an error of missing authorization to retrieve the list of rights
