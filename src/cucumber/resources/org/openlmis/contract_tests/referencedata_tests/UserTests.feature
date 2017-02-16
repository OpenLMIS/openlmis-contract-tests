@UserTests
Feature: User Tests


  Scenario: User should be able to create an account
    Given I have logged in as administrator

    When I try to create a user with:
      | username | firstName | lastName | email                      | timezone | verified | active | loginRestricted |
      | TestUser | FirstN    | LastN    | testowesoldevelo@gmail.com | CET      | false    | false  | false           |
    Then I should get response with the user’s id

    When I try to get user with id
    Then I should get user with:
      | username | firstName | lastName | email                      | timezone | verified | active | loginRestricted |
      | TestUser | FirstN    | LastN    | testowesoldevelo@gmail.com | CET      | false    | false  | false           |

    When I try to set user password
    Then I should get correct response
    And I logout

    Given I have logged in as TestUser
    Given I logout

