@ValidSourceDestinationTests
Feature: Valid Source Destination Tests

  Scenario: Administrator user should be able to assign source or destination to program and facility type
    Given I have logged in as administrator

    When I try to create a new organization
    """
    {
      "name": "testOrg"
    }
    """
    Then I should get response of organization created

    When I try to get all organizations
    Then I should get response of all organizations that contains created organization

    When I try to update an organization
    """
    {
      "name": "updateOrg"
    }
    """
    And I try to get all organizations
    Then I should get response of all organizations that contains updated organization name updateOrg

    When I try to assign created organization as source to combination of program and facility type
    And I try to get all valid source assignments
    Then I should get response of all valid source assignments that contains newly assignment

    When I try to detach created organization as source to combination of program and facility type
    And I try to get all valid source assignments
    Then I should get response of all valid source assignments that not contains detached assignment

    When I try to assign created organization as destination to combination of program and facility type
    And I try to get all valid destination assignments
    Then I should get response of all valid destination assignments that contains newly assignment

    When I try to detach created organization as destination to combination of program and facility type
    And I try to get all valid destination assignments
    Then I should get response of all valid destination assignments that not contains detached assignment