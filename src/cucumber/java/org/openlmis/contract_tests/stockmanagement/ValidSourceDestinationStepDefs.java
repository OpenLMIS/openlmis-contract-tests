package org.openlmis.contract_tests.stockmanagement;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import org.apache.http.HttpStatus;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ValidSourceDestinationStepDefs {
  private static final String URL_OF_ORGANIZATIONS =
      baseUrlOfService("stockmanagement") + "organizations/";

  private static final String URL_OF_VALID_SOURCES =
      baseUrlOfService("stockmanagement") + "validSources/";

  private static final String URL_OF_VALID_DESTINATIONS =
      baseUrlOfService("stockmanagement") + "validDestinations/";

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";
  private Response response;
  private String organizationId;

  @When("^I try to create a new organization$")
  public void iTryToCreateANewOrganization(String bodyString) throws Throwable {
    response = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyString)
        .when()
        .post(URL_OF_ORGANIZATIONS);
  }

  @Then("^I should get response of organization created$")
  public void iShouldGetResponseOfOrganizationCreated() throws Throwable {
    response.then().statusCode(HttpStatus.SC_CREATED);
    organizationId = JsonPath.from(response.asString()).get("id");
  }

  @When("^I try to get all organizations$")
  public void iTryToGetAllOrganizations() throws Throwable {
    response = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(URL_OF_ORGANIZATIONS);
  }

  @Then("^I should get response of all organizations that contains created organization$")
  public void iShouldGetResponseOfAllOrganizationsThatContainsCreatedOrganization()
      throws Throwable {
    response.then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", hasItem(organizationId));
  }

  @When("^I try to update an organization$")
  public void iTryToUpdateAnOrganization(String bodyString) throws Throwable {
    response = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(bodyString)
        .when()
        .put(URL_OF_ORGANIZATIONS + organizationId);
  }

  @Then("^I should get response of all organizations that contains updated organization name (.*)$")
  public void iShouldGetResponseOfAllOrganizationsThatContainsUpdatedOrganizationNameUpdateOrg(
      String organizationName) throws Throwable {
    response.then()
        .statusCode(HttpStatus.SC_OK)
        .body("name", hasItem(organizationName));
  }
}
