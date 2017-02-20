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
import static org.hamcrest.Matchers.notNullValue;
import static org.jglue.fluentjson.JsonBuilderFactory.buildObject;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CreateFacilityTypeStepDefs {

  private Response createFacilityType;
  private String createdFacilityTypeId;

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @When("^I try to create a facilityType with code: (.*)$")
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

  @Then("^I should get response with the created facilityType's id$")
  public void IShouldGetResponseWithTheCreatedfacilityTypeId() throws Throwable {
    createFacilityType
        .then()
        .body("id", notNullValue());
    createdFacilityTypeId = from(createFacilityType.asString()).get("id");
  }
}
