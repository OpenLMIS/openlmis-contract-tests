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

package org.openlmis.contract_tests.stockmanagement;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class StockCardTemplatesStepDefs {

  private Response getTemplateResponse;
  private Response createTemplateResponse;

  private static final String URL_OF_STOCK_CARD_TEMPLATES_MANAGE =
      baseUrlOfService("stockmanagement") + "stockCardTemplates";

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  private static final String STOCKMANAGEMENT_ERROR_PROGRAM_NOT_FOUND = "stockmanagement.error.program.notFound";

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @When("^I try to get the default stock card template")
  public void iTryToGetTheDefaultStockCardTemplate() {
    getTemplateResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_STOCK_CARD_TEMPLATES_MANAGE);
  }

  @Then("^I should get the default stock card template")
  public void iShouldGetTheDefaultStockCardTemplate() {
    getTemplateResponse
        .then()
        .statusCode(HttpStatus.SC_OK);
  }

  @When("^I try to create a stock card template$")
  public void iTryToCreateAStockCardTemplate(String bodyString) throws Throwable {
    createTemplateResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyString).when()
        .post(URL_OF_STOCK_CARD_TEMPLATES_MANAGE);
  }

  @Then("^I should get response of created$")
  public void iShouldGetResponseOfCreated() throws Throwable {
    createTemplateResponse
        .then()
        .body("stockCardFields[0].name", is("packSize"))
        .body("stockCardFields[0].displayed", is(true))
        .body("stockCardLineItemFields[3].name", is("adjustmentReason"))
        .body("stockCardLineItemFields[3].displayOrder", is(1))
        .statusCode(HttpStatus.SC_CREATED);
  }

  @Then("^I should get response of incorrect body format with program$")
  public void iShouldGetResponseOfIncorrectBodyFormat() throws Throwable {
    createTemplateResponse
        .then()
        .body("messageKey", is(STOCKMANAGEMENT_ERROR_PROGRAM_NOT_FOUND))
        .statusCode(HttpStatus.SC_BAD_REQUEST);
  }

  @When("^I try to get a stock card template with programId: (.*), facilityTypeId: (.*)$")
  public void iTryToGetAStockCardTemplate(String programId, String facilityTypeId)
      throws Throwable {
    getTemplateResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("program", programId)
        .queryParam("facilityType", facilityTypeId)
        .get(URL_OF_STOCK_CARD_TEMPLATES_MANAGE);

  }


  @Then("^I should get a stock card template with programId: (.*), facilityTypeId: (.*)$")
  public void iShouldGetAStockCardTemplate(String programId, String facilityTypeId)
      throws Throwable {
    getTemplateResponse
        .then()
        .body("programId", is(programId))
        .body("facilityTypeId", is(facilityTypeId))
        .statusCode(HttpStatus.SC_OK);
  }

  @Then("^I should get response of stock card template not found$")
  public void iShouldGetResponseOfStockCardTemplateNotFound() throws Throwable {
    getTemplateResponse
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }
}
