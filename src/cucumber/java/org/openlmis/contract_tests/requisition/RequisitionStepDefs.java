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

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openlmis.contract_tests.common.JsonFieldHelper;

public class RequisitionStepDefs {

  static final String BASE_URL_OF_REFERENCEDATA_SERVICE =
      baseUrlOfService("referencedata");
  private static final String REQUISITION_LINE_ITEMS = "requisitionLineItems";
  private static final String REQUISITIONS_FOR_APPROVAL = "requisitionsForApproval/";
  private static final String REQUISITIONS_FOR_CONVERT = "requisitionsForConvert/";
  private static final String BASE_URL_OF_REQUISITION_TEMPLATE_SERVICE =
      baseUrlOfService("requisition") + "requisitionTemplates/";
  private static final String BASE_URL_OF_REQUISITION_SERVICE =
      baseUrlOfService("requisition") + "requisitions/";
  private static final String INCORRECT_PERIOD_ERROR =
      "Error occurred while initiating requisition - incorrect suggested period.";
  private static final int FOUR_MONTHS = 4;

  private Response requisitionTemplateResponse;
  private Response requisitionResponse;
  private Response periodResponse;
  private String requisitionId;
  private String supervisoryNodeId;
  private String periodId;
  private JSONObject requisition;
  private int numberOfElements;

  private String requisitionTemplate;
  private DataTable requisitionTemplateUpdateData;
  private Map<String, Map<String, String>> requisitionTemplateColumnsData = new HashMap<>();


  @Before("@RemoveCurrentPeriod")
  public void setUp() {
      try {
        Process proc = Runtime.getRuntime().exec("/app/remove_current_period.sh");

        StreamGobbler streamGobbler = new StreamGobbler(proc, System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);

        proc.waitFor();
      } catch (Exception ex) {
        System.err.println("Removing current period failed with message: " + ex);
      }
  }

  @When("^I try to initiate a requisition with:$")
  public void tryToInitiateARequisition(DataTable argsList) {
    List<Map<String, String>> data = argsList.asMaps();
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
    List<Map<String, String>> data = argsList.asMaps();
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
    updateRequisition();

    List<Map<String, String>> data = argsList.asMaps();

    data.forEach(map -> map.entrySet().forEach(entry -> {
          if (Arrays.asList("datePhysicalStockCountCompleted","supervisoryNode")
              .contains(entry.getKey())) {
            requisition.put(entry.getKey(), entry.getValue());
          } else {
            updateFieldInRequisitionLineItem(requisition, entry.getKey(), entry.getValue());
          }
        }
    ));

    given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .body(requisition.toJSONString())
        .when()
        .put(BASE_URL_OF_REQUISITION_SERVICE + requisitionId).then()
        .statusCode(SC_OK);
  }

  @When("^I try to update fields for product id (.+):$")
  public void tryUpdateFieldInRequisition(String product, DataTable argsList) throws Throwable {
    updateRequisition();

    List<Map<String, String>> data = argsList.asMaps();

    data.forEach(map -> map.forEach((key, value) ->
        updateFieldInRequisitionLineItem(requisition, key, value, product)
    ));

    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .body(requisition.toJSONString())
        .when()
        .put(BASE_URL_OF_REQUISITION_SERVICE + requisitionId);
  }

  @When("^I try to add products to requisition:$")
  public void tryAddProductsToRequisition(DataTable argsList) throws Throwable {
    updateRequisition();

    List<Map<String, String>> data = argsList.asMaps();

    addProducts(requisition);

    data.forEach(map -> map.forEach((key, value) ->
        updateFieldInRequisitionLineItem(requisition, key, value)));

    given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .body(requisition.toJSONString())
        .when()
        .put(BASE_URL_OF_REQUISITION_SERVICE + requisitionId)
        .then()
        .statusCode(SC_OK);
  }

  @Then("^I should get requisition response with status ([0-9]+)$")
  public void tryUpdateFieldInRequisition(String status) throws Throwable {
    requisitionResponse.then()
        .statusCode(Integer.parseInt(status));
  }

