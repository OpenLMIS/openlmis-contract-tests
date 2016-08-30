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
