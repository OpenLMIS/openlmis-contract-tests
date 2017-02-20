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

package org.openlmis.contract_tests.requisition;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openlmis.contract_tests.common.InitialDataException;
import org.openlmis.contract_tests.common.TestDatabaseConnection;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequisitionStepDefs {

  private Response requisitionResponse;
  private Response periodResponse;
  private String requisitionId;
  private String supervisoryNodeId;
  private String periodId;
  private JSONObject requisition;
  private JSONObject period;

  private TestDatabaseConnection databaseConnection;

  private static final String BASE_URL_OF_REQUISITION_SERVICE =
      baseUrlOfService("requisition") + "requisitions/";

  private static final String BASE_URL_OF_REFERENCEDATA_SERVICE =
      baseUrlOfService("referencedata");

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  private static final String INCORRECT_PERIOD_ERROR =
      "Error occurred while initiating requisition - incorrect suggested period.";

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Before("@RequisitionTests")
  public void setUp() throws InitialDataException {
    databaseConnection = new TestDatabaseConnection();
    //Because we have some initial data (bootstrap). We must remove it before loader.
    databaseConnection.removeData();
    databaseConnection.loadData();
  }

  @When("^I try to initiate a requisition with:$")
  public void tryToInitiateARequisition(DataTable argsList) {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    for (Map map : data) {
      requisitionResponse = given()
          .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
          .queryParam("program", map.get("programId"))
          .queryParam("facility", map.get("facilityId"))
          .queryParam("suggestedPeriod", map.get("periodId"))
          .queryParam("emergency", map.get("emergency"))
          .when()
          .post(BASE_URL_OF_REQUISITION_SERVICE + "initiate");
    }
  }

  @Then("^I should get response with the initiated requisition's id$")
  public void shouldGetResponseWithTheInitiatedRequisitionId() {
    requisitionResponse
        .then()
        .body("id", notNullValue());
    requisitionId = from(requisitionResponse.asString()).get("id");
  }

  @When("^I try to get requisition with id$")
  public void tryToGetRequisitionWithId() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(BASE_URL_OF_REQUISITION_SERVICE + requisitionId);
  }

  @Then("^I should get a requisition with:$")
  public void shouldGetRequisitionWith(DataTable argsList) {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    for (Map map : data) {
      requisitionResponse
          .then()
          .body("program.id", is(map.get("programId")))
          .body("facility.id", is(map.get("facilityId")))
          .body("processingPeriod.id", is(map.get("periodId")))
          .body("emergency", is(Boolean.parseBoolean(String.valueOf(map.get("emergency")))));
    }
  }

  @When("^I try update fields in requisition:$")
  public void tryUpdateFieldsInRequisition(DataTable argsList) throws Throwable {
    if (requisition == null || !requisition.get("id").equals(requisitionId)) {
      JSONParser parser = new JSONParser();
      requisition = (JSONObject) parser.parse(requisitionResponse.asString());
    }
    if (supervisoryNodeId != null) {
      requisition.replace("supervisoryNode", null, supervisoryNodeId);
    }

    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);

    for (Map map : data) {
      Map<String, String> hashMap = new HashMap<>(map);
      for (String fieldName : hashMap.keySet()) {
        updateFieldInRequisitionLineItem(requisition, fieldName, map.get(fieldName));
      }
    }

    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .body(requisition.toJSONString())
        .when()
        .put(BASE_URL_OF_REQUISITION_SERVICE + requisitionId);
  }

  @Then("^I should get a updated requisition with:$")
  public void shouldGetUpdatedRequisition(DataTable argsList) throws ParseException {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    JSONParser parser = new JSONParser();
    JSONObject requisition = (JSONObject) parser.parse(requisitionResponse.asString());
    Object requisitionLineItems = requisition.get(("requisitionLineItems"));
    JSONArray requisitionLines = (JSONArray) requisitionLineItems;

    int counter = 0;
    for (Map map : data) {
      Object oneLine = requisitionLines.get(counter);
      JSONObject requisitionLine = (JSONObject) oneLine;

      Map<String, String> hashMap = new HashMap<>(map);
      for (String fieldName : hashMap.keySet()) {
        assertThat(requisitionLine.get(fieldName).toString(), is(map.get(fieldName)));
      }
      counter++;
    }
  }

  @When("^I try to submit a requisition$")
  public void trySubmitRequisition() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .post(BASE_URL_OF_REQUISITION_SERVICE + requisitionId + "/submit");
  }

  @Then("^I should get a requisition with \"([^\"]*)\" ([a-zA-Z]+)$")
  public void shouldGetResponseWithSpecifiedFieldValue(String value, String fieldName) {
    requisitionResponse
        .then()
        .body(fieldName, is(value));
  }

  @When("^I try to authorize a requisition$")
  public void tryAuthorizeRequisition() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .post(BASE_URL_OF_REQUISITION_SERVICE + requisitionId + "/authorize");

    supervisoryNodeId = from(requisitionResponse.asString()).get("supervisoryNode");
  }

  @And("^I should get a requisition with(out|) supervisoryNode$")
  public void shouldGetRequisitionWithSupervisoryNode(String suffix) {
    Matcher matcher = (suffix.isEmpty()) ? notNullValue() : nullValue();
    requisitionResponse
        .then()
        .body("supervisoryNode", matcher);

    supervisoryNodeId = from(requisitionResponse.asString()).get("supervisoryNode");
  }

  @When("^I try to approve a requisition$")
  public void tryApproveRequisition() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .post(BASE_URL_OF_REQUISITION_SERVICE + requisitionId + "/approve");
  }

  @When("^I try to reject authorized requisition$")
  public void tryRejectRequisition() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .put(BASE_URL_OF_REQUISITION_SERVICE + requisitionId + "/reject");
  }

  @When("^I try to get period with id:$")
  public void tryGetPeriod(DataTable argsList) throws ParseException {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    for (Map map : data) {
      periodResponse = given()
          .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
          .contentType(ContentType.JSON)
          .when()
          .get(BASE_URL_OF_REFERENCEDATA_SERVICE + "processingPeriods/" + map.get("periodId"));
    }
  }

  @Then("^I should get response with the period id$")
  public void shouldGetResponseWithThePeriodId() {
    periodResponse
        .then()
        .contentType(ContentType.JSON)
        .body("id", notNullValue());
    periodId = from(periodResponse.asString()).get("id");
  }

  @When("^I try update period to current date$")
  public void tryUpdateDateInPeriod() throws ParseException {
    tryUpdatePeriod(0);
  }

  @When("^I try update period to future date$")
  public void tryUpdateDateToFutureDayInPeriod() throws ParseException {
    tryUpdatePeriod(120);
  }

  @Then("^I should get response of incorrect period$")
  public void shouldGetResponseOfIncorrectPeriod() {
    requisitionResponse
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("message",
            is(INCORRECT_PERIOD_ERROR));
  }

  @When("^I try to delete initiated requisition$")
  public void tryDeleteRequisition() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .delete(BASE_URL_OF_REQUISITION_SERVICE + requisitionId);
  }

  @Then("^I should get response of deleted requisition$")
  public void shouldGetResponseWithDeletedRequisition() {
    requisitionResponse
        .then()
        .statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @Then("^I should get response of not found$")
  public void shouldGetResponseOfNotFound() {
    requisitionResponse
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @When("^I try to convert requisition to order$")
  public void tryConvertRequisitionToOrder() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .contentType(ContentType.JSON)
        .body(createBodyForConvertToOrder())
        .post(BASE_URL_OF_REQUISITION_SERVICE + "convertToOrder");
  }

  private void updateFieldInRequisitionLineItem(JSONObject requisition,
                                                String keyToUpdate, Object newValue) {
    Object requisitionLineItems = requisition.get(("requisitionLineItems"));
    JSONArray requisitionLines = (JSONArray) requisitionLineItems;

    for (Object requisitionLine : requisitionLines) {
      JSONObject requisitionLineAsJson = (JSONObject) requisitionLine;
      requisitionLineAsJson.replace(keyToUpdate, newValue);
    }
  }

  private void tryUpdatePeriod(int daysToAdd) throws ParseException {
    if (period == null) {
      JSONParser parser = new JSONParser();
      period = (JSONObject) parser.parse(periodResponse.asString());
    }
    period.replace("startDate", LocalDate.now().plusDays(daysToAdd).toString());
    period.replace("endDate", LocalDate.now().plusDays(daysToAdd + 30).toString());

    periodResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .body(period.toJSONString())
        .when()
        .put(BASE_URL_OF_REFERENCEDATA_SERVICE + "processingPeriods/" + periodId);
  }

  private JSONArray createBodyForConvertToOrder() {
    JSONObject json = new JSONObject();
    json.put("requisitionId", requisitionId);
    //supplyingDepot from demo-data
    json.put("supplyingDepotId", "19121381-9f3d-4e77-b9e5-d3f59fc1639e");
    JSONArray array = new JSONArray();
    array.add(json);
    return array;
  }

  @After("@RequisitionTests")
  public void cleanUp() throws InitialDataException {
    databaseConnection.removeData();
  }

}
