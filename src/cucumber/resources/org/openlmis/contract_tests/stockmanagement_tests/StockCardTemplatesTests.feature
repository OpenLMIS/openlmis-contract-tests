@StockCardTemplatesTests
Feature: Stock Card Templates Tests


  Scenario: administrator user should be able to get the default stock card template
    Given I have logged in as administrator

    When I try to get the default stock card template
    Then I should get the default stock card template


  Scenario: administrator user should be able to create a stock card templates
    Given I have logged in as administrator

    When I try to create a stock card template
      """
      {
        "programId": "dce17f2e-af3e-40ad-8e00-3496adef44c3",
        "facilityTypeId": "663b1d34-cc17-4d60-9619-e553e45aa441",
        "stockCardFields": [
          {
            "name": "packSize",
            "displayOrder": 1,
            "displayed": true
          },
          {
            "name": "donor",
            "displayOrder": 0,
            "displayed": false
          },
          {
            "name": "maxMonthsOfStock",
            "displayOrder": 0,
            "displayed": false
          },
          {
            "name": "minMonthsOfStock",
            "displayOrder": 0,
            "displayed": false
          }
        ],
        "stockCardLineItemFields": [
          {
            "name": "documentNumber",
            "displayOrder": 0,
            "displayed": false
          },
          {
            "name": "receivedFrom",
            "displayOrder": 0,
            "displayed": false
          },
          {
            "name": "issuedTo",
            "displayOrder": 0,
            "displayed": false
          },
          {
            "name": "adjustmentReason",
            "displayOrder": 1,
            "displayed": true
          },
          {
            "name": "signature",
            "displayOrder": 0,
            "displayed": false
          }
        ]
      }
      """
    Then I should get response of created

  Scenario: administrator user should be able to get a specific stock card templates
    Given I have logged in as administrator

    When I try to get a stock card template with programId: dce17f2e-af3e-40ad-8e00-3496adef44c3, facilityTypeId: 663b1d34-cc17-4d60-9619-e553e45aa441
    Then I should get a stock card template with programId: dce17f2e-af3e-40ad-8e00-3496adef44c3, facilityTypeId: 663b1d34-cc17-4d60-9619-e553e45aa441







