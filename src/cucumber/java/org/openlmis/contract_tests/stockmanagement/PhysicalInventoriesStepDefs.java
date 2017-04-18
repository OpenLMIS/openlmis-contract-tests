package org.openlmis.contract_tests.stockmanagement;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;
import static org.openlmis.contract_tests.requisition.RequisitionStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.stockmanagement.StockCardsStepDefs.URL_OF_STOCK_CARD_SUMMARIES;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class PhysicalInventoriesStepDefs {
  private static final String URL_PHYSICAL_INVENTORIES =
      baseUrlOfService("stockmanagement") + "stockEvents/";

  private static final String URL_PHYSICAL_INVENTORIES_DRAFT =
      baseUrlOfService("stockmanagement") + "physicalInventories/draft";

  private static final String FACILITY_PARAM_NAME = "facility";
  private static final String PROGRAM_PARAM_NAME = "program";

  private Response response;
  private int quantity;

  @When("^I try to get a draft with facilityId: (.*), programId: (.*)$")
  public void iTryToGetADraftWithFacilityIdAndProgramId(String facilityId, String programId)
      throws Throwable {
    response = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam(FACILITY_PARAM_NAME, facilityId)
        .queryParam(PROGRAM_PARAM_NAME, programId)
        .when()
        .get(URL_PHYSICAL_INVENTORIES_DRAFT);
  }

  @Then("^I should get the empty draft of physical inventory$")
  public void iShouldGetTheEmptyDraftOfPhysicalInventory() throws Throwable {
    response.then()
        .statusCode(SC_OK)
        .body("occurredDate", nullValue());
  }

  @When("^I try to save the draft physical inventory$")
  public void iTryToSaveTheDraftPhysicalInventory(String bodyString) throws Throwable {
    response = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyString)
        .when()
        .post(URL_PHYSICAL_INVENTORIES_DRAFT);
    quantity = JsonPath.from(bodyString).get("lineItems[0].quantity");
  }

  @Then("^I should get response of draft physical inventory saved$")
  public void iShouldGetResponseOfDraftPhysicalInventorySaved() throws Throwable {
    response.then()
        .statusCode(SC_CREATED);
  }

  @When("^I try to submit a physical inventory$")
  public void iTryToSubmitAPhysicalInventory(String bodyString) throws Throwable {
    response = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyString)
        .when()
        .post(URL_PHYSICAL_INVENTORIES);
  }

  @Then("^I should get response of physical inventory submitted$")
  public void iShouldGetResponseOfPhysicalInventorySubmitted() throws Throwable {
    response.then().statusCode(SC_CREATED);
  }

  @When("^I try to get stock cards summaries by facilityId: (.*), programId: (.*)$")
  public void iTryToGetStockCardsSummariesByFacilityIdProgramId(String facilityId, String programId)
      throws Throwable {
    response = given().contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam(FACILITY_PARAM_NAME, facilityId)
        .queryParam(PROGRAM_PARAM_NAME, programId)
        .when()
        .get(URL_OF_STOCK_CARD_SUMMARIES);
  }

  @Then("^I should get response of all stock cards summaries that include SOH: (\\d+)$")
  public void iShouldGetResponseOfAllStockCardsSummariesThatIncludeSOH(int stockOnHand)
      throws Throwable {
    response.then()
        .statusCode(SC_OK)
        .body("stockOnHand", hasItem(stockOnHand));
  }

}
