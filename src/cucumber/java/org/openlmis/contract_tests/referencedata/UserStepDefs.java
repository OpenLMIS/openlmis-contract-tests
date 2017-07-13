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
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;
import static org.openlmis.contract_tests.common.TestVariableReader.passwordOf;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public class UserStepDefs {

  private Response userResponse;
  private String userName;
  private String userId;

  private static final String BASE_URL_OF_REFERENCEDATA_SERVICE =
      baseUrlOfService("referencedata");

  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";

  static {
    enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @When("^I try to create a user with:$")
  public void tryToCreateNewUser(DataTable argsList) {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    for (Map map : data) {
      userResponse = given()
          .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
          .when()
          .body(createBodyForUser(map))
          .contentType(ContentType.JSON)
          .put(BASE_URL_OF_REFERENCEDATA_SERVICE + "users");
    }
  }

  @Then("^I should get response with the user’s id$")
  public void shouldGetResponseWithTheUserId() {
    userResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", notNullValue());
    userId = from(userResponse.asString()).get("id");
    userName = from(userResponse.asString()).get("username");
  }

  @When("^I try to get user with id$")
  public void tryToGetUserWithId() {
    userResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .get(BASE_URL_OF_REFERENCEDATA_SERVICE + "users/" + userId);
  }

  @Then("^I should get user with:$")
  public void shouldGetUserWith(DataTable argsList) {
    List<Map<String, String>> data = argsList.asMaps(String.class, String.class);
    for (Map map : data) {
      userResponse
          .then()
          .statusCode(HttpStatus.SC_OK)
          .body("id", is(userId))
          .body("username", is(map.get("username")))
          .body("firstName", is(map.get("firstName")))
          .body("lastName", is(map.get("lastName")))
          .body("email", is(map.get("email")))
          .body("timezone", is(map.get("timezone")))
          .body("verified", is(Boolean.parseBoolean(String.valueOf(map.get("verified")))))
          .body("active", is(Boolean.parseBoolean(String.valueOf(map.get("active")))))
          .body("loginRestricted",
              is(Boolean.parseBoolean(String.valueOf(map.get("loginRestricted")))));
    }
  }

  @When("^I try to set user password$")
  public void tryToSetUserPassword() {
    userResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .body(createBodyForSetPassword())
        .contentType(ContentType.JSON)
        .post(BASE_URL_OF_REFERENCEDATA_SERVICE + "users/auth/" + "passwordReset");
  }

  @Then("^I should get correct response$")
  public void shouldGetCorrectResponse() {
    userResponse
        .then()
        .statusCode(HttpStatus.SC_OK);
  }

  private JSONObject createBodyForUser(Map<String, String> args) {
    JSONObject json = new JSONObject();
    json.put("username", args.get("username"));
    json.put("firstName", args.get("firstName"));
    json.put("lastName", args.get("lastName"));
    json.put("email", args.get("email"));
    json.put("timezone", args.get("timezone"));
    json.put("verified", args.get("verified"));
    json.put("active", args.get("active"));
    json.put("loginRestricted", args.get("loginRestricted"));
    return json;
  }

  private JSONObject createBodyForSetPassword() {
    JSONObject json = new JSONObject();
    json.put("username", userName);
    json.put("newPassword", passwordOf(userName));
    return json;
  }

}
