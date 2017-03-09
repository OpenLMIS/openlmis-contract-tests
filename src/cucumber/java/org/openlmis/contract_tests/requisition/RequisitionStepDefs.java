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
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openlmis.contract_tests.common.InitialDataException;
import org.openlmis.contract_tests.common.TestDatabaseConnection;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class RequisitionStepDefs {

  private Response requisitionTemplateResponse;
  private Response requisitionResponse;
  private Response periodResponse;
  private String requisitionId;
  private String supervisoryNodeId;
  private String periodId;
  private JSONObject requisition;
  private JSONObject period;

  private String requisitionTemplateProgram;
  private DataTable requisitionTemplateUpdateData;

  private TestDatabaseConnection databaseConnection;

  public static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  public static final String BASE_URL_OF_REFERENCEDATA_SERVICE =
      baseUrlOfService("referencedata");

  private static final String BASE_URL_OF_REQUISITION_TEMPLATE_SERVICE =
      baseUrlOfService("requisition") + "requisitionTemplates/";

  private static final String BASE_URL_OF_REQUISITION_SERVICE =
      baseUrlOfService("requisition") + "requisitions/";

  private static final String INCORRECT_PERIOD_ERROR =
      "Error occurred while initiating requisition - incorrect suggested period.";

  private static final Integer FOUR_MONTHS = 120;

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
          .queryParam("suggestedPeriod", map.getOrDefault("periodId", periodId))
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
          .body("processingPeriod.id", is(map.getOrDefault("periodId", periodId)))
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

    data.forEach(map -> map.entrySet().forEach(entry ->
        updateFieldInRequisitionLineItem(requisition, entry.getKey(), entry.getValue())
    ));

    given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .body(requisition.toJSONString())
        .when()
        .put(BASE_URL_OF_REQUISITION_SERVICE + requisitionId).then()
        .statusCode(200);
  }

  @Then("^I should get a updated requisition with:$")
  public void shouldGetUpdatedRequisition(DataTable argsList) throws ParseException {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);

    JSONParser parser = new JSONParser();
    JSONObject root = (JSONObject) parser.parse(requisitionResponse.asString());
    JSONArray lineItems = (JSONArray) root.get("requisitionLineItems");

    data.forEach(map -> lineItems.forEach(line -> {
      JSONObject item = (JSONObject) line;

      map.entrySet().forEach(entry -> assertThat(
          item.toJSONString(), Objects.toString(item.get(entry.getKey())), is(entry.getValue()))
      );
    }));
  }

  @Then("^I should get updated requisition with proper total cost$")
  public void shouldGetUpdatedRequisitionWithProperTotalCost() throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject requisition = (JSONObject) parser.parse(requisitionResponse.asString());
    JSONArray requisitionLines = (JSONArray) requisition.get("requisitionLineItems");

    for (Object requisitionLineObject : requisitionLines) {
      JSONObject requisitionLine = (JSONObject) requisitionLineObject;
      BigDecimal packsToShip = new BigDecimal((Long) requisitionLine.get("packsToShip"));
      BigDecimal pricePerPack = new BigDecimal((Double) requisitionLine.get("pricePerPack"));

      BigDecimal totalCost = new BigDecimal((Double) requisitionLine.get("totalCost")).setScale(
          2, RoundingMode.HALF_UP);

      BigDecimal calculatedTotalCost = packsToShip.multiply(
          pricePerPack).setScale(2, RoundingMode.HALF_UP);

      assertEquals(calculatedTotalCost, totalCost);
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

  @When("^I try to skip initiated requisition$")
  public void trySkipRequisition() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .put(BASE_URL_OF_REQUISITION_SERVICE + requisitionId + "/skip");
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

  @When("^I try to get periods by program (.*) and facility (.*)$")
  public void getPeriodsByProgramAndFacility(String programId, String facilityId) {
    periodResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("programId", programId)
        .queryParam("facilityId", facilityId)
        .when()
        .get(BASE_URL_OF_REFERENCEDATA_SERVICE + "processingPeriods/search");
  }

  @When("^I try to delete current period")
  public void tryDeletePeriod() throws ParseException {
    JSONParser parser = new JSONParser();
    JSONArray periods = (JSONArray) parser.parse(periodResponse.asString());
    String id = ProcessingPeriodUtils.findPeriodByDate(periods, LocalDate.now());

    periodResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .when()
        .delete(BASE_URL_OF_REFERENCEDATA_SERVICE + "processingPeriods/" + id);
  }

  @Then("^I should get response with the period id$")
  public void shouldGetResponseWithThePeriodId() {
    periodResponse
        .then()
        .contentType(ContentType.JSON)
        .body("id", notNullValue());
    periodId = from(periodResponse.asString()).get("id");
  }

  @Then("^I should get response with status ([0-9]+)$")
  public void shouldGetResponseWithStatus(String statusCode) {
    periodResponse
        .then()
        .statusCode(Integer.parseInt(statusCode));
  }

  @When("^I try to get or create a period with current date and schedule (.*)$")
  public void tryCreateAPeriodWithCurrentDate(String id) throws ParseException {
    periodId = getOrCreatePeriod(0, id);
  }

  @When("^I try to get or create a period with future date and schedule (.*)$")
  public void tryCreateAPeriodWithFutureDate(String id) throws ParseException {
    periodId = getOrCreatePeriod(FOUR_MONTHS, id);
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

  @When("^I try get a requisition template for a program (.*)$")
  public void getRequisitionTemplateForProgram(String program) {
    requisitionTemplateProgram = program;

    requisitionTemplateResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("program", requisitionTemplateProgram)
        .when()
        .get(BASE_URL_OF_REQUISITION_TEMPLATE_SERVICE + "search");
  }

  @Then("^I should get response with requisition template$")
  public void getRequisitionTemplateForProgram() {
    requisitionTemplateResponse
        .then()
        .statusCode(200)
        .body("programId", is(requisitionTemplateProgram));
  }

  @When("^I try to update a requisition template:$")
  public void updateRequisitionTemplate(DataTable data) throws ParseException {
    requisitionTemplateUpdateData = data;

    JSONParser parser = new JSONParser();
    JSONObject template = (JSONObject) parser.parse(requisitionTemplateResponse.asString());
    String id = template.get("id").toString();

    requisitionTemplateUpdateData
        .asMaps(String.class, Object.class)
        .forEach(map -> map.forEach(template::replace));

    requisitionTemplateResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("id", id)
        .contentType(ContentType.JSON)
        .body(template.toJSONString())
        .when()
        .put(BASE_URL_OF_REQUISITION_TEMPLATE_SERVICE + id);
  }

  @Then("^I should get response that template has been updated$")
  public void checkIfRequisitionTemplateWasUpdated() {
    ValidatableResponse validatableResponse = requisitionTemplateResponse
        .then()
        .statusCode(200);

    requisitionTemplateUpdateData
        .asMaps(String.class, String.class)
        .forEach(map -> map.forEach(
            (key, value) -> validatableResponse.body(key, is(hasToString(value)))
        ));
  }

  private void updateFieldInRequisitionLineItem(JSONObject requisition,
                                                String keyToUpdate, Object newValue) {
    JSONArray requisitionLineItems = (JSONArray) requisition.get("requisitionLineItems");

    for (Object requisitionLine : requisitionLineItems) {
      JSONObject requisitionLineAsJson = (JSONObject) requisitionLine;
      requisitionLineAsJson.replace(keyToUpdate, newValue);
    }
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

  private String getOrCreatePeriod(int daysToAdd, String scheduleId) throws ParseException {
    LocalDate periodDate = LocalDate.now().plusDays(daysToAdd);
    JSONArray periods = ProcessingPeriodUtils.getExistingPeriods(scheduleId);

    // first, try to find a period matching specified date among existing ones
    String periodId = ProcessingPeriodUtils.findPeriodByDate(periods, periodDate);
    if (periodId == null) {
      // period not found, create it based on the latest one
      JSONObject period = (JSONObject) periods.get(periods.size() - 1);
      LocalDate endDate = ProcessingPeriodUtils.parseDate(period.get("endDate"));

      while (!ProcessingPeriodUtils.isWithinRange(periodDate, period)) {
        UUID id = UUID.randomUUID();
        LocalDate startDate = endDate.plusDays(1);
        endDate = startDate.plusDays(30);
        periodId = createPeriod(period, id, startDate, endDate);
      }
    }
    return periodId;
  }

  private String createPeriod(JSONObject period, UUID id,
                              LocalDate startDate, LocalDate endDate) throws ParseException {
    DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;

    period.replace("id", id.toString());
    period.replace("startDate", startDate.format(dtf));
    period.replace("endDate", endDate.format(dtf));

    periodResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .body(period.toJSONString())
        .when()
        .put(BASE_URL_OF_REFERENCEDATA_SERVICE + "processingPeriods/" + id);
    periodResponse
        .then()
        .statusCode(200);

    JSONParser parser = new JSONParser();
    period = (JSONObject) parser.parse(periodResponse.asString());

    return (String) period.get("id");
  }
}
