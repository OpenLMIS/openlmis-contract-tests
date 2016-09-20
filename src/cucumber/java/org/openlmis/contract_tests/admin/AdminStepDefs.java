package org.openlmis.contract_tests.admin;

import cucumber.api.java.en.Given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jglue.fluentjson.JsonBuilderFactory.buildObject;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

public class AdminStepDefs {

    private Response createProgramResponse;
    private String createdProgramId;
    private Response getProgramResponse;

    static {
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Given("^I try to create a program with name: (.*), code: (.*)$")
    public void ITryToCreateAProgramWith(String name, String code) throws Throwable {
        createProgramResponse = given()
                .contentType(ContentType.JSON)
                .queryParam("access_token", ACCESS_TOKEN)
                .body(buildObject()
                        .add("code", code)
                        .add("name", name)
                        .add("showNonFullSupplyTab", false)
                        .getJson().toString())
                .when()
                .post(baseUrlOfService("requisition") + "programs");

    }

    @Given("^I should get response with the created program's id$")
    public void IShouldGetResponseWithTheCreatedProgramId() throws Throwable {
        createProgramResponse
                .then()
                .body("id", notNullValue());
        createdProgramId = from(createProgramResponse.asString()).get("id");
    }

    @Given("^I try to get program with id$")
    public void ITryToGetProgramWithId() throws Throwable {
        getProgramResponse = given()
                .param("access_token", ACCESS_TOKEN)
                .when()
                .get(baseUrlOfService("requisition") + "programs/" + createdProgramId);
    }

    @Given("^I should get program with name: (.*), code: (.*)$")
    public void IShouldGetProgramWith(String name, String code) throws Throwable {
        getProgramResponse
                .then()
                .body("code", is(code))
                .body("name", is(name));
    }
}
