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

package org.openlmis.contract_tests.stockmanagement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.openlmis.contract_tests.common.LoginStepDefs.ACCESS_TOKEN;
import static org.openlmis.contract_tests.common.TestVariableReader.baseUrlOfService;

public class StockCardsStepDefs {

    private Response idsResponse;
    private Response getCardResponse;

    private static final String URL_OF_STOCK_CARD_MANAGEMENT =
            baseUrlOfService("stockmanagement") + "stockCards/";

    public static final String URL_OF_STOCK_CARD_SUMMARIES =
            baseUrlOfService("stockmanagement") + "stockCardSummaries";

    private static final String ACCESS_TOKEN_PARAM_NAME = "access_token";
    private static final String PROGRAM_PARAM_NAME = "program";
    private static final String FACILITY_PARAM_NAME = "facility";

    private static final String FACILITY_ID = "176c4276-1fb1-4507-8ad2-cdfba0f47445";
    private static final String PROGRAM_ID = "dce17f2e-af3e-40ad-8e00-3496adef44c3";

    private String stockCardId;

    @Given("^I have got stock card id$")
    public void iHaveGotStockCardId() {
        idsResponse = given().contentType(ContentType.JSON)
                .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
                .queryParam(PROGRAM_PARAM_NAME, PROGRAM_ID)
                .queryParam(FACILITY_PARAM_NAME, FACILITY_ID)
                .when().get(URL_OF_STOCK_CARD_SUMMARIES).andReturn();

        stockCardId = idsResponse.jsonPath().getString("content.id[0]");
    }

    @When("^I try to get stock card with card id$")
    public void iTryToGetStockCardWithCardId() {
        getCardResponse = given()
                .contentType(ContentType.JSON)
                .queryParam(ACCESS_TOKEN_PARAM_NAME, ACCESS_TOKEN)
                .when()
                .get(URL_OF_STOCK_CARD_MANAGEMENT + stockCardId);
    }

    @Then("^I should get a stock card with (\\d+) stockOnHand$")
    public void iShouldGetAStockCardWithSOH(int value) {
        getCardResponse
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("stockOnHand", is(value));
    }

    @Then("^I should get response of incorrect user permission of view cards$")
    public void iShouldGetResponseOfIncorrectUserPermissionOfViewEvents() {
        getCardResponse
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }
}
