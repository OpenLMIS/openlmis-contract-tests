@VerificationTests
Feature: Verification tests

  Scenario: User should be able to verify his email address
    Given I have logged in as wclerk1

    When I try to get my contact details
    Then I should get my contact details

    When I try to update my contact details with:
      | phoneNumber | emailDetails.email        |
      | 111-222-333 | contact_test@openlmis.org |
    Then I should get update response with old email address

    When I try to get verification token
    Then I should get verification response for contact_test@openlmis.org

    When I try to verify my new email address
    Then I should get success verification response

    When I try to get my contact details
    Then I should get my contact details with:
    | phoneNumber | emailDetails.email        | emailDetails.emailVerified |
    | 111-222-333 | contact_test@openlmis.org | true                       |