  @Then("^I should get a updated requisition with:$")
  public void shouldGetUpdatedRequisition(DataTable argsList) throws ParseException {
    List<Map<String, String>> data = argsList.asMaps();

    JSONParser parser = new JSONParser();
    JSONObject root = (JSONObject) parser.parse(requisitionResponse.asString());
    JSONArray lineItems = (JSONArray) root.get(REQUISITION_LINE_ITEMS);

    data.forEach(map -> lineItems.forEach(line -> {
      JSONObject item = (JSONObject) line;

      map.entrySet().forEach(entry -> assertThat(
          item.toJSONString(), Objects.toString(item.get(entry.getKey())), is(entry.getValue()))
      );
    }));
  }

  @Then("^I should get updated requisition with product id (.+):$")
  public void shouldGetUpdatedRequisition(String product, DataTable argsList) throws ParseException {
    Map<String, String> fieldMap = argsList.asMaps().get(0);

    JSONParser parser = new JSONParser();
    JSONObject root = (JSONObject) parser.parse(requisitionResponse.asString());
    JSONArray lineItems = (JSONArray) root.get(REQUISITION_LINE_ITEMS);

    lineItems.forEach(line -> {
      JSONObject item = (JSONObject) line;
      JSONObject orderable = (JSONObject) item.get("orderable");
      String id = (String) orderable.get("id");

      if (product.equals(id)) {
        fieldMap.forEach((key, value) -> assertThat(
            item.toJSONString(), Objects.toString(item.get(key)), is(value)));
      }
    });
  }

  @Then("^I should get updated requisition with proper total cost$")
  public void shouldGetUpdatedRequisitionWithProperTotalCost() throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject requisition = (JSONObject) parser.parse(requisitionResponse.asString());
    JSONArray requisitionLines = (JSONArray) requisition.get(REQUISITION_LINE_ITEMS);

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

    requisitionResponse
        .then()
        .statusCode(SC_OK);
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

    requisitionResponse
        .then()
        .statusCode(SC_OK);

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

