Feature: Sanity

  Scenario: sanity check for project setup
    Given I have created a post titled as foo
    Then I should get back a response with title foo
