package org.openlmis.contract_tests.requisition;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.openlmis.contract_tests.common.InitialDataException;
import org.openlmis.contract_tests.common.TestDatabaseConnection;

public class InitiateRequisitionStepDefs {

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  private Response initiateRequisitionResponse;
  private String initiatedRequisitionId;
  private Response getRequisitionResponse;

  private TestDatabaseConnection databaseConnection;

  @Before
  public void setUp() throws InitialDataException {
    databaseConnection = new TestDatabaseConnection();
    //Because we have some initial data. We must remove it before loader.
    databaseConnection.removeData();
    databaseConnection.loadData();
  }

  @When("^I try to initiate a requisition with \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", " +
      "(true|false)$")
  public void ITryToCreateARequisitionWith(String program, String facility,
                                           String period, Boolean emergency) throws Throwable {
    initiateRequisitionResponse = given()
        .queryParam("access_token", ACCESS_TOKEN)
        .queryParam("program", program)
        .queryParam("facility", facility)
        .queryParam("suggestedPeriod", period)
        .queryParam("emergency", emergency)
        .when()
        .post(baseUrlOfService("requisition") + "requisitions/initiate");
  }

  @Then("^I should get response with the initiated requisition's id$")
  public void IShouldGetResponseWithTheInitiatedRequisitionId() throws Throwable {
    initiateRequisitionResponse
        .then()
        .body("id", notNullValue());
    initiatedRequisitionId = from(initiateRequisitionResponse.asString()).get("id");
  }

  @When("^I try to get requisition with id$")
  public void ITryToGetRequisitionWithId() throws Throwable {
    getRequisitionResponse = given()
        .queryParam("access_token", ACCESS_TOKEN)
        .when()
        .get(baseUrlOfService("requisition") + "requisitions/" + initiatedRequisitionId);
  }

  @Then("^I should get a requisition with \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"," +
      " (true|false), \"([^\"]*)\"$")
  public void IShouldGetRequisitionWith(String programId, String facilityId, String periodId,
                                        Boolean emergency, String status) throws Throwable {
    getRequisitionResponse
        .then()
        .body("program.id", is(programId))
        .body("facility.id", is(facilityId))
        .body("processingPeriod.id", is(periodId))
        .body("emergency", is(emergency))
        .body("status", is(status));
  }

  @After
  public void cleanUp() throws InitialDataException {
    databaseConnection.removeData();
  }
}
