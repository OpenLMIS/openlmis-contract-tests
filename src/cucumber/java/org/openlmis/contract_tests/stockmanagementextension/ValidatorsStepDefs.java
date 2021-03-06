package org.openlmis.contract_tests.stockmanagementextension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class ValidatorsStepDefs {

  private Response createEventResponse;

  private static final String URL_OF_STOCK_EVENT_MANAGEMENT =
      baseUrlOfService("stockmanagement") + "stockEvents";

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  private static final String ERROR_EVENT_REASON_FREE_TEXT_NOT_ALLOWED =
      "stockmanagement.error.event.reasonFreeText.notAllowed";

  private static final String ERROR_EVENT_CANNOT_UNPACK_REGULAR_ORDERABLE =
      "stockmanagement.error.event.cannot.unpack.orderable.not.kit";

  @When("^I try to create a stock event for extensions")
  public void iTryToCreateAStockEvent(String bodyString) throws Throwable {
    createEventResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyString).when()
        .post(URL_OF_STOCK_EVENT_MANAGEMENT);
  }

  @Then("^I should get response that the event for extensions was created$")
  public void iShouldGetResponseOfTheEventCreated() throws Throwable {
    createEventResponse
        .then()
        .statusCode(HttpStatus.SC_CREATED);
  }

  @Then("^I should get response of reason free text not allowed")
  public void iShouldGetResponseOfFreeTextNotAllowed() throws Throwable {
    createEventResponse
        .then()
        .body("messageKey", is(ERROR_EVENT_REASON_FREE_TEXT_NOT_ALLOWED))
        .statusCode(HttpStatus.SC_BAD_REQUEST);
  }

  @Then("^I should get response that orderable cannot be unpacked")
  public void iShouldGetResponseThatOrderableCannotBeUnpacked() throws Throwable {
    createEventResponse
        .then()
        .body("messageKey", is(ERROR_EVENT_CANNOT_UNPACK_REGULAR_ORDERABLE))
        .statusCode(HttpStatus.SC_BAD_REQUEST);
  }
}
