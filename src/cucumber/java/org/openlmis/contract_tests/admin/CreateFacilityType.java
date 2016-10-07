package org.openlmis.contract_tests.admin;


import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.notNullValue;
import static org.jglue.fluentjson.JsonBuilderFactory.buildObject;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CreateFacilityType {

  private Response createFacilityType;
  private String createdFacilityTypeId;

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Given("^I try to create a facilityType with code: (.*)$")
  public void ITryToCreateAFacilityTypeWith(String code) throws Throwable {
    createFacilityType = given()
        .contentType(ContentType.JSON)
        .queryParam("access_token", ACCESS_TOKEN)
        .body(buildObject()
            .add("code", code)
            .getJson().toString())
        .when()
        .post(baseUrlOfService("referencedata") + "facilityTypes");
  }

  @Given("^I should get response with the created facilityType's id$")
  public void IShouldGetResponseWithTheCreatedfacilityTypeId() throws Throwable {
    createFacilityType
        .then()
        .body("id", notNullValue());
    createdFacilityTypeId = from(createFacilityType.asString()).get("id");
  }
}
