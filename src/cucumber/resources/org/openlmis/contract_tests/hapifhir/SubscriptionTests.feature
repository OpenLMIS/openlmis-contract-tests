@SubscriptionTests
  Feature: HAPI FHIR Subscription Tests

    Scenario: When rest hook subscription is made, subscribers should receive notification on update
      Given I use API Key: 9a556033-ed13-4dde-9561-158469d15134
      And I have an upstream FHIR server
      When my upstream FHIR server subscribes to Location updates with the OpenLMIS FHIR Service
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

      And I update an OpenLMIS Location
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

      Then After I pause for 30 seconds

      And I verify that my Upstream FHIR Server has received a notification of a Location change