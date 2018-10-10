@LocationTests
Feature: Location Tests

  Scenario: Location should be updated when facility is updated
      Given I have logged in as administrator

      When I create a facility:
      """
      {
        "code": "CT-F",
        "name": "Contract Test Facility",
        "description": "This is an example facility which should be added to FHIR server",
        "active": true,
        "goLiveDate": "2015-01-01",
        "goDownDate": "2020-12-31",
        "comment": "Example comment",
        "enabled": true,
        "openLmisAccessible": true,
        "supportedPrograms": [{
          "id": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
          "code": "PRG001",
          "name": "Family Planning",
          "programActive": true,
          "periodsSkippable": true,
          "showNonFullSupplyTab": true,
          "supportActive": true,
          "supportLocallyFulfilled": false,
          "supportStartDate": "2015-01-01"
        }],
        "geographicZone": {
          "id": "4df0cc89-8a71-450f-9c1a-29ceea1f14f3"
        },
        "operator": {
          "id": "9456c3e9-c4a6-4a28-9e08-47ceb16a4121"
        },
        "type": {
          "id": "ac1d268b-ce10-455f-bf87-9c667da8f060"
        },
        "extraData": { }
      }
      """
      Then I pause 15 seconds for right assignment regeneration
      And the facility should be created
      And I logout

      When I use API Key: 9a556033-ed13-4dde-9561-158469d15134
      Then the related location should be created

      When I have logged in as administrator
      And I update the facility:
      | goDownDate | active |
      | 2025-12-31 | false  |
      Then I pause 15 seconds for right assignment regeneration
      And the facility should be up-to-date
      And I logout

      When I use API Key: 9a556033-ed13-4dde-9561-158469d15134
      Then the related location should be updated

  Scenario: Location should be updated when geographic zone is updated
      Given I have logged in as administrator

      When I create a geographic zone:
      """
      {
        "code" : "CT-GZ",
        "name" : "Contract Test Geographic Zone",
        "level" : {
          "id" : "90e35999-a64f-4312-ba8f-bc13a1311c75"
        },
        "catchmentPopulation" : 1000,
        "latitude" : 0,
        "longitude" : 0,
        "parent" : {
          "id" : "4e471242-da63-436c-8157-ade3e615c848"
        },
        "extraData" : { }
      }
      """
      Then the geographic zone should be created
      And I logout

      When I use API Key: 9a556033-ed13-4dde-9561-158469d15134
      Then the related location should be created

      When I have logged in as administrator
      When I update the geographic zone:
      | latitude | longitude | catchmentPopulation |
      | 2        | 2         | 10000               |
      Then the geographic zone should be up-to-date
      And I logout

      When I use API Key: 9a556033-ed13-4dde-9561-158469d15134
      Then the related location should be updated

  Scenario: Facility should be updated when location is updated
      Given I use API Key: 9a556033-ed13-4dde-9561-158469d15134

      When I create a location:
      """
      {
        "resourceType": "Location",
        "name": "Contract Test FHIR Facility",
        "description": "This is an example location which should be added to the reference data service",
        "alias": ["CT-FHIR-F"],
        "partOf": {
          "reference": "http://nginx/api/Location/4df0cc89-8a71-450f-9c1a-29ceea1f14f3"
        },
        "physicalType": {
          "coding": [{
            "code": "si"
          }]
        },
        "status": "inactive"
      }
      """
      Then I pause 15 seconds for right assignment regeneration
      And the location should be created
      And the related facility should be created

      When I update the location:
      | status |
      | active |
      Then I pause 15 seconds for right assignment regeneration
      And the location should be up-to-date
      And the related facility should be updated

  Scenario: Geographic zone (without parent) should be updated when location is updated
      Given I use API Key: 9a556033-ed13-4dde-9561-158469d15134

      When I create a location:
      """
      {
        "resourceType": "Location",
        "name": "Contract Test FHIR Geographic Zone 1",
        "alias": ["CT-FHIR-GZ-1"],
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
      And the location should be created
      And the related geographic zone should be created

      When I update the location:
      | position.latitude | position.longitude |
      | 2                 | 2                  |
      And the location should be up-to-date
      And the related geographic zone should be updated

  Scenario: Geographic zone (with parent) should be updated when location is updated
      Given I use API Key: 9a556033-ed13-4dde-9561-158469d15134

      When I create a location:
      """
      {
        "resourceType": "Location",
        "name": "Contract Test FHIR Geographic Zone 2",
        "alias": ["CT-FHIR-GZ-2"],
        "partOf": {
          "reference": "http://nginx/api/Location/4e471242-da63-436c-8157-ade3e615c848"
        },
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
      And the location should be created
      And the related geographic zone should be created

      When I update the location:
      | position.latitude | position.longitude |
      | 2                 | 2                  |
      And the location should be up-to-date
      And the related geographic zone should be updated
