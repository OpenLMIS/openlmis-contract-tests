package org.openlmis.contract_tests.stockmanagement;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.ArrayList;

public class StockReasonsStepDefs {
  private static final String URL_OF_STOCK_REASON_CATEGORIES =
      baseUrlOfService("stockmanagement") + "reasonCategories/";
  private static final String URL_OF_STOCK_REASON_TYPES =
      baseUrlOfService("stockmanagement") + "reasonTypes/";
  private static final String URL_OF_STOCK_REASONS =
      baseUrlOfService("stockmanagement") + "stockCardLineItemReasons/";
  private static final String URL_OF_VALID_REASON_ASSIGNMENTS =
      baseUrlOfService("stockmanagement") + "validReasons/";

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";
  private static final String PROGRAM_PARAM_NAME = "program";
  private static final String FACILITY_TYPE_PARAM_NAME = "facilityType";

  private static final String PROGRAM = "dce17f2e-af3e-40ad-8e00-3496adef44c3";
  private static final String FACILITY_TYPE = "663b1d34-cc17-4d60-9619-e553e45aa441";

  private Response reasonResponse;
  private String createdReasonId;
  private String validReasonAssignmentId;

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
        .body("$", hasItems("TRANSFER", "ADJUSTMENT"));
  }

  @When("^I try to create a new stock card line item reason$")
  public void iTryToCreateANewStockCardLineItemReason(String bodyString) throws Throwable {
    reasonResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyString)
        .when()
        .post(URL_OF_STOCK_REASONS);
  }

  @Then("^I should get response of reason created$")
  public void iShouldGetResponseOfReasonCreated() throws Throwable {
    reasonResponse.then().statusCode(HttpStatus.SC_CREATED);
    createdReasonId = from(reasonResponse.asString()).get("id");
  }

  @When("^I try to get all stock card line item reasons$")
  public void iTryToGetAllStockCardLineItemReasons() throws Throwable {
    reasonResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_STOCK_REASONS);
  }

  @Then("^I should get response of all reasons that contains created reason$")
  public void iShouldGetResponseOfAllReasonsThatContainsCreatedReason() throws Throwable {
    reasonResponse.then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", hasItem(createdReasonId));
  }

  @When("^I try to update a stock card line item reason$")
  public void iTryToUpdateAStockCardLineItemReason(String bodyString) throws Throwable {
    reasonResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyString)
        .when()
        .put(URL_OF_STOCK_REASONS + createdReasonId);
  }

  @Then("^I should get response of all reasons that contains updated reason name (.*)$")
  public void iShouldGetResponseOfAllReasonsThatContainsUpdatedReasonName(String reasonName)
      throws Throwable {
    reasonResponse.then()
        .statusCode(HttpStatus.SC_OK)
        .body("name", hasItem(reasonName));
  }

  @When("^I try to assign created reason to program and facility type combination$")
  public void iTryToAssignCreatedReasonToProgramAndFacilityTypeCombination() throws Throwable {
    reasonResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(createValidReasonJson())
        .when()
        .post(URL_OF_VALID_REASON_ASSIGNMENTS);
    reasonResponse.then().statusCode(HttpStatus.SC_CREATED);
  }

  @And("^I try to get all valid reason assignments$")
  public void iTryToGetAllValidReasonAssignments() throws Throwable {
    reasonResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam(PROGRAM_PARAM_NAME, PROGRAM)
        .queryParam(FACILITY_TYPE_PARAM_NAME, FACILITY_TYPE)
        .when()
        .get(URL_OF_VALID_REASON_ASSIGNMENTS);
  }

  @Then("^I should get response of all valid reason assignments that contains newly assignment$")
  public void iShouldGetResponseOfAllValidReasonAssignmentsThatContainsNewlyAssignment()
      throws Throwable {
    ArrayList<String> assignmentIds = from(reasonResponse.asString()).get("id");
    validReasonAssignmentId = assignmentIds.get(assignmentIds.size() - 1);
    reasonResponse.then()
        .statusCode(HttpStatus.SC_OK)
        .body("reason.id", hasItem(createdReasonId));
  }


  @When("^I try to detach created reason to program and facility type combination$")
  public void iTryToDetachCreatedReasonToProgramAndFacilityTypeCombination() throws Throwable {
    reasonResponse = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .delete(URL_OF_VALID_REASON_ASSIGNMENTS + validReasonAssignmentId);
    reasonResponse.then().statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @Then("^I should get response of all valid reason assignments that not contains detached assignment$")
  public void iShouldGetResponseOfAllValidReasonAssignmentsThatNotContainsDetachedAssignment() throws Throwable {
    reasonResponse.then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", not(hasItem(validReasonAssignmentId)));
  }

  private JSONObject createValidReasonJson() {
    JSONObject json = new JSONObject();

    JSONObject program = new JSONObject();
    program.put("id", PROGRAM);
    json.put(PROGRAM_PARAM_NAME, program);

    JSONObject facilityType = new JSONObject();
    facilityType.put("id", FACILITY_TYPE);
    json.put(FACILITY_TYPE_PARAM_NAME, facilityType);

    JSONObject reason = new JSONObject();
    reason.put("id", createdReasonId);
    json.put("reason", reason);
    return json;
  }
}
