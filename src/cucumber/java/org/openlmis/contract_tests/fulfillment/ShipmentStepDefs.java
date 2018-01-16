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

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

public class ShipmentStepDefs {

  private Response shipmentResponse;
  private Response orderResponse;
  private Response requisitionResponse;
  private Response stockCardResponse;
  private String orderId;
  private String stockCardId;
  private int stockOnHand;

  private static final String URL_OF_SHIPMENTS =
      baseUrlOfService("fulfillment") + "shipments";
  private static final String URL_OF_ORDERS_SEARCH =
      baseUrlOfService("fulfillment") + "orders/search";
  private static final String URL_OF_REQUISITION_CONVERT =
      baseUrlOfService("requisition") + "requisitions/convertToOrder";
  private static final String URL_OF_STOCK_CARD_SUMMARIES =
      baseUrlOfService("stockmanagement") + "stockCardSummaries";
  private static final String URL_OF_STOCK_CARD_MANAGEMENT =
      baseUrlOfService("stockmanagement") + "stockCards/";

  private static final String CONTENT_ID = "content[0].id";
  private static final String STOCK_ON_HAND = "content[0].stockOnHand";
  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Then("^I have got stock card id for programId: (.*) and facilityId: (.*)$")
  public void iHaveGotStockCardId(String programId, String facilityId) throws Throwable {
    stockCardResponse = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("program", programId)
        .queryParam("facility", facilityId)
        .when()
        .get(URL_OF_STOCK_CARD_SUMMARIES).andReturn();

    stockCardId = stockCardResponse.jsonPath().getString(CONTENT_ID);
    stockOnHand = stockCardResponse.jsonPath().getInt(STOCK_ON_HAND);
  }

  @When("^I try to get stock card with id$")
  public void iTryToGetStockCardWithId() throws Throwable {
    stockCardResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_STOCK_CARD_MANAGEMENT + stockCardId);
  }

  @Then("^I should get a stock card$")
  public void iShouldGetAStockCardWithStockOnHand() throws Throwable {
    stockCardResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(STOCK_ON_HAND, is(stockOnHand));
  }

  @Then("^I should get a stock card with zero stock on hand$")
  public void iShouldGetAStockCardWithStockOnHandSubstracted() throws Throwable {
    stockCardResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(STOCK_ON_HAND, is(0));
  }

  @When("^I try to convert requisition with requisitionId: (.*) and supplyingDepotId: (.*)$")
  public void iTryToConvertRequisition(String requisitionId, String suppylyingDepotId) throws Throwable {
    requisitionResponse = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(createBodyForConvertToOrder(requisitionId, suppylyingDepotId))
        .when()
        .post(URL_OF_REQUISITION_CONVERT);
  }

  @Then("^I should get response of order created$")
  public void iShouldGetResponseOfOrderCreated() throws Throwable {
    requisitionResponse.then()
        .statusCode(SC_CREATED);
  }

  @When("^I try to get order by supplying facility: (.*)$")
  public void iShouldGetOrderBySupplyingFacility(String supplyingFacilityId) throws Throwable {
    orderResponse = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("supplyingFacility", supplyingFacilityId)
        .when()
        .get(URL_OF_ORDERS_SEARCH).andReturn();

    orderId = orderResponse.jsonPath().getString(CONTENT_ID);
  }

  @Then("^I should get response of order found")
  public void iShouldGetResponseOfOrderFound() throws Throwable {
    orderResponse.then()
        .body(CONTENT_ID, notNullValue())
        .statusCode(SC_OK);
  }

  @When("^I try to finalize shipment$")
  public void iTryToFinalizeShipment(String bodyString) throws Throwable {
    org.json.JSONObject jsonObject = new org.json.JSONObject(bodyString);
    jsonObject.put("order", createObjectReference());
    shipmentResponse = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(jsonObject.toString())
        .when()
        .post(URL_OF_SHIPMENTS);
  }

  @Then("^I should get response of shipment created$")
  public void iShouldGetResponseOfShipmentCreated() throws Throwable {
    shipmentResponse.then()
        .statusCode(SC_CREATED);
  }

  private JSONObject createObjectReference() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("id", orderId);
    return jsonObject;
  }

  private JSONArray createBodyForConvertToOrder(String requisitionId, String supplyingDepotId) {
    JSONObject json = new JSONObject();
    json.put("requisitionId", requisitionId);
    json.put("supplyingDepotId", supplyingDepotId);
    JSONArray array = new JSONArray();
    array.add(json);
    return array;
  }
}
