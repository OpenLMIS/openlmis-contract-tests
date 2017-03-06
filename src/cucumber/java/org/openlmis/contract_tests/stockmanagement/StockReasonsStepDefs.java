package org.openlmis.contract_tests.stockmanagement;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import org.apache.http.HttpStatus;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StockReasonsStepDefs {
  private static final String URL_OF_STOCK_REASON_CATEGORIES =
      baseUrlOfService("stockmanagement") + "reasonCategories";
  private static final String URL_OF_STOCK_REASON_TYPES =
      baseUrlOfService("stockmanagement") + "reasonTypes";

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  private Response reasonResponse;

  @When("^I try to get stock card line item reason types$")
  public void iTryToGetStockCardLineItemReasonTypes() throws Throwable {
    reasonResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_STOCK_REASON_TYPES);

  }

  @Then("^I should get response of stock card line item reason types$")
  public void iShouldGetResponseOfStockCardLineItemReasonTypes() throws Throwable {
    reasonResponse.then()
        .statusCode(HttpStatus.SC_OK)
        .body("$", hasItems("CREDIT", "DEBIT"));
  }

  @When("^I try to get stock card line item reason categories$")
  public void iTryToGetStockCardLineItemReasonCategories() throws Throwable {
    reasonResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_STOCK_REASON_CATEGORIES);

  }

  @Then("^I should get response of stock card line item reason categories$")
  public void iShouldGetResponseOfStockCardLineItemReasonCategories() throws Throwable {
    reasonResponse.then()
        .statusCode(HttpStatus.SC_OK)
        .body("$", hasItems("AD_HOC", "ADJUSTMENT"));
  }
}
