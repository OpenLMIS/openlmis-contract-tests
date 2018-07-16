package org.openlmis.contract_tests.referencedata;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.openlmis.contract_tests.common.JsonFieldSetter;

public class FacilityTypeApprovedProductStepDefs {
  private static final String FTAP_URL = baseUrlOfService("referencedata")
      + "facilityTypeApprovedProducts/";

  private Response createApprovedProductResponse;
  private Response deleteApprovedProductResponse;

  private String createdApprovedProductId;

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @When("^I try to create a FTAP:$")
  public void tryToCreateApprovedProduct(DataTable table) {
    JSONObject json = new JSONObject();
    table
        .asMaps()
        .forEach(map -> map.forEach((key, value) -> JsonFieldSetter.setField(json, key, value)));

    createApprovedProductResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .when()
        .body(json.toJSONString())
        .post(FTAP_URL);
  }

  @Then("^I should get response with created FTAP's id$")
  public void shouldGetResponseWithCreatedApprovedProductId() {
    createApprovedProductResponse
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body("id", is(notNullValue()));

    createdApprovedProductId = from(createApprovedProductResponse.asString()).getString("id");
  }

  @When("^I try to delete created FTAP$")
  public void tryToDeleteCreatedApprovedProduct() {
    deleteApprovedProductResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .delete(FTAP_URL + createdApprovedProductId);
  }

  @Then("^I should get response of deleted FTAP$")
  public void shouldGetResponseOfDeletedApprovedProduct() {
    deleteApprovedProductResponse
        .then()
        .statusCode(HttpStatus.SC_NO_CONTENT);
  }

}
