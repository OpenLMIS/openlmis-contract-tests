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

package org.openlmis.contract_tests.cce;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN_PARAM_NAME;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CceNotificationStepDefs {

  private static final String BASE_URL_OF_CCE_SERVICE = baseUrlOfService("cce");
  private static final String INVENTORY_ITEMS_URL = BASE_URL_OF_CCE_SERVICE + "inventoryItems/";

  private static final String REFERENCE_NAME = "referenceName";
  private static final String FUNCTIONAL_STATUS = "functionalStatus";
  private static final String REASON_NOT_WORKING_OR_NOT_IN_USE = "reasonNotWorkingOrNotInUse";

  private Response searchResponse;
  private Response changeStatusResponse;

  private JSONObject inventoryItem;

  @When("^I try to retrieve inventory items$")
  public void tryToRetrieveInventoryItemWithReferenceName(DataTable params) {
    RequestSpecification specification = given()
        .when()
        .contentType(ContentType.JSON)
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN);

    params.asMaps().forEach(map -> map.forEach(specification::queryParam));

    searchResponse = specification.get(INVENTORY_ITEMS_URL);
  }

  @Then("^I should get response with inventory item with reference name \"([^\"]*)?\"$")
  public void shouldGetResponseWithInventoryItemWithReferenceName(String referenceName)
      throws ParseException {
    JSONParser parser = new JSONParser();

    JSONObject page = (JSONObject) parser.parse(searchResponse.asString());
    JSONArray content = (JSONArray) page.get("content");
    inventoryItem = null;

    for (Object item : content) {
      JSONObject elem = (JSONObject) item;

      if (referenceName.equals(elem.get(REFERENCE_NAME))) {
        inventoryItem = elem;
        break;
      }
    }

    assertThat(inventoryItem)
        .as("Can't find an inventory item with reference name %s", referenceName)
        .isNotNull();
  }

  @When("^I try to change functionality status to ([A-Z_]+) with ([A-Z_]+) as a reason$")
  public void tryToChangeFunctionalityStatusWithReason(String status, String reason) {
    inventoryItem.put(FUNCTIONAL_STATUS, status);
    inventoryItem.put(REASON_NOT_WORKING_OR_NOT_IN_USE, reason);

    changeStatusResponse = given()
        .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
        .contentType(ContentType.JSON)
        .body(inventoryItem.toJSONString())
        .when()
        .put(INVENTORY_ITEMS_URL + inventoryItem.get("id"));

  }

  @Then("^I should get response with inventory item with ([A-Z_]+) functionality status$")
  public void shouldGetResponseWithInventoryItemWithFunctionalityStatus(String status) {
    changeStatusResponse
        .then()
        .statusCode(200)
        .body(FUNCTIONAL_STATUS, is(status));
  }

}
