package org.openlmis.contract_tests.stockmanagement;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class StockCardsStepDefs {

  private Response idsResponse;
  private Response getCardResponse;

  private static final String URL_OF_STOCK_CARD_MANAGEMENT =
      baseUrlOfService("stockmanagement") + "stockCards/";

  private static final String URL_OF_STOCK_CARD_IDS =
      baseUrlOfService("stockmanagement") + "stockCardIds";

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  private String stockCardId;

  @Given("^I have got stock card id$")
  public void iHaveGotStockCardId() {
    idsResponse = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when().get(URL_OF_STOCK_CARD_IDS).andReturn();

    stockCardId = idsResponse.asString().substring(idsResponse.asString().lastIndexOf('.') + 1);
    if (stockCardId.contains(",")) {
      stockCardId = stockCardId.substring(stockCardId.lastIndexOf(',') + 1);
    }
  }

  @When("^I try to get stock card with card id$")
  public void iTryToGetStockCardWithCardId() {
    getCardResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_STOCK_CARD_MANAGEMENT + stockCardId);
  }

  @Then("^I should get a stock card with (\\d+) stockOnHand$")
  public void iShouldGetAStockCardWithSOH(int value) {
    getCardResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("stockOnHand", is(value));
  }

  @Then("^I should get response of incorrect user permission of view cards$")
  public void iShouldGetResponseOfIncorrectUserPermissionOfViewEvents() {
    getCardResponse
        .then()
        .statusCode(HttpStatus.SC_FORBIDDEN);
  }
}
