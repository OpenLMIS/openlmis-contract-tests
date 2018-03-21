@IdealStockAmountTests
Feature: Ideal Stock Amount Tests

  Scenario: User should be able to upload, download and retrieve ISAs
    Given I have logged in as administrator

    When I try to upload ISA CSV from org/openlmis/contract_tests/referencedata_tests/csv/isa_part1.csv
    Then I should get response with a number of rows uploaded
    And The number of rows uploaded should be 3500

    When I try to upload ISA CSV from org/openlmis/contract_tests/referencedata_tests/csv/isa_part2.csv
    Then I should get response with a number of rows uploaded
    And The number of rows uploaded should be 3500

    When I try to download ISA CSV
    Then I should get response with non-empty CSV file
    And The number of entries in the file should be at least 7000

    When I try to get ISA for
      | facilityId                           | commodityTypeId                      | processingPeriodId                   |
      | 7fc9bda8-ad8a-468d-8244-38e1918527d5 | 99ccf663-3304-44ae-b2e0-a67fd5511e2a | 516ac930-0d28-49f5-a178-64764e22b236 |
    Then I should get response with ISA page
      | facilityId                           | commodityTypeId                      | processingPeriodId                   | amount |
      | 7fc9bda8-ad8a-468d-8244-38e1918527d5 | 99ccf663-3304-44ae-b2e0-a67fd5511e2a | 516ac930-0d28-49f5-a178-64764e22b236 | 19300  |

    When I try to get all ISA
    Then I should get a page that have at least 7000 elements

    And I logout
