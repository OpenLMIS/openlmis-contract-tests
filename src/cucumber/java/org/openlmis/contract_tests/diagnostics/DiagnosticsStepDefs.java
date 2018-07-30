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

package org.openlmis.contract_tests.diagnostics;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class DiagnosticsStepDefs {
  private Response healthResponse;

  private static final String BASE_URL_OF_DIAGNOSTICS_SERVICE =
      baseUrlOfService("diagnostics");

  @When("^I try to retrieve system health status$")
  public void tryToRetrieveSystemHealthStatus() {
    healthResponse = given()
        .when()
        .contentType(ContentType.JSON)
        .get(BASE_URL_OF_DIAGNOSTICS_SERVICE + "health");
  }

  @Then("^I should get response with current system health status$")
  public void shouldGetResponseWithCurrentSystemHealthStatus() {
    healthResponse
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("$.size()", equalTo(1))
        .body("status", hasItem("PASSING"))
        .body("serviceName", hasItem("referencedata"));
  }

}
