@SubscriptionTests
  Feature: HAPI FHIR Subscription Tests

    Scenario: When rest hook subscription is made, subscribers should receive notification on update
      Given I use API Key: 9a556033-ed13-4dde-9561-158469d15134
      And I stub a mock server
      And I post a rest-hook subscription resource to call wiremock server on any location update
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

      Then I post a new hapifhir location
      """
      {
        "id": "a5d519aa-1ab4-49e8-b720-07cc647108ea"
        "resourceType": "Location",
        "name": "Contract Test S  FHIR Geographic Zone 2",
        "alias": ["CTS-FHIR-GZ-2"],
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

      And I pause 30 seconds

      Then verify that mock server is called