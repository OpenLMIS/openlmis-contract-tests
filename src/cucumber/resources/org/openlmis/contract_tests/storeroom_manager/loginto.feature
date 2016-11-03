
Feature:  Logging in
  @storeroom_manager
  Scenario: Storeroom Manager user should be able to log into application
    Given Storeroom manager user should exist in database with username: StoreroomManager and password: password
    When  I try to log into application with username: StoreroomManager, password: password
    Then  I should see Home Page with text: Welcome to Logistics Management Information Systems


  Scenario: StoreroomManager try to log into application with incorect password
    Given Storeroom manager user should exist in database with username: StoreroomManager and password: password and should not exist with username: StoreroomManager and password: password1
    When  I try to log into application with username: StoreroomManager, password: password1
    Then  I should see Login Page with error message: The username or password you entered is incorrect. Please try again.

  Scenario: StoreroomManager try to log into application with non-existing username
    Given Storeroom manager user should exist in database with username: StoreroomManager and password: password and should not exist with username: StoreroomManager1 and password: password
    When  I try to log into application with username: StoreroomManager1, password: password
    Then  I should see Login Page with error message: The username or password you entered is incorrect. Please try again.