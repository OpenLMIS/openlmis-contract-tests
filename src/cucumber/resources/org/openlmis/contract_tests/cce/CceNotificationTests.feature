@CceNotificationTests
Feature: CCE Notification Tests

  Scenario: User with supervisory supervision role should get a notification about nonfunctional CCE
    Given I have logged in as divo1

    When I try to retrieve inventory items
    | facilityId                           | programId                            |
    | 4774fc63-a0c8-4a29-a01d-5b6b48632175 | 418bdc1d-c303-4bd0-b2d3-d8901150a983 |
    Then I should get response with inventory item with reference name "Fridge #166"

    When I try to change functionality status to AWAITING_REPAIR with NO_FUEL as a reason
    Then I should get response with inventory item with AWAITING_REPAIR functionality status
    And I pause for 30 seconds

    When I try to find notifications for user 560be32a-ea2e-4d12-ae00-1f69376ad535 from last 5 days
    Then I should get a notification that match the following regex
    """
    ^Dear divo1:(\s*)This email is to inform you that the Kerosene "Fridge #166" at Lurio, Cuamba has been marked as AWAITING_REPAIR with the reason "NO_FUEL\."
    """

  Scenario: User with home facility supervision role should get a notification about nonfunctional CCE
    Given I have logged in as rivo

    When I try to retrieve inventory items
    | facilityId                           | programId                            |
    | 0898dcd6-53f2-41b6-ba7d-b2f5122ffb44 | 418bdc1d-c303-4bd0-b2d3-d8901150a983 |
    Then I should get response with inventory item with reference name "Fridge #27"

    When I try to change functionality status to AWAITING_REPAIR with NO_FUEL as a reason
    Then I should get response with inventory item with AWAITING_REPAIR functionality status
    And I pause for 30 seconds

    When I try to find notifications for user 1e3b03a5-1d48-4de1-bb4a-389beece2277 from last 5 days
    Then I should get a notification that match the following regex
    """
    ^Dear rivo:(\s*)This email is to inform you that the Gas "Fridge #27" at Depósito Provincial Niassa has been marked as AWAITING_REPAIR with the reason "NO_FUEL\."
    """
