/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.contract_tests.referencedata;


import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdealStockAmountStepDefs {
  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";
  private static final String AMOUNT_FIELD = "amount";

  private static final String BASE_URL_OF_REFERENCEDATA_SERVICE = baseUrlOfService("referencedata");

  private static final String ISA_URL = BASE_URL_OF_REFERENCEDATA_SERVICE
      + "idealStockAmounts";

  private static final String ID = "id";
  private static final String CONTENT = "content";
  private static final String FACILITY = "facility";
  private static final String COMMODITY_TYPE = "commodityType";
  private static final String PROCESSING_PERIOD = "processingPeriod";
  private static final String AMOUNT = "amount";
  private static final String FACILITY_ID = "facilityId";
  private static final String COMMODITY_TYPE_ID = "commodityTypeId";
  private static final String PROCESSING_PERIOD_ID = "processingPeriodId";

  private Response isaUploadResponse;
  private Response isaDownloadResponse;
  private Response isaSearchResponse;
  private Response isaGetAllResponse;
  private byte[] downloadedIsaFile;

  private Integer rowsUploaded;

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @When("^I try to upload ISA CSV from (\\S+)$")
  public void tryToUploadIsaCsv(String path) {
    ClassLoader classLoader = getClass().getClassLoader();
    File isaCsvFile = new File(classLoader.getResource(path).getFile());

    isaUploadResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("format", "csv")
        .multiPart(isaCsvFile)
        .when()
        .post(ISA_URL);
  }

  @Then("^I should get response with a number of rows uploaded$")
  public void shouldGetResponseWithNumberOfRows() {
    String uploadResult = isaUploadResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(AMOUNT_FIELD, notNullValue())
        .extract()
        .asString();

    rowsUploaded = from(uploadResult).get(AMOUNT_FIELD);
  }

  @Then("^The number of rows uploaded should be (\\d+)$")
  public void numberOfRowsShouldBe(Integer rows) {
    assertEquals(rows, rowsUploaded);
  }

  @When("^I try to download ISA CSV$")
  public void downloadIsaCsv() {
    isaDownloadResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("format", "csv")
        .when()
        .get(ISA_URL);
  }

  @Then("^I should get response with non-empty CSV file$")
  public void getNonEmptyResponse() {
    downloadedIsaFile = isaDownloadResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .asByteArray();

    assertNotNull(downloadedIsaFile);
    assertTrue(downloadedIsaFile.length > 0);
  }

  @Then("^The number of entries in the file should be (\\d+)$")
  public void numberOfEntriesInFileShouldBe(int expected) {
    String csvDownload = isaDownloadResponse.asString();
    Matcher m = Pattern.compile("\r\n|\r|\n").matcher(csvDownload);
    int lines = 0;
    while (m.find()) {
      lines ++;
    }

    // We subtract one from lines count, to ignore header line
    assertEquals(expected, lines - 1);
  }

  @When("^I try to get ISA for$")
  public void getIsaFor(DataTable requestTable) {
    Map<String, String> request = requestTable.asMaps(String.class, String.class).get(0);

    isaSearchResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam(FACILITY_ID, request.get(FACILITY_ID))
        .queryParam(COMMODITY_TYPE_ID, request.get(COMMODITY_TYPE_ID))
        .queryParam(PROCESSING_PERIOD_ID, request.get(PROCESSING_PERIOD_ID))
        .when()
        .get(ISA_URL);
  }

  @Then("^I should get response with ISA page")
  public void isaPageResponse(DataTable responseTable) throws ParseException {
    String response = isaSearchResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .extract()
        .asString();

    assertNotNull(response);

    Map<String, String> expected = responseTable.asMaps(String.class, String.class).get(0);

    JSONParser parser = new JSONParser();
    JSONObject object = (JSONObject) parser.parse(response);

    JSONArray content = (JSONArray) object.get(CONTENT);
    assertEquals(1, content.size());

    JSONObject isa = (JSONObject) content.get(0);
    String facilityId = (String) ((JSONObject) isa.get(FACILITY)).get(ID);
    String commodityTypeId = (String) ((JSONObject) isa.get(COMMODITY_TYPE)).get(ID);
    String processingPeriodId = (String) ((JSONObject) isa.get(PROCESSING_PERIOD)).get(ID);
    Long amount = (Long) (isa.get(AMOUNT));

    assertEquals(expected.get(FACILITY_ID), facilityId);
    assertEquals(expected.get(COMMODITY_TYPE_ID), commodityTypeId);
    assertEquals(expected.get(PROCESSING_PERIOD_ID), processingPeriodId);
    assertEquals(Long.valueOf(expected.get(AMOUNT)), amount);
  }

  @When("^I try to get all ISA$")
  public void getAllIsa() {
    isaGetAllResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(ISA_URL);
  }

  @Then("^I should get a page with total elements set to (\\d+)$")
  public void getPageWithTotalElements(Long expectedTotal) throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject object = (JSONObject) parser.parse(isaGetAllResponse.asString());

    Long totalElements = (Long) object.get("totalElements");
    assertEquals(expectedTotal, totalElements);
  }


}
