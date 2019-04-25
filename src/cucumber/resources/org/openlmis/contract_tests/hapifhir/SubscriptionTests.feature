@SubscriptionTests @HapiFhirTests
Feature: Subscription Tests

  Scenario: When rest hook subscription is made, subscribers should receive notification on update
    Given I am logged in as a service client
    And I have an upstream FHIR server

    When I create subscription:
    """
    {
      "resourceType": "Subscription",
      "status": "requested",
      "end": "2025-01-01T00:00:00Z",
      "reason": "Monitor all locations",
      "criteria": "Location?",
      "channel": {
        "type": "rest-hook",
        "endpoint": "http://wiremock:8080/fhir_locations",
        "header": "Authorization: Bearer 04199b94-15ce-4405-969c-05dedf4c073c",
        "payload": "application/json"
      }
    }
    """
    Then I pause for 60 seconds
    And subscription should be created

    When I create location:
    """
    {
      "resourceType": "Location",
      "name": "Contract Test S",
      "alias": ["CTS-FHIR-S"],
      "position": {
        "latitude": 0,
        "longitude": 0
      },
      "physicalType": {
        "coding": [{
          "code": "area"
        }]
      }
    }
    """
    Then I pause for 30 seconds
    And location should be created
    And my upstream FHIR Server has received a notification of a location change
