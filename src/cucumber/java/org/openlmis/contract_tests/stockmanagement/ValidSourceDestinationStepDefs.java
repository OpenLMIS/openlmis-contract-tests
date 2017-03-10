package org.openlmis.contract_tests.stockmanagement;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.CoreMatchers.hasItem;
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

public class ValidSourceDestinationStepDefs {
  private static final String URL_OF_ORGANIZATIONS =
      baseUrlOfService("stockmanagement") + "organizations/";

  private static final String URL_OF_VALID_SOURCES =
      baseUrlOfService("stockmanagement") + "validSources/";

  private static final String URL_OF_VALID_DESTINATIONS =
      baseUrlOfService("stockmanagement") + "validDestinations/";
  private static final String PROGRAM_PARAM_NAME = "program";
  private static final String FACILITY_TYPE_PARAM_NAME = "facilityType";

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";
  private static final String PROGRAM = "dce17f2e-af3e-40ad-8e00-3496adef44c3";
  private static final String FACILITY_TYPE = "663b1d34-cc17-4d60-9619-e553e45aa441";
  private Response response;
  private String organizationId;
  private String validSourceAssignmentId;

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
    organizationId = from(response.asString()).get("id");
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

  @When("^I try to assign created organization to combination of program and facility type$")
  public void iTryToAssignCreatedOrganizationToCombinationOfProgramAndFacilityType() throws Throwable {
    response = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .body(createValidSourceDestination())
        .when()
        .post(URL_OF_VALID_SOURCES);
    response.then().statusCode(HttpStatus.SC_CREATED);
  }

  @And("^I try to get all valid source assignments$")
  public void iTryToGetAllValidSourceAssignments() throws Throwable {
    response = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .queryParam(PROGRAM_PARAM_NAME, PROGRAM)
        .queryParam(FACILITY_TYPE_PARAM_NAME, FACILITY_TYPE)
        .when()
        .get(URL_OF_VALID_SOURCES);
  }

  @Then("^I should get response of all valid source assignment that contains newly assignment$")
  public void iShouldGetResponseOfAllValidSourceAssignmentThatContainsNewlyAssignment() throws Throwable {
    ArrayList<String> assignmentIds = from(response.asString()).get("id");
    validSourceAssignmentId = assignmentIds.get(assignmentIds.size() - 1);

    response.then()
        .statusCode(HttpStatus.SC_OK)
        .body("node.referenceId", hasItem(organizationId));
  }

  @When("^I try to detach created organization to combination of program and facility type$")
  public void iTryToDetachCreatedOrganizationToCombinationOfProgramAndFacilityType() throws Throwable {
    response = given()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .delete(URL_OF_VALID_SOURCES + validSourceAssignmentId);
    response.then().statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @Then("^I should get response of all valid source assignments that not contains detached assignment$")
  public void iShouldGetResponseOfAllValidSourceAssignmentsThatNotContainsDetachedAssignment() throws Throwable {
    response.then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", not(hasItem(validSourceAssignmentId)));
  }

  private JSONObject createValidSourceDestination() {
    JSONObject json = new JSONObject();
    json.put("programId", PROGRAM);
    json.put("facilityTypeId", FACILITY_TYPE);
    JSONObject node = new JSONObject();
    node.put("referenceId", organizationId);
    json.put("node", node);
    return json;
  }
}
