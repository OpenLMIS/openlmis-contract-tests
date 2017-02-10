package org.openlmis.contract_tests.stockmanagement;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

public class StockEventsStepDefs {

  private Response createEventResponse;

  private static final String URL_OF_STOCK_EVENT_MANAGEMENT =
      baseUrlOfService("stockmanagement") + "stockEvents";

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  private static final String STOCKMANAGEMENT_ERROR_ORDERABLE_NOT_FOUND =
      "stockmanagement.error.orderable.notFound";

  @When("^I try to create a stock event$")
  public void ITryToCreateAStockEvent(String bodyString) throws Throwable {
    createEventResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyString).when()
        .post(URL_OF_STOCK_EVENT_MANAGEMENT);
  }

  @Then("^I should get response of the event created$")
  public void IShouldGetResponseOfTheEventCreated() throws Throwable {
    createEventResponse
        .then()
        .statusCode(HttpStatus.SC_CREATED);
  }

  @Then("^I should get response of incorrect body with orderable$")
  public void IShouldGetResponseOfIncorrectBodyWithOrderable() throws Throwable {
    createEventResponse
        .then()
        .body("messageKey", is(STOCKMANAGEMENT_ERROR_ORDERABLE_NOT_FOUND))
        .statusCode(HttpStatus.SC_BAD_REQUEST);
  }

  @Then("^I should get response of incorrect user permission$")
  public void IShouldGetResponseOfIncorrectUserPermission() throws Throwable {
    createEventResponse
        .then()
        .statusCode(HttpStatus.SC_FORBIDDEN);
  }
}
