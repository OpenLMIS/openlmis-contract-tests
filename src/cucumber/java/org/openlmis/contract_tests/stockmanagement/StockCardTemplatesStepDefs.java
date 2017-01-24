package org.openlmis.contract_tests.stockmanagement;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

public class StockCardTemplatesStepDefs {

  private Response getTemplateResponse;
  private Response createTemplateResponse;

  private static final String URL_OF_STOCK_CARD_TEMPLATES_MANAGE =
      baseUrlOfService("stockmanagement") + "stockCardTemplates";

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @When("^I try to get the default stock card template")
  public void ITryToGetTheDefaultStockCardTemplate() {
    getTemplateResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_STOCK_CARD_TEMPLATES_MANAGE);
  }

  @Then("^I should get the default stock card template")
  public void IShouldGetTheDefaultStockCardTemplate() {
    getTemplateResponse
        .then()
        .statusCode(HttpStatus.SC_OK);
  }

  @When("^I try to create a stock card template$")
  public void ITryToCreateAStockCardTemplate(String bodyString) throws Throwable {
    createTemplateResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyString).when()
        .post(URL_OF_STOCK_CARD_TEMPLATES_MANAGE);
  }

  @Then("^I should get response of created$")
  public void IShouldGetResponseOfCreated() throws Throwable {
    createTemplateResponse
        .then()
        .body("stockCardFields[0].name", is("packSize"))
        .body("stockCardFields[0].displayed", is(true))
        .body("stockCardLineItemFields[3].name", is("adjustmentReason"))
        .body("stockCardLineItemFields[3].displayOrder", is(1))
        .statusCode(HttpStatus.SC_CREATED);
  }

  @When("^I try to get a stock card template with programId: (.*), facilityTypeId: (.*)$")
  public void ITryToGetAStockCardTemplate(String programId, String facilityTypeId) throws Throwable {
    getTemplateResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam("program", programId)
        .queryParam("facilityType", facilityTypeId)
        .get(URL_OF_STOCK_CARD_TEMPLATES_MANAGE);

  }


  @Then("^I should get a stock card template with programId: (.*), facilityTypeId: (.*)$")
  public void IShouldGetAStockCardTemplate(String programId, String facilityTypeId) throws Throwable {
    getTemplateResponse
        .then()
        .body("programId", is(programId))
        .body("facilityTypeId", is(facilityTypeId))
        .statusCode(HttpStatus.SC_OK);
  }
}
