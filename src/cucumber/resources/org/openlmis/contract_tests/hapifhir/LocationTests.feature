@LocationTests @HapiFhirTests
Feature: Location Tests

  Scenario: Location should be synchronized with geographic zone (without parent)
      Given I have logged in as administrator

      When I create geographic zone:
      """
      {
        "code" : "CT-GZ-1",
        "name" : "Contract Test Geographic Zone 1",
        "level" : {
          "id" : "6b78e6c6-292e-4733-bb9c-3d802ad61206"
        },
        "catchmentPopulation" : 1000,
        "latitude" : 0,
        "longitude" : 0,
        "extraData" : { }
      }
      """
      Then geographic zone should be created
      And I logout

      When I am logged in as a service client
      Then related location should also be created

      When I have logged in as administrator
      And I update geographic zone:
      | latitude | longitude | catchmentPopulation |
      | 2        | 2         | 10000               |
      Then geographic zone should be up-to-date
      And I logout

      When I am logged in as a service client
      Then related location should also be up-to-date

  Scenario: Location should be synchronized with geographic zone (with parent)
      Given I have logged in as administrator

      When I try to find geographic zone for new geographic zone:
      | code    |
      | CT-GZ-1 |
      Then I should find geographic zone

      When I use id field of found geographic zone to set parent.id field in geographic zone
      And I create geographic zone:
      """
      {
        "code" : "CT-GZ-2",
        "name" : "Contract Test Geographic Zone 2",
        "level" : {
          "id" : "9b497d87-cdd9-400e-bb04-fae0bf6a9491"
        },
        "catchmentPopulation" : 1000,
        "latitude" : 0,
        "longitude" : 0,
        "extraData" : { }
      }
      """
      Then geographic zone should be created
      And I logout

      When I am logged in as a service client
      Then related location should also be created

      When I have logged in as administrator
      And I update geographic zone:
      | latitude | longitude | catchmentPopulation |
      | 2        | 2         | 10000               |
      Then geographic zone should be up-to-date
      And I logout

      When I am logged in as a service client
      Then related location should also be up-to-date

  Scenario: Location should be synchronized with facility
      Given I have logged in as administrator

      When I try to find geographic zone for new facility:
      | code    |
      | CT-GZ-2 |
      Then I should find geographic zone

      When I use id field of found geographic zone to set geographicZone.id field in facility
      And I create facility:
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
        "operator": {
          "id": "9456c3e9-c4a6-4a28-9e08-47ceb16a4121"
        },
        "type": {
          "id": "ac1d268b-ce10-455f-bf87-9c667da8f060"
        },
        "extraData": { }
      }
      """
      Then I pause for 45 seconds
      And facility should be created
      And I logout

      When I am logged in as a service client
      Then related location should also be created

      When I have logged in as administrator
      And I update facility:
      | goDownDate | active |
      | 2025-12-31 | false  |
      Then I pause for 45 seconds
      And facility should be up-to-date
      And I logout

      When I am logged in as a service client
      Then related location should also be up-to-date

  Scenario: Geographic zone (without parent) should be synchronized with location
      Given I am logged in as a service client

      When I create location:
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
      Then location should be created
      And related geographic zone should also be created

      When I update location:
      | position.latitude | position.longitude |
      | 2                 | 2                  |
      Then location should be up-to-date
      And related geographic zone should also be up-to-date

  Scenario: Geographic zone (with parent) should be synchronized with location
      Given I am logged in as a service client

      When I try to find location for new location:
      | name         |
      | CT-FHIR-GZ-1 |
      Then I should find location

      When I use id field of found location to set partOf.reference field in location
      And I create location:
      """
      {
        "resourceType": "Location",
        "name": "Contract Test FHIR Geographic Zone 2",
        "alias": ["CT-FHIR-GZ-2"],
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
      Then location should be created
      And related geographic zone should also be created

      When I update location:
      | position.latitude | position.longitude |
      | 2                 | 2                  |
      Then location should be up-to-date
      And related geographic zone should also be up-to-date

  Scenario: Facility should be synchronized with location
      Given I am logged in as a service client

      When I try to find location for new location:
      | name         |
      | CT-FHIR-GZ-2 |
      Then I should find location

      When I use id field of found location to set partOf.reference field in location
      And I create location:
      """
      {
        "resourceType": "Location",
        "name": "Contract Test FHIR Facility",
        "description": "This is an example location which should be added to the reference data service",
        "alias": ["CT-FHIR-F"],
        "physicalType": {
          "coding": [{
            "code": "si"
          }]
        },
        "status": "inactive"
      }
      """
      Then I pause for 45 seconds
      And location should be created
      And related facility should also be created

      When I update location:
      | status |
      | active |
      Then I pause for 45 seconds
      And location should be up-to-date
      And related facility should also be up-to-date
