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

package org.openlmis.contract_tests.referencedata;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jglue.fluentjson.JsonBuilderFactory.buildObject;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.jglue.fluentjson.JsonObjectBuilder;

public class SupervisoryNodeStepDefs {

  private Response createSupervisoryNodeResponse;
  private Response getSupervisoryNodeResponse;
  private Response deleteSupervisoryNodeResponse;
  private String id;

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @When("^I try to create a supervisoryNode with:$")
  public void tryToCreateSupervisoryNodeWith(DataTable argsList) {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    for (Map<String, String> map : data) {
      createSupervisoryNodeResponse = given()
          .contentType(ContentType.JSON)
          .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
          .body(buildSupervisoryNode(map)
              .getJson().toString())
          .when()
          .post(baseUrlOfService("referencedata") + "supervisoryNodes");
    }
  }

  @Then("^I should get response with the created supervisoryNode's id$")
  public void shouldGetResponseWithTheCreatedSupervisoryNodeId() throws Throwable {
    createSupervisoryNodeResponse
        .then()
        .body("id", notNullValue());
    id = from(createSupervisoryNodeResponse.asString()).get("id");
  }

  @When("^I try to get supervisoryNode with id$")
  public void tryToGetSupervisoryNodeWithId() throws Throwable {
    getSupervisoryNodeResponse = given()
        .param("access_token", ACCESS_TOKEN)
        .when()
        .get(baseUrlOfService("referencedata") + "supervisoryNodes/" + id);
  }

  @Then("^I should get supervisoryNode with:$")
  public void shouldGetProgramWith(DataTable argsList) throws Throwable {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    for (Map<String, String> map : data) {
      getSupervisoryNodeResponse
          .then()
          .body("code", is(map.get("code")))
          .body("name", is(map.get("name")))
          .body("facility.id", is(map.get("facilityId")));
    }
  }

  @When("^I try to delete a supervisoryNode with id$")
  public void tryDeleteSupervisoryNode() {
    deleteSupervisoryNodeResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .delete(baseUrlOfService("referencedata") + "supervisoryNodes/" + id);
  }

  @Then("^I should get no content response")
  public void shouldGetNoContentResponse() {
    deleteSupervisoryNodeResponse
        .then()
        .statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @Then("^I should get not found response$")
  public void shouldGetNotFoundResponse() {
    getSupervisoryNodeResponse
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  private JsonObjectBuilder buildSupervisoryNode(Map<String, String> data) {
    JsonObjectBuilder facility = buildObject()
        .add("id", data.get("facilityId"));
    return buildObject()
        .add("code", (String) data.get("code"))
        .add("facility", facility)
        .add("name", (String) data.get("name"));
  }
}
