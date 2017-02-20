/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.contract_tests.admin;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jglue.fluentjson.JsonBuilderFactory.buildObject;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AdminStepDefs {

    private Response createProgramResponse;
    private String createdProgramId;
    private Response getProgramResponse;

    static {
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @When("^I try to create a program with name: (.*), code: (.*)$")
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
                .post(baseUrlOfService("referencedata") + "programs");
    }

    @Then("^I should get response with the created program's id$")
    public void IShouldGetResponseWithTheCreatedProgramId() throws Throwable {
        createProgramResponse
                .then()
                .body("id", notNullValue());
        createdProgramId = from(createProgramResponse.asString()).get("id");
    }

    @When("^I try to get program with id$")
    public void ITryToGetProgramWithId() throws Throwable {
        getProgramResponse = given()
                .param("access_token", ACCESS_TOKEN)
                .when()
                .get(baseUrlOfService("referencedata") + "programs/" + createdProgramId);
    }

    @Then("^I should get program with name: (.*), code: (.*)$")
    public void IShouldGetProgramWith(String name, String code) throws Throwable {
        getProgramResponse
                .then()
                .body("code", is(code))
                .body("name", is(name));
    }
}
