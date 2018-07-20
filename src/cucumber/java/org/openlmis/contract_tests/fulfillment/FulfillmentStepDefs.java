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

package org.openlmis.contract_tests.fulfillment;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import io.cucumber.datatable.DataTable;
import java.util.function.BiConsumer;
import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class FulfillmentStepDefs {

  private static final Integer REMAINDER = 826;
  private Response shipmentResponse;
  private Response orderResponse;
  private Response requisitionResponse;
  private Response stockCardResponse;
  private Response proofOfDeliveryResponse;
  private Response printProofOfDeliveryResponse;

  private String orderId;
  private String shipmentId;
  private String stockCardId;
  private String proofOfDeliveryId;

  private int stockOnHand;

  private static final String FULFILLMENT = "fulfillment";
  private static final String URL_OF_SHIPMENTS =
      baseUrlOfService(FULFILLMENT) + "shipments";
  private static final String URL_OF_ORDERS =
      baseUrlOfService(FULFILLMENT) + "orders";
  private static final String URL_OF_PODS =
      baseUrlOfService(FULFILLMENT) + "proofsOfDelivery/";
  private static final String URL_OF_REQUISITION_CONVERT =
      baseUrlOfService("requisition") + "requisitions/convertToOrder";
  private static final String URL_OF_STOCK_CARD_SUMMARIES =
      baseUrlOfService("stockmanagement") + "stockCardSummaries";
  private static final String URL_OF_STOCK_CARD_MANAGEMENT =
      baseUrlOfService("stockmanagement") + "stockCards/";

  private static final String CONTENT_ID = "content[0].id";
  private static final String STOCK_ON_HAND = "content[0].stockOnHand";
  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";
  private static final String LOT_ID = "lotId";

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Then("^I have got stock card id for$")
  public void gotStockCardId(DataTable table) {
    Map<Object, Object> data = table.asMaps(String.class, String.class).get(0);

    RequestSpecification request = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN);

    data.forEach((key, value) -> request.queryParam(key.toString(), value));

    stockCardResponse = request
        .when()
        .get(URL_OF_STOCK_CARD_SUMMARIES).andReturn();

    stockCardId = stockCardResponse.jsonPath().getString(CONTENT_ID);
    stockOnHand = stockCardResponse.jsonPath().getInt(STOCK_ON_HAND);
  }

  @When("^I try to get stock card with id$")
  public void tryToGetStockCardWithId() {
    stockCardResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_STOCK_CARD_MANAGEMENT + stockCardId);
  }

  @When("^I try to find any proof of delivery$")
  public void tryToGetAnyProofOfDelivery() {
    proofOfDeliveryResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_PODS);
  }

  @When("^I try to print proof of delivery with id$")
  public void tryToPrintProofOfDeliveryWithId() {
    printProofOfDeliveryResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_PODS + proofOfDeliveryId + "/print");
  }

  @Then("^I should get correct pdf response$")
  public void shouldGetCorrectPdfResponse() {
    printProofOfDeliveryResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .header("Content-Type", "application/pdf;charset=UTF-8");
  }

  @Then("^I should get a stock card$")
  public void shouldGetAStockCardWithStockOnHand() {
    stockCardResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(STOCK_ON_HAND, is(stockOnHand));
  }

  @Then("^I should get a stock card with proper stock on hand$")
  public void shouldGetStockCardWithStockOnHandSubstracted() {
    stockCardResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(STOCK_ON_HAND, is(REMAINDER));
  }

  @When("^I try to convert requisition with:$")
  public void tryToConvertRequisition(DataTable table) {
    requisitionResponse = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(createBodyForConvertToOrder(table))
        .when()
        .post(URL_OF_REQUISITION_CONVERT);
  }

  @Then("^I should get response of order created$")
  public void shouldGetResponseOfOrderCreated() {
    requisitionResponse.then()
        .statusCode(SC_CREATED);
  }

  @When("^I try to get order by:$")
  public void tryToGetOrderBy(DataTable table) {
    Map<Object, Object> data = table.asMaps(String.class, String.class).get(0);

    RequestSpecification request = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN);

    data.forEach((key, value) -> request.queryParam(key.toString(), value));

    orderResponse = request
        .when()
        .get(URL_OF_ORDERS)
        .andReturn();
  }

  @Then("^I should get response of order found")
  public void shouldGetResponseOfOrderFound() {
    orderResponse.then()
        .body(CONTENT_ID, notNullValue())
        .statusCode(SC_OK);

    orderId = orderResponse.jsonPath().getString(CONTENT_ID);
  }

  @When("^I try to get shipment by order id$")
  public void tryToGetShipmentByOrderId() {
    shipmentResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("orderId", orderId)
        .when()
        .get(URL_OF_SHIPMENTS)
        .andReturn();
  }

  @Then("^I should get response of shipment found$")
  public void shouldGetResponseOfShipmentFound() {
    shipmentResponse
        .then()
        .body(CONTENT_ID, notNullValue())
        .statusCode(SC_OK);

    shipmentId = shipmentResponse.jsonPath().getString(CONTENT_ID);
  }

  @Then("^I should get response of shipment not found$")
  public void shouldGetResponseOfShipmentNotFound() {
    shipmentResponse
        .then()
        .body("content.isEmpty()", is(true))
        .statusCode(SC_OK);
  }

  @When("^I try to get proof of delivery by shipment id$")
  public void tryToGetProofOfDeliveryByShipmentId() {
    proofOfDeliveryResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("shipmentId", shipmentId)
        .when()
        .get(URL_OF_PODS)
        .andReturn();
  }

  @Then("^I should get response of proof of delivery found$")
  public void shouldGetResponseOfProofOfDeliveryFound() {
    proofOfDeliveryResponse.then()
        .body(CONTENT_ID, notNullValue())
        .statusCode(SC_OK);

    proofOfDeliveryId = proofOfDeliveryResponse.jsonPath().getString(CONTENT_ID);
  }

  @When("^I try to confirm the proof of delivery$")
  public void tryToConfirmProofOfDelivery(DataTable table) throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject page = (JSONObject) parser.parse(proofOfDeliveryResponse.asString());
    JSONArray content = (JSONArray) page.get("content");

    JSONObject body = (JSONObject) content.get(0);
    body.put("status", "CONFIRMED");
    body.put("receivedBy", "Someone");
    body.put("deliveredBy", "Someone");
    body.put("receivedDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    List<Map<String, String>> data = table.asMaps(String.class, String.class);
    JSONArray lineItems = (JSONArray) body.get("lineItems");

    for (Map<String, String> map : data) {
      for (Object line : lineItems) {
        JSONObject obj = (JSONObject) line;
        JSONObject orderable = (JSONObject) obj.get("orderable");
        JSONObject lot = (JSONObject) obj.get("lot");

        boolean orderableMatch = map.get("orderableId").equals(orderable.get("id"));
        boolean lotMatch = null == lot || map.get(LOT_ID).equals(lot.get("id"));

        if (orderableMatch && lotMatch) {
          obj.put("quantityAccepted", map.get("quantityAccepted"));
          obj.put("quantityRejected", 0);

          if ("true".equals(obj.get("useVvm").toString())) {
            obj.put("vvmStatus", "STAGE_2");
          }
        }
      }
    }

    proofOfDeliveryResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(body)
        .when()
        .put(URL_OF_PODS + proofOfDeliveryId)
        .andReturn();
  }

  @Then("^I should get response of proof of delivery confirmation success$")
  public void shouldGetResponseOfProofOfDeliveryConfirmationSuccess() throws ParseException {
    proofOfDeliveryResponse
        .then()
        .statusCode(SC_OK);

    JSONParser parser = new JSONParser();
    JSONObject body = (JSONObject) parser.parse(proofOfDeliveryResponse.asString());
    assertThat(body.get("id").toString(), is(proofOfDeliveryId));
    assertThat(body.get("status").toString(), is("CONFIRMED"));
  }

  @When("^I try to finalize shipment$")
  public void tryToFinalizeShipment(DataTable table) {
    List<Map<String, String>> data = table.asMaps(String.class, String.class);
    JSONArray lineItems = new JSONArray();

    for (Map map : data) {
      JSONObject json = new JSONObject();
      json.put("orderable", createObjectReference(map.get("orderableId")));

      if (map.containsKey(LOT_ID)) {
        json.put("lot", createObjectReference(map.get(LOT_ID)));
      }

      json.put("quantityShipped", map.get("quantityShipped"));

      lineItems.add(json);
    }

    JSONObject body = new JSONObject();
    body.put("order", createObjectReference(orderId));
    body.put("notes", "some notes");
    body.put("lineItems", lineItems);

    shipmentResponse = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(body)
        .when()
        .post(URL_OF_SHIPMENTS);
  }

  @Then("^I should get response of shipment created$")
  public void shouldGetResponseOfShipmentCreated() {
    shipmentResponse.then()
        .statusCode(SC_CREATED);
  }

  private JSONObject createObjectReference(Object id) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("id", id);
    return jsonObject;
  }

  private JSONArray createBodyForConvertToOrder(DataTable table) {
    List<Map<String, String>> data = table.asMaps(String.class, String.class);
    JSONArray array = new JSONArray();

    for (Map map : data) {
      JSONObject json = new JSONObject();
      json.put("requisitionId", map.get("requisitionId"));
      json.put("supplyingDepotId", map.get("supplyingDepotId"));

      array.add(json);
    }

    return array;
  }
}
