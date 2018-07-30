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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

public class ApiKeyStepDefs {
  private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";
  private static final String TOKEN_FIELD = "token";

  private static final String TOKEN_PATH = "/{" + TOKEN_FIELD + "}";

  private static final String BASE_URL_OF_REFERENCEDATA_SERVICE = baseUrlOfService("referencedata");
  private static final String BASE_URL_OF_AUTH_SERVICE = baseUrlOfService("auth");

  private static final String API_KEY_URL = BASE_URL_OF_AUTH_SERVICE + "apiKeys";

  private static final String SERVICE_ACCOUNT_URL = BASE_URL_OF_REFERENCEDATA_SERVICE
      + "serviceAccounts";

  private static final String API_KEY_DELETE_URL = API_KEY_URL + TOKEN_PATH;
  private static final String SERVICE_ACCOUNT_DELETE_URL = SERVICE_ACCOUNT_URL + TOKEN_PATH;

  private Response apiKeyResponse;
  private Response serviceAccountResponse;

  private String token;

  private Map<String, Response> resourceResponses = new HashMap<>();

  @When("^I try to create a API key$")
  public void tryToCreateApiKey() {
    apiKeyResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .when()
        .post(API_KEY_URL);

    String authResponse = apiKeyResponse
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body(TOKEN_FIELD, notNullValue())
        .extract()
        .asString();

    token = from(authResponse).get(TOKEN_FIELD);

    JSONObject json = new JSONObject();
    json.put(TOKEN_FIELD, token);

    serviceAccountResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType("application/json")
        .when()
        .body(json.toJSONString())
        .post(SERVICE_ACCOUNT_URL);
  }

  @Then("^I should get response with the new API key$")
  public void shouldGetResponseWithNewApiKey() {
    serviceAccountResponse
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body(TOKEN_FIELD, notNullValue());

    token = from(serviceAccountResponse.asString()).get(TOKEN_FIELD);
  }

  @When("^I try to retrieve a list of ([a-z]+) by using API key$")
  public void tryToRetrieveListOfResourceByUsingApiKey(String resource) {
    Response response = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, token)
        .when()
        .get(BASE_URL_OF_REFERENCEDATA_SERVICE + resource);

    resourceResponses.put(resource, response);
  }

  @Then("^I should get the list of ([a-z]+)$")
  public void shouldGetListOfResources(String resource) {
    resourceResponses
        .get(resource)
        .then()
        .statusCode(HttpStatus.SC_OK);
  }

  @Then("^I should get an error of missing permission to retrieve the list of ([a-z]+)$")
  public void shouldGetErrorOfMissingPermissionToRetrieveListOfResources(String resource) {
    resourceResponses
        .get(resource)
        .then()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .body("messageKey", startsWith("referenceData.error.unauthorized"));
  }

  @Then("^I should get an error of missing authorization to retrieve the list of ([a-z]+)$")
  public void shouldGetErrorOfMissingAuthorizationToRetrieveListOfResources(String resource) {
    resourceResponses
        .get(resource)
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("error", startsWith("invalid_token"));
  }

  @When("^I try to remove created API key$")
  public void tryToRemoveCreatedApiKey() {
    serviceAccountResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .pathParam(TOKEN_FIELD, token)
        .when()
        .delete(SERVICE_ACCOUNT_DELETE_URL);

    apiKeyResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .pathParam(TOKEN_FIELD, token)
        .when()
        .delete(API_KEY_DELETE_URL);
  }

  @Then("^I should get response of deleted API key$")
  public void shouldGetResponseOfDeletedApiKey() {
    serviceAccountResponse
        .then()
        .statusCode(HttpStatus.SC_NO_CONTENT);

    apiKeyResponse
        .then()
        .statusCode(HttpStatus.SC_NO_CONTENT);
  }

}
