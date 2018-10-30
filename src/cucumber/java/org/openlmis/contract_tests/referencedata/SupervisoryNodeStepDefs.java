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

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.jglue.fluentjson.JsonBuilderFactory.buildObject;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import com.google.gson.JsonObject;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.jglue.fluentjson.JsonObjectBuilder;

public class SupervisoryNodeStepDefs {

  private static final String SUPERVISORY_NODE_URL = baseUrlOfService("referencedata")
      + "supervisoryNodes/";

  private Response createSupervisoryNodeResponse;
  private Response getSupervisoryNodeResponse;
  private Response deleteSupervisoryNodeResponse;
  private Response searchSupervisoryNodesResponse;
  private Response updateSupervisoryNodeResponse;
  private String id;

  @When("^I try to create a supervisoryNode with:$")
  public void tryToCreateSupervisoryNodeWith(DataTable argsList) {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    for (Map<String, String> map : data) {
      createSupervisoryNodeResponse = given()
          .contentType(ContentType.JSON)
          .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
          .body(buildSupervisoryNode(map, null)
              .getJson().toString())
          .when()
          .post(SUPERVISORY_NODE_URL);
    }
  }

  @When("^I try to find a supervisoryNode with:$")
  public void tryToGetupervisoryNodeWith(DataTable argsList) {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    for (Map<String, String> map : data) {
      searchSupervisoryNodesResponse = given()
          .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
          .queryParam("code", map.get("code"))
          .queryParam("name", map.get("name"))
          .queryParam("facilityId", map.get("facilityId"))
          .when()
          .get(SUPERVISORY_NODE_URL);
    }
  }

  @Then("^I should get response with the supervisoryNode's id$")
  public void shouldGetResponseWithTheSupervisoryNodeId() throws Throwable {
    searchSupervisoryNodesResponse
        .then()
        .body("content.id[0]", notNullValue());
    id = searchSupervisoryNodesResponse.jsonPath().getString("content.id[0]");
  }

  @When("^I try to update a supervisoryNode with:$")
  public void tryToUpdateSupervisoryNodeWith(DataTable argsList) {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    for (Map<String, String> map : data) {
      updateSupervisoryNodeResponse = given()
          .contentType(ContentType.JSON)
          .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
          .body(buildSupervisoryNode(map, id)
              .getJson().toString())
          .when()
          .put(SUPERVISORY_NODE_URL + id);
    }
  }

  @Then("^I should get response with OK status")
  public void shouldGetOkResponse() {
    updateSupervisoryNodeResponse
        .then()
        .statusCode(HttpStatus.SC_OK);
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
        .get(SUPERVISORY_NODE_URL + id);
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
        .delete(SUPERVISORY_NODE_URL + id);
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

  private JsonObjectBuilder buildSupervisoryNode(Map<String, String> data, String id) {
    JsonObjectBuilder<?, JsonObject> json = buildObject()
        .add("code", (String) data.get("code"))
        .add("facility", buildObject().add("id", data.get("facilityId")))
        .add("name", (String) data.get("name"));

    if (StringUtils.isNotBlank(id)) {
      json.add("id", id);
    }

    return json;
  }
}