    requisitionResponse
        .then()
        .statusCode(SC_OK);
  }

  @When("^I try to skip initiated requisition$")
  public void trySkipRequisition() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .put(BASE_URL_OF_REQUISITION_SERVICE + requisitionId + "/skip");

    requisitionResponse
        .then()
        .statusCode(SC_OK);
  }

  @When("^I try to reject authorized requisition$")
  public void tryRejectRequisition() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .put(BASE_URL_OF_REQUISITION_SERVICE + requisitionId + "/reject");

    requisitionResponse
        .then()
        .statusCode(SC_OK);
  }

  @When("^I try to get period with id:$")
  public void tryGetPeriod(DataTable argsList) throws ParseException {
    List<Map<String, String>> data = argsList.asMaps();
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
        .get(BASE_URL_OF_REFERENCEDATA_SERVICE + "processingPeriods/");
  }

  @When("^I try to delete current period")
  public void tryDeletePeriod() throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject page = (JSONObject) parser.parse(periodResponse.asString());
    JSONArray periods = (JSONArray) page.get("content");
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
    getOrCreatePeriod(0, id);
  }

  @When("^I try to get or create a period with future date and schedule (.*)$")
  public void tryCreateAPeriodWithFutureDate(String id) throws ParseException {
    getOrCreatePeriod(FOUR_MONTHS, id);
  }

  @Then("^I should get response of incorrect period$")
  public void shouldGetResponseOfIncorrectPeriod() {
    requisitionResponse
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("message",
            is(INCORRECT_PERIOD_ERROR));
  }

  @When("^I try to delete requisition$")
  public void tryDeleteRequisition() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .delete(BASE_URL_OF_REQUISITION_SERVICE + requisitionId);

    requisitionResponse
        .then()
        .statusCode(SC_NO_CONTENT);
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

  @When("^I try to convert requisition with:$")
  public void tryConvertRequisitionToOrder(DataTable table) {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .contentType(ContentType.JSON)
        .body(createBodyForConvertToOrder(table))
        .post(BASE_URL_OF_REQUISITION_SERVICE + "convertToOrder");
  }

  @Then("^I should get response of order created$")
  public void shouldGetResponseOfOrderCreated() {
    requisitionResponse
        .then()
        .statusCode(SC_CREATED);
  }

  @When("^I try get a requisition templates$")
  public void tryGetRequisitionTemplates() {
    requisitionTemplateResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(BASE_URL_OF_REQUISITION_TEMPLATE_SERVICE);
  }

  @Then("^I should get response with requisition template for a program (.*) and facility type (.*)$")
  public void shouldGetRequisitionTemplateForProgram(String programId, String facilityTypeId) throws ParseException {
    requisitionTemplateResponse
        .then()
        .statusCode(SC_OK)
        .body("$.isEmpty()", is(false));

    JSONParser parser = new JSONParser();
    JSONArray templates = (JSONArray) parser.parse(requisitionTemplateResponse.asString());
    for (Object element : templates) {
      JSONObject template = (JSONObject) element;
      JSONObject program = (JSONObject) template.get("program");
      JSONArray facilityTypes = (JSONArray) template.get("facilityTypes");

      for (Object facilityType : facilityTypes) {
        JSONObject type = (JSONObject) facilityType;
        if (programId.equals(program.get("id").toString()) &&
            facilityTypeId.equals(type.get("id").toString())) {
          requisitionTemplate = template.toString();
          break;
        }
      }

      if (isNotBlank(requisitionTemplate)) {
        break;
      }
    }

    assertThat(requisitionTemplate, is(notNullValue()));
  }

  @When("^I try to update a requisition template:$")
  public void updateRequisitionTemplate(DataTable data) throws ParseException {
    requisitionTemplateUpdateData = data;

    JSONParser parser = new JSONParser();
    JSONObject template = (JSONObject) parser.parse(requisitionTemplate);
    String id = template.get("id").toString();

    requisitionTemplateUpdateData
        .asMaps()
        .forEach(map -> map.forEach(template::replace));

    requisitionTemplateResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("id", id)
        .contentType(ContentType.JSON)
        .body(template.toJSONString())
        .when()
        .put(BASE_URL_OF_REQUISITION_TEMPLATE_SERVICE + id);
  }

  @When("^I try to update columns:$")
  public void updateRequisitionTemplateColumns(DataTable data) throws ParseException {
    Map<String, Map> columnsData = data.asMap(String.class, Map.class);
    columnsData.forEach(requisitionTemplateColumnsData::put);

    JSONParser parser = new JSONParser();
    JSONObject template = (JSONObject) parser.parse(requisitionTemplate);
    String id = template.get("id").toString();

    JSONObject columnMaps = (JSONObject) template.get("columnsMap");
    columnsData.forEach((key, value) -> {
      JSONObject column = (JSONObject) columnMaps.get(key);
      value.forEach(column::replace);
    });

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
        .statusCode(SC_OK);

    if (null != requisitionTemplateUpdateData) {
      requisitionTemplateUpdateData
          .asMaps()
          .forEach(map -> map.forEach(
              (key, value) -> validatableResponse.body(key, is(hasToString(value)))
          ));
    }

    if (!requisitionTemplateColumnsData.isEmpty()) {
      for (Entry<String, Map<String, String>> entry : requisitionTemplateColumnsData.entrySet()) {
        String prefix = "columnsMap." + entry.getKey() + ".";

        entry
            .getValue()
            .forEach((key, value) ->
                validatableResponse.body(prefix + key, is(hasToString(value))));
      }
    }
  }

  @When("^I try to find requisitions for convert without request parameters$")
  public void tryToGetRequisitionsForConvert() {
    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(BASE_URL_OF_REQUISITION_SERVICE + REQUISITIONS_FOR_CONVERT);
  }

  @When("^I try to find requisitions for convert with request parameters:$")
  public void tryToGetRequisitionsForConvert(DataTable argsList) {
    Map<String, String> data = argsList.asMaps().get(0);

    requisitionResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("programId", data.get("programId"))
        .queryParam("facilityId", data.get("facilityId"))
        .when()
        .get(BASE_URL_OF_REQUISITION_SERVICE + REQUISITIONS_FOR_CONVERT);
  }

  @Then("^I should get a page with ([0-9]+) requisitions$")
  public void shouldGetPageWithRequisitions(String number) {
    int intNumber = Integer.parseInt(number);
    requisitionResponse
        .then()
        .body("numberOfElements", is(intNumber));

    numberOfElements = intNumber;
  }

  @Then("^I should get requisitions with program \"([^\"]*)\"$")
  public void shouldGetRequisitionsWithProgram(String program) {
    for (int i = 0; i < numberOfElements; i++) {
      requisitionResponse
          .then()
          .body("content[" + i + "].requisition.program.name", is(program));
    }
  }

  @Then("^I should get requisitions with facility \"([^\"]*)\"$")
  public void shouldGetRequisitionsWithFacility(String facility) {
    for (int i = 0; i < numberOfElements; i++) {
      requisitionResponse
          .then()
          .body("content[" + i + "].requisition.facility.code", is(facility));
    }
  }

  @Then("^I should get requisitions with facilities \"([^\"]*)\"$")
  public void shouldGetRequisitionsWithFacilitis(String facilities) {
    List<String> facilityList = Arrays.asList(facilities.split(","));

    for (int i = 0; i < numberOfElements; i++) {
      String code = from(requisitionResponse.asString()).get("content[" + i + "].requisition.facility.code");
      assertNotNull(code);
      assertTrue(facilityList.contains(code));
    }
  }

  @When("^I try to find requisitions for approval with request parameters:$")
  public void tryToGetRequisitionsForApproval(DataTable argsList) {
    Map<String, String> data = argsList.asMaps().get(0);

    RequestSpecification specification = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN);

    data.forEach(specification::queryParam);

    requisitionResponse = specification
        .when()
        .get(BASE_URL_OF_REQUISITION_SERVICE + REQUISITIONS_FOR_APPROVAL);
  }

  @When("^I try to find a requisition from the response with parameters:$")
  public void tryToFindRequisitionFromResponse(DataTable table) throws ParseException {
    Map<String, String> params = table.asMaps().get(0);

    JSONParser parser = new JSONParser();
    JSONObject page = (JSONObject) parser.parse(requisitionResponse.asString());
    JSONArray content = (JSONArray) page.get("content");

    for (int i = 0, size = content.size(); i < size; ++i) {
      requisition = (JSONObject) content.get(i);
      boolean match = true;

      for (Entry<String, String> entry : params.entrySet()) {
        match = match
            && entry.getValue().equals(JsonFieldHelper.getField(requisition, entry.getKey()));
      }

      if (match) {
        break;
      } else {
        requisition = null;
      }
    }
  }

  @Then("^I should get the requisition$")
  public void shouldGetRequisition() {
    assertThat("Can't find the correct requisition", requisition, is(notNullValue()));
    requisitionId = String.valueOf(requisition.get("id"));
  }

  @Then("^the requisition should have line items with values:$")
  public void requisitionShouldHaveLineItems(DataTable table) throws ParseException {
    List<Map<String, String>> params = table.asMaps();

    JSONParser parser = new JSONParser();
    JSONObject req = (JSONObject) parser.parse(requisitionResponse.asString());
    JSONArray lineItems = (JSONArray) req.get(REQUISITION_LINE_ITEMS);
    Iterator iterator = lineItems.iterator();

    while (iterator.hasNext()) {
      JSONObject lineItem = (JSONObject) iterator.next();

      for (Map<String, String> values : params) {
        boolean match = true;

        for (Entry<String, String> entry : values.entrySet()) {
          Object lineItemField = JsonFieldHelper.getField(lineItem, entry.getKey());

          match = null == lineItemField
              ? match && (null == entry.getValue() || "".equals(entry.getValue()))
              : match && entry.getValue().equals(lineItemField.toString());
        }

        if (match) {
          iterator.remove();
          break;
        }
      }
    }

    assertThat("Can't match line items: " + lineItems, lineItems.size(), is(0));
  }

  private void addProducts(JSONObject requisition) {
    JSONArray fullSupplyProducts = (JSONArray) requisition.get("availableFullSupplyProducts");
    JSONArray lineItems = new JSONArray();

    for (Object product : fullSupplyProducts) {
      JSONObject lineItem = new JSONObject();
      lineItem.put("orderable", product);

      lineItems.add(lineItem);
    }

    requisition.put(REQUISITION_LINE_ITEMS, lineItems);
  }

  private void updateFieldInRequisitionLineItem(JSONObject requisition,
                                                Object keyToUpdate, Object newValue) {
    JSONArray requisitionLineItems = (JSONArray) requisition.get(REQUISITION_LINE_ITEMS);

    for (Object requisitionLine : requisitionLineItems) {
      JSONObject requisitionLineAsJson = (JSONObject) requisitionLine;
      requisitionLineAsJson.put(keyToUpdate, newValue);
    }
  }

  private void updateFieldInRequisitionLineItem(JSONObject requisition,
      Object keyToUpdate, Object newValue, String productId) {
    JSONArray requisitionLineItems = (JSONArray) requisition.get(REQUISITION_LINE_ITEMS);

    for (Object requisitionLine : requisitionLineItems) {
      JSONObject requisitionLineAsJson = (JSONObject) requisitionLine;
      JSONObject orderable = (JSONObject) requisitionLineAsJson.get("orderable");
      String id = (String) orderable.get("id");

      if (productId.equals(id)) {
        requisitionLineAsJson.put(keyToUpdate, newValue);
      }
    }
  }

  private JSONArray createBodyForConvertToOrder(DataTable table) {
    List<Map<String, String>> data = table.asMaps(String.class, String.class);
    JSONArray array = new JSONArray();

    for (Map map : data) {
      JSONObject json = new JSONObject();
      if (map.get("requisitionId") != null) {
        requisitionId = map.get("requisitionId").toString();
      }
      json.put("requisitionId", requisitionId);
      json.put("supplyingDepotId", map.get("supplyingDepotId"));

      array.add(json);
    }
    return array;
  }

  private void getOrCreatePeriod(int monthsToAdd, String scheduleId) throws ParseException {
    LocalDate periodDate = LocalDate.now().plusMonths(monthsToAdd);
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

    periodResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .when()
        .get(BASE_URL_OF_REFERENCEDATA_SERVICE + "processingPeriods/" + periodId);
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
        .statusCode(SC_OK);

    JSONParser parser = new JSONParser();
    period = (JSONObject) parser.parse(periodResponse.asString());

    return (String) period.get("id");
  }

  private void updateRequisition() throws ParseException {
    if (requisition == null || !requisition.get("id").equals(requisitionId)) {
      JSONParser parser = new JSONParser();
      requisition = (JSONObject) parser.parse(requisitionResponse.asString());
    }
    if (supervisoryNodeId != null) {
      requisition.replace("supervisoryNode", null, supervisoryNodeId);
    }
  }

  private static class StreamGobbler implements Runnable {
    private Process process;
    private Consumer<String> consumer;

    StreamGobbler(Process process, Consumer<String> consumer) {
      this.process = process;
      this.consumer = consumer;
    }

    @Override
    public void run() {
      new BufferedReader(new InputStreamReader(process.getInputStream())).lines().forEach(consumer);
      new BufferedReader(new InputStreamReader(process.getErrorStream())).lines().forEach(consumer);
    }
  }
}
