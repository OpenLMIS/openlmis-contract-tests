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

package org.openlmis.contract_tests.sanity;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.jglue.fluentjson.JsonBuilderFactory.buildObject;

public class SanityStepDefs {

    private Response response;

    @Given("^I have created a post titled as (.*)$")
    public void IHaveCreatedAPostTitledAs(String title) throws Throwable {
        response = given()
                .contentType(JSON)
                .body(buildObject()
                        .add("title", title)
                        .getJson().toString())
                .when()
                .post("http://jsonplaceholder.typicode.com/posts");
    }

    @Then("^I should get back a response with title (.*)$")
    public void IShouldGetBackAResponseWithTitle(String title) {
        response.then().body("title", equalTo(title));
    }
}
